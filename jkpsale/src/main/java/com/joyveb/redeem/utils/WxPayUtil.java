package com.joyveb.redeem.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.joyveb.redeem.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
@PropertySource(value = {"classpath:pay.properties"})
public class WxPayUtil {

    private static String APPID;

    private static String MCH_ID;

    private static String NOTIFY_URL;

    private static String TRADE_TYPE_JS;

    private static String KEY;

    private static String APP_SECRET;

    @Autowired
    private OrderService orderService;

    /**
     * 根据code获取openid
     * @param code
     * @return
     * @throws IOException
     */
    public static JsonObject getOpenIdByCode(String code) throws IOException {
        //请求该链接获取access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code"
                .replace("APPID", APPID).replace("SECRET", APP_SECRET).replace("CODE", code);
        log.info("组装后的url：" + url);

        HttpGet httpget = new HttpGet(url);
        //设置浏览器
        CloseableHttpClient httpclient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        //发起请求
        String response = httpclient.execute(httpget,responseHandler);
        //获得请求内容
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(response);

        log.info("获取到请求内容：" + jsonObject);
        return jsonObject;
    }


    /**
     * 统一下单
     * @param body 商品描述
     * @param out_trade_no 商户订单号,自定义，一般为时间戳+6位随机字符串
     * @param total_fee 商品总价，单位为分
     * @param IP APP和网页支付提交用户端ip地址
     * @param openid 用户在本公众号的唯一标识
     * @return
     * @throws IOException
     */
    public static String unifiedOrder(String body,String out_trade_no,String total_fee,String IP,String openid,String appId)throws IOException {

        String key = getKey(appId);
        //设置访问路径
        HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
        String nonce_str = getNonceStr().toUpperCase();//随机
        //组装请求参数,按照ASCII排序
        String sign = "appid=" + appId +
                "&body=" + body +
                "&mch_id=" + MCH_ID +
                "&nonce_str=" + nonce_str +
                "&notify_url=" + NOTIFY_URL+
                "&openid=" + openid +
                "&out_trade_no=" + out_trade_no +
                "&spbill_create_ip=" + IP +
                "&total_fee=" + total_fee +
                "&trade_type=" + TRADE_TYPE_JS +
                "&key=" + KEY;//这个字段是用于之后MD5加密的，字段要按照ascii码顺序排序
        sign = DigestUtils.md5Hex(sign).toUpperCase();

        //组装包含openid用于请求统一下单返回结果的XML
        StringBuilder sb = new StringBuilder("");
        sb.append("<xml>");
        setXmlKV(sb,"appid",appId);
        setXmlKV(sb,"body",body);
        setXmlKV(sb,"mch_id",MCH_ID);
        setXmlKV(sb,"nonce_str",nonce_str);
        setXmlKV(sb,"notify_url",NOTIFY_URL);
        setXmlKV(sb,"openid",openid);
        setXmlKV(sb,"out_trade_no",out_trade_no);
        setXmlKV(sb,"spbill_create_ip",IP);
        setXmlKV(sb,"total_fee",total_fee);
        setXmlKV(sb,"trade_type",TRADE_TYPE_JS);
        setXmlKV(sb,"sign",sign);
        sb.append("</xml>");
        log.info("统一下单请求：" + sb);

        StringEntity reqEntity = new StringEntity(new String (sb.toString()));//这个处理是为了防止传中文的时候出现签名错误
        httppost.setEntity(reqEntity);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(httppost);
        String strResult = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
        log.info("统一下单返回xml：" + strResult);

        return strResult;
    }

    private static String getKey(String appId) {
        String key = null;
        if (appId != null){
            if ("wx2b0cbda9e963ea89".equals(appId)){
                key = "YYnIdIUUU5619GW8jVVm4vV4dvijhCM1";
            }else if ("wxbb6b8383a755c755".equals(appId)){
                key = "081NOeXO1sGCba13cmXO1xPWWO1NOeXu";
            }
        }else {
            log.warn("key获取失败，appId[{}],key[{}]",appId,key);
        }

        return key;
    }

