package org.spring.proxy.test;

import java.util.concurrent.atomic.AtomicInteger;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ThreadIdMethodInterceptor implements MethodInterceptor {

    private static final AtomicInteger BLUB = new AtomicInteger(0);

    private final int foo = BLUB.incrementAndGet();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return foo;
    }

}