package com.develop.wallet.eth;


/**
 * @author Angus
 */
public class Test {
    /**
     * address 1
     * volume final loyal match glare era olive size craft deposit palm label
     * privateKey:64697273701363975667816058824460362985711449577878882500586854681900954954961
     * publicKey:5496711611248940009022846050672889666971339526342890590677810753113420676926314424138619206773289034944759711160587975852529230026816469867207949082550707
     * address:8c10a04b0ce0414b089efe89e311f75fbf964563
     * {"address":"8c10a04b0ce0414b089efe89e311f75fbf964563","id":"64d8dbd2-ee5e-420d-959a-8c92f08cf72c","version":3,"crypto":{"cipher":"aes-128-ctr","ciphertext":"21f340e3ed878fef7ba15e9b6ba1a4a021c102be45d26875d1a9e07e7bec2a5e","cipherparams":{"iv":"7d0f058d57e4e5264ee9c8f5cdff4c21"},"kdf":"scrypt","kdfparams":{"dklen":32,"n":262144,"p":1,"r":8,"salt":"eb1052398418fb3c8525cff4161d2d44c19f965a87f5659a590b4adcd841ae09"},"mac":"e005db91c4d8b73946e2c69844481d76b49e0393885c7cae565aa1062d738325"}}
     */
    public static String address1 = "0x8c10a04b0ce0414b089efe89e311f75fbf964563";
    /**
     * address 2
     * height tape damp skirt fly security yellow bird better ice coach add
     * privateKey:94928797919772738040674919697082738632439476913208251040593217504257094419781
     * publicKey:9963975966879740012168411787063223483260685129620970304341137834558077381391001031279292016326899272767649562005523884756503412697756001931046132478881715
     * address:e94791399f3a0e6d9ac23e64102005efef1bb424
     * {"address":"e94791399f3a0e6d9ac23e64102005efef1bb424","id":"ff3c4eca-59bb-4baa-a5b7-a918c97d128b","version":3,"crypto":{"cipher":"aes-128-ctr","ciphertext":"a45f93bb597514200f86640b0cdcbfcc868bb6c6961fe0952d70f5a21fa81901","cipherparams":{"iv":"fcd188ded167bb8b879b244d5feede89"},"kdf":"scrypt","kdfparams":{"dklen":32,"n":262144,"p":1,"r":8,"salt":"44eb71d5ee5cb96bc2c0787c038455abf0cfecec2e30dcecb4bb7f5141f20399"},"mac":"94389c71f8710be5b459cadf6583bbb16aa8770fa2449b09ed6c4f49b5bbbc43"}}
     */
    public static String address2 = "0xe94791399f3a0e6d9ac23e64102005efef1bb424";

    public static void query(String address) {
        // 获取版本信息
        WalletManager.getClientVersion();
        // ethBalance 以太坊余额
        WalletManager.getEthBalance(address);
        // noce
        WalletManager.getNonce(address);
        // name
        WalletManager.getTokenName(address);
        // symbol
        WalletManager.getTokenSymbol(address);
        // decimals
        WalletManager.getTokenDecimals(address);
        // totalsupply
        WalletManager.getTokenTotalSupply(address);
        // token balance 代币余额
        WalletManager.getTokenBalance(address);
    }

    public static final void main(String[] args) {
        // 生成钱包地址
        Wallet wallet = WalletManager.generateWalletAddress();
        // 根据助记词获取地址
        WalletManager.generateAddress(wallet.getMnemonic());
        // 通过助记词获取私钥
        WalletManager.generatePrivateKey(wallet.getMnemonic());
        // 生成钱包keystore
        Wallet wallet1 = WalletManager.generateWalletKeystore("123456", wallet.getMnemonic());
        // 通过keystore 获取私钥
        WalletManager.generatePrivateKey("123456", wallet1.getKeystore());

//        String hash = WalletManager.sendTransactionByMnemonic("0x8c10a04b0ce0414b089efe89e311f75fbf964563",
//                "volume final loyal match glare era olive size craft deposit palm label",
//                "0xe94791399f3a0e6d9ac23e64102005efef1bb424", String.valueOf(0.1));


//        WalletManager.sendTransactionByMnemonicAsync("0x8c10a04b0ce0414b089efe89e311f75fbf964563",
//                "volume final loyal match glare era olive size craft deposit palm label",
//                "0xe94791399f3a0e6d9ac23e64102005efef1bb424", String.valueOf(0.1), null);


//        query("0x8c10a04b0ce0414b089efe89e311f75fbf964563");

//        System.out.println(EthUtils.toChecksumAddress("0x8c10a04b0ce0414b089efe89e311f75fbf964563"));
    }
}
