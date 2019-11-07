package com.cullen.admin.annotation;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Target(ElementType.METHOD)//作用于方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    int limit() default 5;

    int timeout() default 1000;
}
