package com.holy.jast.annotation;

import java.lang.annotation.*;

/**
 * Created by tiptimes on 16/1/8.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Api {
    String route() default "/";
}
