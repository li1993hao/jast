package com.holy.jast.annotation;

import com.holy.jast.http.RequestMethod;

import java.lang.annotation.*;

/**
 * Created by tiptimes on 16/1/8.
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {

    String[] route() default {};
    RequestMethod[] method() default {};
}