    /**
     * XML-->MAP 根据统一下单返回预支付订单的id和其他信息生成签名并拼装为map（调用微信支付）
     * @param prePayInfoXml
     * @return
     */
    public static Map<String,Object> getPayMap(String prePayInfoXml,String appId){
        Map<String,Object> map = new HashMap<String,Object>();

        String key = getKey(appId);

        String prepay_id = getXmlPara(prePayInfoXml,"prepay_id");//统一下单返回xml中prepay_id
        String timeStamp = String.valueOf(System.currentTimeMillis()/1000);//1970年到现在的秒数
        String nonceStr = getNonceStr().toUpperCase();//随机数据字符串
        String packageStr = "prepay_id=" + prepay_id;
        String signType = "MD5";
        String paySign =
                "appId=" + appId +
                        "&nonceStr=" + nonceStr +
                        "&package=prepay_id=" + prepay_id +
                        "&signType=" + signType +
                        "&timeStamp=" + timeStamp +
                        "&key="+ KEY;//注意这里的参数要根据ASCII码 排序
        paySign = DigestUtils.md5Hex(paySign).toUpperCase();//将数据MD5加密

        map.put("appId",appId);
        map.put("timeStamp",timeStamp);
        map.put("nonceStr",nonceStr);
        map.put("packageStr",packageStr);
        map.put("signType",signType);
        map.put("paySign",paySign);
        map.put("prepay_id",prepay_id);
        return map;
    }

    /**
     * 获取32位随机字符串
     * @return
     */
    public static String getNonceStr(){
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rd = new Random();
        for(int i = 0 ; i < 32 ; i ++ ){
            sb.append(str.charAt(rd.nextInt(str.length())));
        }
        return sb.toString();
    }
    /**
     * 获取6位随机字符串
     * @return
     */
    public static String getNonceStr6(){
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rd = new Random();
        for(int i = 0 ; i < 6 ; i ++ ){
            sb.append(str.charAt(rd.nextInt(str.length())));
        }
        return sb.toString();
    }
    /**
     * 插入XML标签
     * @param sb
     * @param Key
     * @param value
     * @return
     */
    public static StringBuilder setXmlKV(StringBuilder sb,String Key,String value){
        sb.append("<");
        sb.append(Key);
        sb.append(">");

        sb.append(value);

        sb.append("</");
        sb.append(Key);
        sb.append(">");

        return sb;
    }

    /**
     * 解析XML 获得名称为para的参数值
     * @param xml
     * @param para
     * @return
     */
    public static String getXmlPara(String xml,String para){
        int start = xml.indexOf("<"+para+">");
        int end = xml.indexOf("</"+para+">");

        if(start < 0 && end < 0){
            return null;
        }
        return xml.substring(start + ("<"+para+">").length(),end).replace("<![CDATA[","").replace("]]>","");
    }

    /**
     * 获取ip地址
     * @return
     */
    public static String getIpAddr() {
        InetAddress addr = null;
        HttpServletRequest request = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return request.getRemoteAddr();
        }
        byte[] ipAddr = addr.getAddress();
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i] & 0xFF;
        }
        return ipAddrStr;
    }

    @Value("${APPID}")
    public void setAPPID(String APPID) {
        WxPayUtil.APPID = APPID;
    }

    @Value("${MCH_ID}")
    public void setMchId(String mchId) {
        MCH_ID = mchId;
    }

    @Value("${NOTIFY_URL}")
    public void setNotifyUrl(String notifyUrl) {
        NOTIFY_URL = notifyUrl;
    }

    @Value("${TRADE_TYPE_JS}")
    public void setTradeTypeJs(String tradeTypeJs) {
        TRADE_TYPE_JS = tradeTypeJs;
    }

    @Value("${KEY}")
    public void setKEY(String KEY) {
        WxPayUtil.KEY = KEY;
    }

    @Value("${APP_SECRET}")
    public void setAppSecret(String appSecret) {
        WxPayUtil.APP_SECRET = appSecret;
    }

    public static String getKEY() {
        return KEY;
    }
}



