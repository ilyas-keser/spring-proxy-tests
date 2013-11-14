package org.spring.proxy.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class CallingBeanMethodRunnable implements Runnable {

    private final ApplicationContext applicationContext;

    private int doSomething;

    @Autowired
    private SingletonScopedBean singletonScopedBean;

    public CallingBeanMethodRunnable(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
        doSomething = singletonScopedBean.getThreadScopedBean().doSomething();
    }

    // Getter / Setter
    public SingletonScopedBean getSingletonScopedBean() {
        return singletonScopedBean;
    }

    public void setSingletonScopedBean(SingletonScopedBean testSingletonScopeClass) {
        singletonScopedBean = testSingletonScopeClass;
    }

    public int getDoSomething() {
        return doSomething;
    }

    public void setDoSomething(int doSomething) {
        this.doSomething = doSomething;
    }

}
