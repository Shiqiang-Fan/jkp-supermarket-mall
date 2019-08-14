package com.joyveb.jkpmall.logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartRequest;

/**
 * @author yangqiju
 * @date 2018-03-13 下午8:21
 */
@Slf4j
@Aspect
public class LoggingAspect {

    private static final String DEFAULT_SESSION_USERNAME_KEY = "username";

    @Pointcut("@annotation(com.joyveb.jkpmall.logger.ControllerLog)")
    public void controllerAspect() {
    }


    @SuppressWarnings("all")
    @Around("controllerAspect()")
    public Object controllerLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        OperateLogBean loggerBean = new OperateLogBean();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();

        ControllerLog controllerLog = method.getAnnotation(ControllerLog.class);
        boolean printResult = controllerLog.printResult();

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            Map<String, Object> requestMapInfos = this.getRequestMapInfos(requestMapping);
            loggerBean.setRequestMapping(requestMapInfos);
        }

        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();
        Map<String, Object> argsMap = new HashMap<>();
        if (args.length == parameterNames.length) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    continue;
                }
                if (this.springInject(args[i].getClass())) {
                    continue;
                }
                argsMap.put(parameterNames[i], args[i]);
            }
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute(DEFAULT_SESSION_USERNAME_KEY);
        String clientIp = request.getRemoteAddr();

        loggerBean.setClassName(className);
        loggerBean.setMethodName(methodName);
        loggerBean.setArgs(argsMap);
        loggerBean.setAction(controllerLog.action());
        loggerBean.setUsername(user);
        loggerBean.setClientIp(clientIp);

        Object proceed = null;
        long startTime = System.currentTimeMillis();
        boolean processError = false;
        try {
            proceed = joinPoint.proceed();

            if (printResult) {
                loggerBean.setResult(proceed);
            }
        } catch (Throwable throwable) {
            loggerBean.setExceptionMsg(this.getExceptionMsg(throwable));
            processError = true;
            throw throwable;
        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            loggerBean.setCostTime(costTime);

            if (processError) {
                log.warn(Markers.appendFields(loggerBean), "controller agent logger");
            } else {
                log.info(Markers.appendFields(loggerBean), "controller agent logger");
            }
        }
        return proceed;
    }


    private Map<String, Object> putIfHasValues(Map<String, Object> map, String key, Object[] objs) {
        if (objs != null && objs.length > 0) {
            map.put(key, objs);
        }
        return map;
    }

    private Map<String, Object> getRequestMapInfos(RequestMapping requestMapping) {
        Map<String, Object> requestMappingMap = new HashMap<>(6);
        requestMappingMap = this
            .putIfHasValues(requestMappingMap, "value", requestMapping.value());
        requestMappingMap = this
            .putIfHasValues(requestMappingMap, "method", requestMapping.method());
        requestMappingMap = this
            .putIfHasValues(requestMappingMap, "consumes", requestMapping.consumes());
        requestMappingMap = this
            .putIfHasValues(requestMappingMap, "headers", requestMapping.headers());
        requestMappingMap = this
            .putIfHasValues(requestMappingMap, "params", requestMapping.params());
        requestMappingMap = this
            .putIfHasValues(requestMappingMap, "produces", requestMapping.produces());
        return requestMappingMap;
    }

    public String getExceptionMsg(Throwable throwable) {
        Throwable cause = throwable.getCause();
        String exceptionMsg = null;
        if (cause == null) {
            exceptionMsg = throwable.getMessage();
        } else {
            exceptionMsg = cause.getMessage();
        }
        return exceptionMsg;
    }

    public boolean springInject(Class<?> paramType) {
        return WebRequest.class.isAssignableFrom(paramType)
            || ServletRequest.class.isAssignableFrom(paramType)
            || MultipartRequest.class.isAssignableFrom(paramType)
            || HttpSession.class.isAssignableFrom(paramType)
            || Principal.class.isAssignableFrom(paramType)
            || Locale.class.equals(paramType)
            || InputStream.class.isAssignableFrom(paramType)
            || Reader.class.isAssignableFrom(paramType)
            || ServletResponse.class.isAssignableFrom(paramType)
            || OutputStream.class.isAssignableFrom(paramType)
            || Writer.class.isAssignableFrom(paramType);
    }

}
