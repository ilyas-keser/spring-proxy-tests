package org.spring.proxy.test;

import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ThreadIdAspect {

    private static final AtomicInteger BLUB = new AtomicInteger(0);

    private final int foo = BLUB.incrementAndGet();

    @Around("execution(* org.spring.proxy.test.ThreadScopedBean.*(..))")
    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        return foo;
    }

}