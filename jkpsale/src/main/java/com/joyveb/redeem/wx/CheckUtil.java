package com.joyveb.redeem.wx;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.UUID;

public class CheckUtil {

    public static final String  token = "xiaodou"; //开发者自行定义Token
    /**
     * 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式 （即 key1=value1&key2=value2…）拼接成字符串string1
     * @param nonce_str
     * @param timestamp
     * @param jsapi_ticket
     * @param url
     * @return
     */
    public static String getString1(String nonce_str,String timestamp,String jsapi_ticket,String url){
        //1.定义数组存放nonce_str,timestamp,jsapi_ticket,url
        String[] arr = {"noncestr="+nonce_str,"timestamp="+timestamp,"jsapi_ticket="+jsapi_ticket,"url="+url};
        //2.对数组进行排序
        Arrays.sort(arr);
        //3.生成字符串
        StringBuffer sb = new StringBuffer();
        for(String s : arr){
            sb.append(s);
            sb.append("&");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
    public static String getSha1(String str){
        if(str==null||str.length()==0){
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
    public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
