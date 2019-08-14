package com.joyveb.redeem.wx;

import com.joyveb.redeem.utils.WxPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@PropertySource(value = {"classpath:pay.properties"})
public class WXJsapiticket {

    private static String APPID;

    /**
     * 微信jsapi验签
     * @param url
     * @return
     */
    public static Map<String, String> jsApiSign(String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = CheckUtil.create_nonce_str();
        String timestamp = CheckUtil.create_timestamp();
        String jsapi_ticket = getJsApiTicket();
        String string1 = CheckUtil.getString1(nonce_str,timestamp,jsapi_ticket,url);
        String signature = CheckUtil.getSha1(string1);
        ret.put("appid", APPID);//取你自己的公众号appid
        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        log.info("jsApiSign------url=" + url + "-----jsapi_ticket=" + jsapi_ticket + "--------nonceStr=" + nonce_str + "--------timestamp=" + timestamp + "-------signature=" + signature);
        return ret;
    }
    public static String getJsApiTicket(){
        Map<String,Object> map = JsApiTicketCache.getInstance().getJsApiTicketAndExpiresIn();
        return (String) map.get("jsapi_ticket");
    }

    @Value("${APPID}")
    public void setAPPID(String APPID) {
        WXJsapiticket.APPID = APPID;
    }

}
