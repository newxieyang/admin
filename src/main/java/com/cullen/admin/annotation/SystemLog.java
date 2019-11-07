package com.cullen.admin.annotation;



import com.cullen.admin.enums.LogType;

import java.lang.annotation.*;

/**
 * 系统日志自定义注解
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})//作用于参数或方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

    /**
     * 日志名称
     *
     * @return
     */
    String description() default "";

    /**
     * 日志类型
     *
     * @return
     */
    LogType type() default LogType.OPERATION;
}
