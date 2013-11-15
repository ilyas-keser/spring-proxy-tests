package org.spring.proxy.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * {@link Runnable}, das den Aufruf des {@link RetrievalService} durchführt.
 * 
 * @author Olaf Siefart, Senacor Technologies AG
 * 
 */
public class CallingServiceRunnable implements Runnable {

    private final ApplicationContext applicationContext;
    private final Long id;

    private ServiceCallResult serviceCallResult;

    @Autowired
    private Service service;

    @Autowired
    private CallerId callerId;

    public CallingServiceRunnable(Long id, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.id = id;
    }

    @Override
    public void run() {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
        callerId.setCallerId(id);
        serviceCallResult = service.doSomething(new Long(-1L));
    }

    // Getter / Setter
    public ServiceCallResult getServiceCallResult() {
        return serviceCallResult;
    }

    public void setServiceCallResult(ServiceCallResult serviceCallResult) {
        this.serviceCallResult = serviceCallResult;
    }

}
