package org.spring.proxy.test;

public interface Service {

    ServiceCallResult doSomething(Long... callerId);

    int getCount();

}