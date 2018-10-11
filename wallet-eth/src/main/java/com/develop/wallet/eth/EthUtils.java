package com.develop.wallet.eth;

import com.develop.wallet.eth.utils.HexUtils;
import com.develop.wallet.eth.utils.KECCAK256;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Convert;

import java.math.BigInteger;

/**
 * @author Angus
 */
public class EthUtils {

    /**
     * 生成 Checksum 的地址
     *
     * @param address 普通地址
     * @return Checksum 地址
     */
    public static String toChecksumAddress(String address) {
        String lowercaseAddress = cleanHexPrefix(address).toLowerCase();
        String addressHash = (HexUtils.toHex(KECCAK256.keccak256(lowercaseAddress.getBytes())));

        StringBuilder result = new StringBuilder(lowercaseAddress.length() + 2);

        if (containsHexPrefix(address)) {
            result.append("0x");
        }

        for (int i = 0; i < lowercaseAddress.length(); i++) {
            if (Integer.parseInt(String.valueOf(addressHash.charAt(i)), 16) >= 8) {
                result.append(String.valueOf(lowercaseAddress.charAt(i)).toUpperCase());
            } else {
                result.append(lowercaseAddress.charAt(i));
            }
        }

        return result.toString();
    }

    /**
     * 检查 ETH 地址是否合法
     *
     * @param address ETH 地址
     * @return true：合法 false：不合法
     */
    public static boolean checksumAddress(String address) {
        if (!containsHexPrefix(address)) {
            //没有前缀 0x
            return false;
        } else if (checkUppercase(cleanHexPrefix(address))) {
            String checksumAddress = toChecksumAddress(address);
            if (checksumAddress.equals(address)) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检查字符串中是否有大写英文字母
     *
     * @param address ETH 地址
     * @return true：大写 false：小写
     */
    private static boolean checkUppercase(String address) {
        for (int i = 0; i < address.length(); i++) {
            char c = address.charAt(i);
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如果字符串 0x 开头则去掉
     *
     * @param input 要处理的字符串
     * @return 处理完成的字符串
     */
    public static String cleanHexPrefix(String input) {
        if (containsHexPrefix(input)) {
            return input.substring(2);
        } else {
            return input;
        }
    }

    /**
     * 检查字符串是否是 0x 开头
     *
     * @param input 要检测的字符串
     * @return true：0x 开头 false：不是 0x 开头
     */
    public static boolean containsHexPrefix(String input) {
        return !isEmpty(input) && input.length() > 1
                && input.charAt(0) == '0' && input.charAt(1) == 'x';
    }

    private static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

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
