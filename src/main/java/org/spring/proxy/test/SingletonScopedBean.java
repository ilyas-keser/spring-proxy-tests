package org.spring.proxy.test;

import org.springframework.beans.factory.annotation.Autowired;

public class SingletonScopedBean {

    @Autowired
    private ThreadScopedBean threadScopedBean;

    // Getter / Setter
    public ThreadScopedBean getThreadScopedBean() {
        return threadScopedBean;
    }

    public void setThreadScopedBean(ThreadScopedBean testSessionScopeClass) {
        this.threadScopedBean = testSessionScopeClass;
    }

}