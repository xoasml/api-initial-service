package com.hoon.api.utils.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MethodInfoLogging 어노테이션
 * 메소드 작업 진행 경과 시간을 log에 출력해준다.
 * <pre>
 *      {@code
 *      Ex)
 *      @MethodInfoLogging(description = "메소드설명") // 어노테이션 입력 하면 적용 됨
 *      public Method() {...}
 *      }
 * </pre>
 * @since v1.0 2023/02/25
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodInfoLogging {
    String description() default "";
}
