package com.joyveb.redeem.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

/**
 * @author wenchao
 * @date 18:35 2018/6/4
 */
@Slf4j
public final class WeChatSignUtil {

    private static final int TWO_HUNDRED_AND_FIFTY_SIX = 256;
    private static final int SIXTEEN = 16;

    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "a", "b", "c", "d", "e", "f"};

    private WeChatSignUtil() {
    }

    public static String createSign(SortedMap<Object, Object> parameters, String key,
        String characterEncoding) {
        StringBuilder sb = new StringBuilder();
        Set<Entry<Object, Object>> entries = parameters.entrySet();

        for (Entry<Object, Object> entry : entries) {
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k).append('=').append(v).append('&');
            }
        }

        sb.append("key=").append(key);
        return md5Encode(sb.toString(), characterEncoding).toUpperCase();
    }

    private static String md5Encode(String origin, String charsetname) {
        String resultString = origin;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(
                    StandardCharsets.UTF_8)));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (NoSuchAlgorithmException e) {
            log.info("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            log.info("UnsupportedEncodingException", e);
        }

        return resultString;
    }

    private static String byteArrayToHexString(byte[] bs) {
        StringBuilder resultSb = new StringBuilder();
        for (byte b : bs) {
            resultSb.append(byteToHexString(b));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += TWO_HUNDRED_AND_FIFTY_SIX;
        }
        int d1 = n / SIXTEEN;
        int d2 = n % SIXTEEN;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }


}
