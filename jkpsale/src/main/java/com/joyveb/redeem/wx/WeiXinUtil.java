package com.joyveb.redeem.wx;

import com.google.gson.Gson;

public class WeiXinUtil {
    /**
     * 获取jsapi_ticket
     * @return
     */
    public static JsapiTicket getjsapiTicket() {
        JsapiTicket jsapiTicket = null ;
        String accessTokenJson = NetUtils.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx2b0cbda9e963ea89&secret=546fe9bfd252927c29896f878dac74bc");
        AccrssBean accrssBean = new Gson().fromJson(accessTokenJson,AccrssBean.class);

        String ticketJson = NetUtils.get(String.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi",accrssBean.getAccess_token()));
        TicketBean ticketBean = new Gson().fromJson(ticketJson,TicketBean.class);

        System.out.println(accessTokenJson);
        System.out.println(ticketJson);

        // 如果请求成功
        if (null != ticketBean) {
            try {
                jsapiTicket = new JsapiTicket();
                jsapiTicket.setJsapiTicket(ticketBean.getTicket());
                jsapiTicket.setExpiresIn(ticketBean.getExpires_in());
            } catch (Exception e) {
                jsapiTicket = null;
                // 获取token失败
                System.out.println("获取ticket失败 errcode:{} errmsg:{}"+ ticketBean.getErrcode()+ ticketBean.getErrmsg());
            }
        }else {

        }
        return jsapiTicket;
    }

}
