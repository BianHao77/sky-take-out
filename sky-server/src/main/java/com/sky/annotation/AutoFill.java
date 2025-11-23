package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**自定义注解，用于公共字段的填充
 *
 */

@Target(ElementType.METHOD) // 指定注解只能用在方法上
@Retention(RetentionPolicy.RUNTIME) // 指定注解在运行时保留
public @interface AutoFill {

    //  设置数据库操作类型 UPDATE INSERT
    OperationType value();


}
