package org.spring.proxy.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class NoOpAspect {

    @Around("execution(* org.spring.proxy.test.ThreadScopedBean.*(..))")
    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        return point.proceed();
    }

}