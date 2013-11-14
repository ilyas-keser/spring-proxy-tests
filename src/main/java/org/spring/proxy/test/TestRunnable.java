package org.spring.proxy.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class TestRunnable implements Runnable {

    private final ApplicationContext applicationContext;

    @Autowired
    private TestSingletonScopeClass testSingletonScopeClass;

    public TestRunnable(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
        testSingletonScopeClass.getTestSessionScopeClass().doSomething();
    }

    // Getter / Setter
    public TestSingletonScopeClass getTestSingletonScopeClass() {
        return testSingletonScopeClass;
    }

    public void setTestSingletonScopeClass(TestSingletonScopeClass testSingletonScopeClass) {
        this.testSingletonScopeClass = testSingletonScopeClass;
    }

}
