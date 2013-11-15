package org.spring.proxy.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Aspect, der die Id des aufrufenden {@link Runnable} als Parameter an der
 * ServiceImpl überträgt.
 * 
 * @author Olaf Siefart, Senacor Technologies AG
 * 
 */
@Aspect
public class CallerIdAspect {

    @Autowired
    private CallerId callerId;

    @Around("execution(* org.spring.proxy.test.Service.*(..))")
    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        return point.proceed(new Object[] { new Long[] { callerId.getCallerId() } });
    }

}