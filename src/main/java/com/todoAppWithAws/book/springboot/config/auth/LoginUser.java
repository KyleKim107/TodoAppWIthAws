package com.todoAppWithAws.book.springboot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 @Target - 이 어노테이션을 사용할수 있는 위치를 지정합니다
  파라메터로 지정했으니 모소드의 파라미터로 선언된 객체에서만 사용할 수 있습니다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
    /*
     @interface 이 파일을 annotation클래스로 지정한다.
     */
}
