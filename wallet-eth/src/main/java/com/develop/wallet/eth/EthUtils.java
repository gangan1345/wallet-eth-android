package com.develop.wallet.eth;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Convert;

import java.math.BigInteger;

/**
 * @author Angus
 */
public class EthUtils {

    /**
     * 通过私钥获取地址
     *
     * @param ecKeyPair
     * @return
     */
    public static String getAddress(ECKeyPair ecKeyPair) {
        return replaceAddress(Keys.getAddress(ecKeyPair));
    }

    /**
     * 获取私钥
     *
     * @param ecKeyPair
     * @return
     */
    public static String getPrivateKey(ECKeyPair ecKeyPair) {
        return toKeyString(ecKeyPair.getPrivateKey());
    }

    /**
     * 获取公钥
     *
     * @param ecKeyPair
     * @return
     */
    public static String getPublicKey(ECKeyPair ecKeyPair) {
        return toKeyString(ecKeyPair.getPublicKey());
    }

    /**
     * 格式化地址
     *
     * @param address
     * @return
     */
    public static String replaceAddress(String address) {
        if (address != null && !address.startsWith("0x")) {
            return "0x" + address;
        }
        return address;
    }

    /**
     * 私钥 公钥 BigInteger 类型转成string
     *
     * @param key
     * @return
     */
    public static String toKeyString(BigInteger key) {
        return key.toString(16);
    }

    /**
     * 将私钥  公钥转成BigInteger
     *
     * @param key
     * @return
     */
    public static BigInteger toKeyBigInteger(String key) {
        return new BigInteger(key, 16);
    }

    /**
     * 1 x 10   18次方
     *
     * @param num
     * @return
     */
    public static BigInteger toBigInteger(String num) {
        return Convert.toWei(num, Convert.Unit.ETHER).toBigInteger();
    }

    public static String toStringNum(BigInteger num) {
        return Convert.fromWei(String.valueOf(num), Convert.Unit.ETHER).toString();
    }
}
