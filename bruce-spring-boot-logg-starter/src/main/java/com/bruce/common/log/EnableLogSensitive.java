package com.bruce.common.log;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableLogSensitive
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/28
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(EnableLogSensitiveImportSelector.class)
public @interface EnableLogSensitive {

    String keys() default "phone";

}
