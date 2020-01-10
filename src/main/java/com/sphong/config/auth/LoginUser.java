package com.sphong.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Parameter에서만 생성 가능
@Target(ElementType.PARAMETER)
//
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}
