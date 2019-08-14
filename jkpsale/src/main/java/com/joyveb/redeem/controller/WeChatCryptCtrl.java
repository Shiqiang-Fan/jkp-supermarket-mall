package com.joyveb.redeem.controller;


import com.joyveb.redeem.bean.WeChatCryptReqs;
import com.joyveb.redeem.common.ReturnInfo;
import com.joyveb.redeem.utils.WeChatCryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenjia on 2018/8/7
 */
@Slf4j
@RestController
public class WeChatCryptCtrl {

    @RequestMapping(value = "/crypt", method = RequestMethod.POST)
    @ResponseBody
    public ReturnInfo crypt(@RequestBody WeChatCryptReqs cryptReqs) {

        try {
            log.debug("解密请求参数:{}", cryptReqs);
            String decrypt = WeChatCryptUtil
              .decrypt(cryptReqs.getEncryptedData(), cryptReqs.getIv(), cryptReqs.getSessionKey()).trim();
            log.debug("解密完成，解密信息：{}", decrypt);
            return new ReturnInfo("解密成功", "200", decrypt, true);
        } catch (Exception e) {
            log.warn("小程序用户信息解密异常", e);
            return new ReturnInfo("解密异常","",e, false);
        }

    }

}
