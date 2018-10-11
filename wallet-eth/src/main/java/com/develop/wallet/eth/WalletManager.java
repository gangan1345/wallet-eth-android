package com.develop.wallet.eth;

import com.develop.mnemonic.MnemonicUtils;
import com.develop.wallet.eth.listener.WalletListener;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Angus
 */
public class WalletManager {

    public static boolean DEBUG = true;
    /**
     * 代币合约地址
     */
    public static String tokenAddres = "0x047a880b27ca11509c9c1e853fe47229febc57fe";

    /**
     * 区块链服务器地址
     */
    public static String URL = "http://192.168.1.138";


    public static BigInteger GAS_PRICE = BigInteger.valueOf(0x3b9aca00);
    public static BigInteger GAS_LIMIT = BigInteger.valueOf(0x493e0);

    private static Web3j web3j;
    private static Admin admin;

    private static ExecutorService mExecutorService;

    public static Web3j getWeb3j() {
        if (web3j == null) {
            web3j = Web3jFactory.build(new HttpService(URL));
        }
        return web3j;
    }


    public static Admin getAdmin() {
        if (admin == null) {
            admin = AdminFactory.build(new HttpService(URL));
        }
        return admin;
    }

    public static ExecutorService getExecutorService() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newCachedThreadPool();
        }
        return mExecutorService;
    }

    /**
     * 配置地址
     *
     * @param url
     * @param token
     * @param debug
     */
    public static void config(String url, String token, boolean debug) {
        URL = url;
        tokenAddres = token;
        DEBUG = debug;
    }

    /**
     * 生成钱包地址
     */
    public static Wallet generateWalletAddress() {
        try {
            String mnemonic = MnemonicUtils.generateMnemonic();
            ECKeyPair ecKeyPair = WalletUtils.generateBip32ECKeyPair(mnemonic);
            String address = EthUtils.getAddress(ecKeyPair);

            String privateKey = EthUtils.getPrivateKey(ecKeyPair);
            String publicKey = EthUtils.getPublicKey(ecKeyPair);
            log(String.format("generateWalletAddress: mnemonic = %s, address = %s, privateKey = %s", mnemonic, address, privateKey));
            return new Wallet(mnemonic, address, privateKey, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据助记词获取地址
     *
     * @param mnemonic
     * @return
     */
    public static String generateAddress(String mnemonic) {
        ECKeyPair ecKeyPair = WalletUtils.generateBip32ECKeyPair(mnemonic);
        String address = EthUtils.getAddress(ecKeyPair);
        log(String.format("generateAddress: mnemonic = %s, address = %s", mnemonic, address));
        return address;
    }

    /**
     * 通过助记词获取私钥
     *
     * @param mnemonic
     */
    public static String generatePrivateKey(String mnemonic) {
        ECKeyPair ecKeyPair = WalletUtils.generateBip32ECKeyPair(mnemonic);
        String privateKey = EthUtils.getPrivateKey(ecKeyPair);
        log(String.format("generatePrivateKey: mnemonic = %s, privateKey = %s", mnemonic, privateKey));
        return privateKey;
    }

    /**
     * 生成钱包keystore
     *
     * @param password
     * @param mnemonic
     * @return
     */
    public static Wallet generateWalletKeystore(String password, String mnemonic) {
        try {
            Wallet wallet = WalletUtils.generateBip32Wallet(password, mnemonic);
            if (wallet != null) {
                log(String.format("generateWalletKeystore: mnemonic = %s, password = %s, privateKey = %s, keystore = %s", mnemonic, password, wallet.getPrivateKey(), wallet.getKeystore()));
            }
            return wallet;
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过keystore 获取私钥
     *
     * @param password
     * @param keystore
     */
    public static void generatePrivateKey(String password, String keystore) {
        try {
            ECKeyPair ecKeyPair = WalletUtils.loadKeystore(password, keystore);
            String address = EthUtils.getAddress(ecKeyPair);
            String privateKey = EthUtils.getPrivateKey(ecKeyPair);
            log(String.format("generatePrivateKey: privateKey = %s, address = %s, password = %s, keystore = %s", privateKey, address, password, keystore));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据私钥转账
     *
     * @param fromAddress
     * @param privateKey
     * @param toAddress
     * @param amount
     * @return
     */
    public static String sendTransactionByPrivateKey(String fromAddress, String privateKey, String toAddress, String amount) {
        ECKeyPair ecKeyPair = ECKeyPair.create(EthUtils.toKeyBigInteger(privateKey));
        return sendTransaction(fromAddress, ecKeyPair, toAddress, EthUtils.toBigInteger(amount));
    }


    public static String sendTransactionByPrivateKey(String fromAddress, BigInteger privateKey, String toAddress, String amount) {
        ECKeyPair ecKeyPair = ECKeyPair.create(privateKey);
        return sendTransaction(fromAddress, ecKeyPair, toAddress, EthUtils.toBigInteger(amount));
    }

    /**
     * 根据助记词转账
     *
     * @param fromAddress
     * @param mnemonic
     * @param toAddress
     * @param amount
     * @return
     */
    public static String sendTransactionByMnemonic(String fromAddress, String mnemonic, String toAddress, String amount) {
        ECKeyPair ecKeyPair = WalletUtils.generateBip32ECKeyPair(mnemonic);
        return sendTransaction(fromAddress, ecKeyPair, toAddress, EthUtils.toBigInteger(amount));
    }

    /**
     * 根据助记词异步调用转账
     *
     * @param fromAddress
     * @param mnemonic
     * @param toAddress
     * @param amount
     * @param listener
     */
    public static void sendTransactionByMnemonicAsync(final String fromAddress,
                                                      final String mnemonic,
                                                      final String toAddress,
                                                      final String amount,
                                                      final WalletListener listener) {
        getExecutorService().submit(new Runnable() {
            @Override
            public void run() {
                ECKeyPair ecKeyPair = WalletUtils.generateBip32ECKeyPair(mnemonic);
                String hash = sendTransaction(fromAddress, ecKeyPair, toAddress, EthUtils.toBigInteger(amount));
                if (listener != null) {
                    listener.onSendTransaction(hash);
                }
            }
        });
    }

    /**
     * 发送转账交易
     */
    private static String sendTransaction(String fromAddress, ECKeyPair ecKeyPair, String toAddress, BigInteger amount) {
        // noce
        BigInteger nonce = getNonce(fromAddress);

        String hash = sendTransaction(fromAddress, ecKeyPair, toAddress, amount, nonce, 0);
        return hash;
    }

    private static int maxResetCount = 100;

    private static String sendTransaction(String fromAddress, ECKeyPair ecKeyPair, String toAddress, BigInteger amount, BigInteger nonce, int resetCount) {
        try {
//            PersonalUnlockAccount personalUnlockAccount = getAdmin().personalUnlockAccount(fromAddress, "").send();
//            if (personalUnlockAccount.accountUnlocked()) {
//            }
            List inputParameters = Arrays.asList(new Address(toAddress), new Uint256(amount));
            List outputParameters = Arrays.asList(new TypeReference<Address>() {
            }, new TypeReference<Bool>() {
            });

            //创建交易  注意金额 保留小数点后8位 要转化为整数 比如0.00000001 转化为1
            Function function = new Function("transfer", inputParameters, outputParameters);
            String data = FunctionEncoder.encode(function);

            //智能合约事物
//            BigInteger GAS_PRICE = Convert.toWei(BigDecimal.valueOf(5), Convert.Unit.GWEI).toBigInteger();
//            BigInteger GAS_LIMIT = Convert.toWei(BigDecimal.valueOf(60000), Convert.Unit.GWEI).toBigInteger();
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, tokenAddres, data);

            //通过私钥获取凭证  当然也可以根据其他的获取 其他方式详情请看web3j
            Credentials credentials = Credentials.create(ecKeyPair);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            //发送事务
            EthSendTransaction ethSendTransaction = null;
            try {
                ethSendTransaction = getWeb3j().ethSendRawTransaction(hexValue).sendAsync().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //事物的HASH
            String transactionHash = ethSendTransaction.getTransactionHash();
            // nonce +1 重试
            log(String.format("sendTransaction: hash = %s, nonce = %s", transactionHash, String.valueOf(nonce)));
            if (transactionHash == null) {
                if (resetCount < maxResetCount) {
                    nonce = nonce.add(BigInteger.valueOf(1));
                    resetCount += 1;
                    log(String.format("sendTransaction reset: count = %s, nonce = %s", String.valueOf(resetCount), String.valueOf(nonce)));
                    return sendTransaction(fromAddress, ecKeyPair, toAddress, amount, nonce, resetCount++);
                }
            }
            return transactionHash;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取代币余额
     *
     * @param address
     * @param listener
     */
    public static void getTokenBalanceAsync(final String address, final WalletListener listener) {
        getExecutorService().submit(new Runnable() {
            @Override
            public void run() {
                String num = getTokenBalance(address);
                if (listener != null) {
                    listener.onQueryTokenBalance(num);
                }
            }
        });
    }

    /**
     * 获取版本信息
     *
     * @return
     */
    public static String getClientVersion() {
        String clientVersion = "";
        try {
            Web3ClientVersion web3ClientVersion = getWeb3j().web3ClientVersion().sendAsync().get();
            clientVersion = web3ClientVersion.getWeb3ClientVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log("clientVersion:" + clientVersion);
        return clientVersion;
    }

    /**
     * 获取以太币余额
     *
     * @param address
     * @return
     */
    public static BigInteger getEthBalance(String address) {
        BigInteger balance = BigInteger.ZERO;
        try {
            balance = getWeb3j().ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log("eth-balance:" + EthUtils.toStringNum(balance));
        return balance;
    }


    /**
     * 获取NONCE
     */
    public static BigInteger getNonce(String address) {
        BigInteger nonce = BigInteger.ZERO;
        try {
            EthGetTransactionCount ethGetTransactionCount = getWeb3j().ethGetTransactionCount(
                    address, DefaultBlockParameterName.PENDING).sendAsync().get();
            nonce = ethGetTransactionCount.getTransactionCount();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        log("nonce:" + nonce);
        return nonce;
    }

    public static <T> T queryToken(Class<T> t, String methodName, List<Type> inputParameters,
                                   List<TypeReference<?>> outputParameters, String from, String to) {
        Function function = new Function(
                methodName,//交易的方法名称
                inputParameters,
                outputParameters
        );
        String data = FunctionEncoder.encode(function);
        //智能合约事物
        Transaction transaction = Transaction.createEthCallTransaction(from, to, data);
        EthCall ethCall;
        try {
            ethCall = getWeb3j().ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            return (T) results.get(0).getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询代币余额
     */
    public static String getTokenBalance(String address) {
        List inputParameters = Arrays.asList(new Address(address));
        List outputParameters = Arrays.asList(new TypeReference<Uint256>() {
        });
        BigInteger banalce = queryToken(BigInteger.class, "balanceOf", inputParameters, outputParameters, address, tokenAddres);
        if (banalce == null) {
            banalce = BigInteger.ZERO;
        }
        String num = EthUtils.toStringNum(banalce);
        log(String.format("address:%s, token-balance:%s", address, num));
        return num;
    }

    /**
     * 查询代币名称
     */
    public static String getTokenName(String address) {
        List inputParameters = new ArrayList<>();
        List outputParameters = Arrays.asList(new TypeReference<Utf8String>() {
        });
        String name = queryToken(String.class, "name", inputParameters, outputParameters, address, tokenAddres);
        log("token-name:" + name);
        return name;
    }

    /**
     * 查询代币符号
     */
    public static String getTokenSymbol(String address) {
        List inputParameters = new ArrayList<>();
        List outputParameters = Arrays.asList(new TypeReference<Utf8String>() {
        });
        String symbol = queryToken(String.class, "symbol", inputParameters, outputParameters, address, tokenAddres);
        log("token-symbol:" + symbol);
        return symbol;
    }

    /**
     * 查询代币精度
     */
    public static BigInteger getTokenDecimals(String address) {
        List inputParameters = new ArrayList<>();
        List outputParameters = Arrays.asList(new TypeReference<Uint256>() {
        });
        BigInteger decimals = queryToken(BigInteger.class, "decimals", inputParameters, outputParameters, address, tokenAddres);
        log("token-decimals:" + decimals);
        return decimals;
    }

    /**
     * 查询代币发行总量
     */
    public static BigInteger getTokenTotalSupply(String address) {
        List inputParameters = new ArrayList<>();
        List outputParameters = Arrays.asList(new TypeReference<Uint256>() {
        });
        BigInteger totalSupply = queryToken(BigInteger.class, "totalSupply", inputParameters, outputParameters, address, tokenAddres);
        log("token-totalSupply:" + totalSupply);
        return totalSupply;
    }

    private static void log(String message) {
        if (DEBUG) {
//            Log.i("wallet", "WalletManager->" + message);
            System.out.println("WalletManager->" + message);
        }
    }
}
