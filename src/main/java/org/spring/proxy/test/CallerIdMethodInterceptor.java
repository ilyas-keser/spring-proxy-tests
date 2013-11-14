package org.spring.proxy.test;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@link MethodInterceptor}, der die Id des aufrufenden {@link Runnable} als
 * Parameter an der Service überträgt.
 * 
 * @author Olaf Siefart, Senacor Technologies AG
 * 
 */
@Aspect
public class CallerIdMethodInterceptor implements MethodInterceptor {

    @Autowired
    private CallerId callerId;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        invocation.getArguments()[0] = new Long[] { callerId.getCallerId() };
        return invocation.proceed();
    }

}