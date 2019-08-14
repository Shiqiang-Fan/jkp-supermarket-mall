package com.joyveb.redeem.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

public class MixBarcodeUtil {
    private final static String CHARSET = "utf-8";
    /**
     * 混淆数组
     */
    private static char[] mixArr = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z'};

    private static Random rand = new Random();

    /**
     * 混淆值
     */
    private static int mixValue = 4;

    public static void main(String[] args) throws Exception {
        String merchantKey = "0123456789888891";
        String barcode = "这是一份测试数据";

        //加密
        String mixBarcode = MixBarcodeUtil.encodeBarcode(barcode);
        //混淆
        String ciphertext = MixBarcodeUtil.desEncrypt(mixBarcode, merchantKey);

        //解密
        String decryptMixBarcode = MixBarcodeUtil.desDecrypt(ciphertext, merchantKey);
        //还原
        String decryptBarcoe = MixBarcodeUtil.decodeBarcode(decryptMixBarcode);

        if (barcode.equals(decryptBarcoe)) {
            System.out.println("加解密解密成功");
        } else {
            System.err.println("加解密解密失败");
        }
    }



    /**
     * 对cipherText进行DES解密
     */
    public static String desDecrypt(String cipherText, String desKey)
            throws Exception {
        String decryptStr = null;
        try {
            // 解密
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            byte[] input = Base64.decodeBase64(cipherText.trim().getBytes(
                    CHARSET));

            cipher.init(Cipher.DECRYPT_MODE, genSecretKey(desKey));
            byte[] output = cipher.doFinal(input);
            decryptStr = new String(output, CHARSET);

        } catch (Exception e) {
            throw new Exception("DES解密发生异常!", e);
        }

        return decryptStr;
    }

    /**
     * 对message进行DES加密
     */
    public static String desEncrypt(String message, String desKey)
            throws Exception {
        String encryptStr = null;
        byte encryptByte[];

        try {
            // 加密
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, genSecretKey(desKey));
            byte[] cipherText = cipher
                    .doFinal(message.trim().getBytes(CHARSET));
            encryptByte = Base64.encodeBase64(cipherText);
            encryptStr = new String(encryptByte, CHARSET);
            encryptStr = encryptStr.replaceAll("\r\n", "").replaceAll("\n", "");

        } catch (Exception e) {
            throw new Exception("des加密发生异常!", e);
        }
        return encryptStr;
    }

    private static Key genSecretKey(String key)
            throws InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        DESKeySpec desKeySpec = new DESKeySpec(hexStringToByte(key));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

        return secretKey;
    }

    private static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 打散混淆数组
     */
    private static void scatterMixArr() {
        int arrLen = mixArr.length;

        for (int i = 0; i < arrLen; i++) {
            int idx = Math.abs(rand.nextInt()) % arrLen;

            char tmpData = mixArr[idx];
            mixArr[idx] = mixArr[arrLen - 1];
            mixArr[arrLen - 1] = tmpData;
        }
    }

    /**
     * 解密二维码
     *
     * @param srcStr 加密的二维码
     * @return 解密后的数据
     */
    public static String decodeBarcode(String srcStr) {
        char[] tmpArr = srcStr.toCharArray();

        // 减权值
        for (int i = 0; i < tmpArr.length; i++) {
            tmpArr[i] = (char) (tmpArr[i] - mixValue);
        }

        int arr_len = tmpArr.length;
        int half_len = tmpArr.length / 2;

        // 置换回来
        for (int i = 0; i < half_len; i++) {
            char tmpData = tmpArr[i];

            tmpArr[i] = tmpArr[arr_len - i - 1];
            tmpArr[arr_len - i - 1] = tmpData;
        }

        // 结果数组
        char[] destArr = new char[half_len];

        for (int i = 0; i < destArr.length; i++) {
            destArr[i] = tmpArr[i * 2];
        }

        return new String(destArr);
    }

    /**
     * 获得经过混淆后的数组数据
     *
     * @param srcStr 需要加密的字符串
     * @return 加入混淆数组后的数据
     */
    private static char[] getBarArrByMix(String srcStr) {
        char[] tmpArr = srcStr.toCharArray();
        char[] destArr = new char[tmpArr.length * 2];

        int len = tmpArr.length;
        int mixArr_len = mixArr.length;

        // 数据之间用混淆数据分隔
        for (int i = 0; i < len; i++) {
            destArr[i * 2] = tmpArr[i];
            destArr[i * 2 + 1] = i < mixArr_len ? mixArr[i]
                    : mixArr[mixArr_len - 1];
        }

        return destArr;
    }

    /**
     * 通过混淆数组加密
     *
     * @param srcStr 需要加密的二维码
     * @return 加密后的数据
     */
    private static String encodeBarcodeByMixArr(String srcStr) {
        char[] tmpArr = getBarArrByMix(srcStr);

        int arr_len = tmpArr.length;
        int half_len = tmpArr.length / 2;

        // 置换
        for (int i = 0; i < half_len; i++) {
            char tmpData = tmpArr[i];

            tmpArr[i] = tmpArr[arr_len - i - 1];
            tmpArr[arr_len - i - 1] = tmpData;
        }

        // 加权值
        for (int i = 0; i < tmpArr.length; i++) {
            tmpArr[i] = (char) (tmpArr[i] + mixValue);
        }

        return new String(tmpArr);
    }

    /**
     * 加密二维码
     *
     * @param srcStr 需要加密的二维码
     * @return 加密后的数据
     */
    public static String encodeBarcode(String srcStr) {
        // 混淆数组打散
        scatterMixArr();

        return encodeBarcodeByMixArr(srcStr);
    }
}
