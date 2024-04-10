package com.hoon.api.utils.aop;

import com.hoon.api.utils.aop.annotation.MethodInfoLogging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * AOP 작성 클래스
 *
 * @since 1.0 2023-02-24
 */
@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class LogAspect {

    /**
     * 메소드 작업 진행 경과 시간을 log에 출력해준다.
     * <pre>
     * {@code
     * Ex)
     * @MethodInfoLogging(메소드 설명) // 어노테이션 입력 하면 적용 됨
     * public Method(  ) {...}
     * }
     * </pre>
     *
     * @param pjp ProceedingJoinPoint
     * @return Object retVal = pjp.proceed();
     * @throws Throwable AOP 메소드에서 익셉션 발생 시 대비
     * @since v1.0 2023/02/24
     */
    @Around("@annotation(com.hoon.api.utils.aop.annotation.MethodInfoLogging) && @annotation(target)")
    public Object workTime(ProceedingJoinPoint pjp, MethodInfoLogging target) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String methodPath = pjp.getTarget().getClass().getName();
        String className = methodPath.substring(methodPath.lastIndexOf(".") + 1);
        String methodName = signature.getMethod().getName();
        String classMethod = className + "." + methodName;

        log.info("");
        log.info("START {}()", classMethod);
        log.info("Method Path : {}", methodPath);
        log.info("Description : {}", target.description());

        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기

        Object retVal = pjp.proceed();

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기

        log.info("Execute Time : {}ms", afterTime - beforeTime);
        log.info("END {}()\n", classMethod);

        return retVal;
    }

    /**
     * Repository 호출시 해당 메소드 정보를 log에 출력해준다
     */
    @Around("execution( * com.hoon.api.domain.*.*Repository.*(..))")
    public Object workingRepoName(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String methodPath = pjp.getTarget().getClass().getName();
        String className = methodPath.substring(methodPath.lastIndexOf(".") + 1);
        String methodName = signature.getMethod().getName();
        String classMethod = className + "." + methodName;

        log.info("SQL : {}() ", classMethod);
        Object proceed = pjp.proceed();

        return proceed;
    }




}
