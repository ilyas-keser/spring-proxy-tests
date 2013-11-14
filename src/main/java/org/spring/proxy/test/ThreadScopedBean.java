package org.spring.proxy.test;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadScopedBean {

    private final AtomicInteger count = new AtomicInteger(0);

    public int doSomething() {
        return count.incrementAndGet();
    }

    // Getter / Setter

    public AtomicInteger getCount() {
        return count;
    }

}
