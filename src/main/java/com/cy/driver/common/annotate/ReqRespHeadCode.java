package com.cy.driver.common.annotate;

import com.cy.driver.common.enumer.ApiReqCodeEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求头的注解
 * Created by wyh on 2015/5/7.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReqRespHeadCode {
    ApiReqCodeEnum reqHeadCode() default ApiReqCodeEnum.DEFAULT;
}
