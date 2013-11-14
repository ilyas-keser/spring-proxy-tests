package org.spring.proxy.test;

import org.springframework.beans.factory.annotation.Autowired;

public class TestSingletonScopeClass {

    @Autowired
    private TestThreadScopeClass testSessionScopeClass;

    // Getter / Setter
    public TestThreadScopeClass getTestSessionScopeClass() {
        return testSessionScopeClass;
    }

    public void setTestSessionScopeClass(TestThreadScopeClass testSessionScopeClass) {
        this.testSessionScopeClass = testSessionScopeClass;
    }

}