package org.spring.proxy.test;

import java.util.concurrent.atomic.AtomicInteger;

public class Service {

    private final AtomicInteger count = new AtomicInteger(0);

    public ServiceCallResult doSomething(Long... callerId) {
        Long id = new Long(-2L);
        if ((callerId != null) && (callerId.length > 0)) {
            id = callerId[0];
        }
        return new ServiceCallResult(id, count.incrementAndGet());
    }

    // Getter / Setter

    public int getCount() {
        return count.intValue();
    }

}
