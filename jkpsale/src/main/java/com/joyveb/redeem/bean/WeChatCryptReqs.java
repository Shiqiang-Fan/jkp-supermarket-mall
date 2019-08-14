package com.joyveb.redeem.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author chenjia on 2018/8/7
 */
@Data
public class WeChatCryptReqs {

    /**
     * 加密向量
     */
    @NotNull
    private String iv;
    /**
     * 微信key
     */
    @NotNull
    private String sessionKey;
    /**
     * 解密密文
     */
    @NotNull
    private String encryptedData;


}
