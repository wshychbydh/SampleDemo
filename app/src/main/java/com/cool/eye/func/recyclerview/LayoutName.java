package com.cool.eye.func.recyclerview;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cool on 16/4/18. kotlin在设置注解时必须是编译时常量，而layout值此时还没生成，介于此可考虑使用@LayoutName+常量名来设置。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutName {

  String value();
}
