package com.joyveb.redeem.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.joyveb.redeem.common.ReturnInfo;
import com.joyveb.redeem.config.WeChatConfig;
import com.joyveb.redeem.utils.JsonParserUtils;
import com.unlto.twls.commonutil.component.HttpClientComponent;
import com.unlto.twls.commonutil.component.HttpClientComponent.HttpResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Saxon
 * @date 2018/7/19
 */
@Slf4j
@RestController
@RequestMapping("/weixin_core")
public class WeChatCoreCtrl {

    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    private HttpClientComponent httpClientComponent;

    /**
     * 登录凭证校验
     */
    @GetMapping(value = "/get_check_wechat_openid")
    public ReturnInfo getOpenId(String appid, String secret, String jsCode, String grantType) {
        ReturnInfo returnInfo = new ReturnInfo();
        if (StringUtils.isBlank(appid) && StringUtils.isBlank(secret) && StringUtils
                .isBlank(jsCode) && StringUtils.isBlank(grantType)) {
            log.error("登录凭证校验参数异常：appid:[{}],secret:[{}],jsCode:[{}],grantType:[{}]", appid, secret,
                    jsCode, grantType);
            returnInfo.setDescription("参数异常");
            return returnInfo;
        }
        try {
            String url = String.format(weChatConfig.getWebLoginCheckUrl(), appid, secret, jsCode, grantType);
            log.info("授权url:{}", url);
            HttpResponse andResponse = httpClientComponent.getAndResponse(url);
            String responseMsg = andResponse.getResponseMsg();
            log.info("登录凭证校验，接口返回结果：{}, url:{}", responseMsg, url);
            JsonObject ele = JsonParserUtils.parse(responseMsg).getAsJsonObject();
            ReturnInfoBean returnInfoBean = new ReturnInfoBean();
            JsonElement openid = ele.get("openid");
            JsonElement session_key = ele.get("session_key");
            if (null != openid) {
                returnInfoBean.setOpenId(openid.getAsString());
            }
            if (null != session_key) {
                returnInfoBean.setSessionKey(session_key.getAsString());
            }
            log.info("返回结果:[{}]", returnInfoBean);
            returnInfo.setRetObj(returnInfoBean);
            returnInfo.setSuccess(true);
            return returnInfo;
        } catch (IOException e) {
            log.error("登录凭证校验异常", e);
            returnInfo.setDescription("服务器出错了，请联系管理员，我们会及时处理");
            return returnInfo;
        }
    }

    @Data
    private static class ReturnInfoBean {

        private String openId;
        private String sessionKey;
        private String unionId;
    }
}
