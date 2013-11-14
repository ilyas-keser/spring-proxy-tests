package org.spring.proxy.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class CallingBeanMethodRunnable implements Runnable {

    private final ApplicationContext applicationContext;

    @Autowired
    private SingletonScopedBean singletonScopedBean;

    public CallingBeanMethodRunnable(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
        singletonScopedBean.getThreadScopedBean().doSomething();
    }

    // Getter / Setter
    public SingletonScopedBean getSingletonScopedBean() {
        return singletonScopedBean;
    }

    public void setSingletonScopedBean(SingletonScopedBean testSingletonScopeClass) {
        this.singletonScopedBean = testSingletonScopeClass;
    }

}
