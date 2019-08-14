package com.joyveb.jkpmall.logger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yangqiju
 * @date 2018-03-13 下午10:52
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControllerLog {

    /**
     * 操作内容
     * @return
     */
    String action() default "";

    /**
     * 打印结果
     * @return
     */
    boolean printResult() default false;

}
