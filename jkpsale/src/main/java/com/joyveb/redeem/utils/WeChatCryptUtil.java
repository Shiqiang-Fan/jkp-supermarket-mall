package com.joyveb.redeem.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

/**
 * @author chenjia on 2018/8/7
 */
@Slf4j
public class WeChatCryptUtil {

    public static String decrypt(String encryptedData, String iv, String sessionKey) throws Exception {
        /**
         * 小程序加密数据解密算法
         * https://developers.weixin.qq.com/miniprogram/dev/api/signature.html#wxchecksessionobject
         * 1.对称解密的目标密文为 Base64_Decode(encryptedData)。
         * 2.对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
         * 3.对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
         */
        byte[] encryptedByte = Base64.decodeBase64(encryptedData);
        byte[] sessionKeyByte = Base64.decodeBase64(sessionKey);
        byte[] ivByte = Base64.decodeBase64(iv);
        /**
         * 以下为AES-128-CBC解密算法
         */
        SecretKeySpec skeySpec = new SecretKeySpec(sessionKeyByte, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] original = cipher.doFinal(encryptedByte);
        return new String(original, Charset.defaultCharset());
    }


    public static void main(String[] args) throws Exception {
        String s = "HgiqrQhfEIMGRmGMg6dWI3MuXpWGaTw0BFuXIs+aNcmQJHqn2lAaXAnQE1ieq7YiDxFjzkeji7AX5tDnf1J/e9ScaAAEY9SgTt8C2a1b38h4dzlV3mkQVeWilj9apnBmYwa2LM6gVidBucwNfViIP+rrD+cGFu3QAgetdnaniBFZHu2xGBwbrWIyJkWYclEycvfwVFH5a9id2gr2813lAg==";

        String s1 = "LNstc0VHtSLqPR8Ftx5+OQ==";
//        String s1 = "XftpuFn1pSiT7KGK5C4Omw==";
        String s2 = "kjB/jmMKY9aHRcr0HKPtDw==";
        System.out.println(WeChatCryptUtil.decrypt(s, s2, s1).trim());
    }

}
