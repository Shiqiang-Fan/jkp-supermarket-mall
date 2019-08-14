package com.joyveb.jkpmall.logger;

import lombok.Data;

/**
 * @author yangqiju
 * @date 2018-03-13 下午7:41
 */
@Data
public class OperateLogBean {
    private Long operateTime;
    private String className;
    private String methodName;
    private Object args;
    private String action;
    private String username;
    private String clientIp;
    private Object result;
    private String exceptionMsg;
    private Long costTime;
    private Object requestMapping;

}
