package com.jxm.yiti.annotation;

import com.jxm.yiti.enums.WxUserConst;

import java.lang.annotation.*;

/**
 * 用户访问权限限制
 */
@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
    // 标识: 默认只有 "普通用户" 可以访问
    WxUserConst[] type() default WxUserConst.NORMAL_USER;
}
