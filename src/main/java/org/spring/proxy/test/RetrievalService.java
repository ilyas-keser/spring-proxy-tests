package org.spring.proxy.test;

import org.springframework.beans.factory.annotation.Autowired;

public class RetrievalService {

    @Autowired
    private Service service;

    public ServiceCallResult doSomething(Long... callerId) {
        return service.doSomething(callerId);
    }

    // Getter / Setter
    public Service getService() {
        return service;
    }

    public void setService(Service testSessionScopeClass) {
        service = testSessionScopeClass;
    }

}