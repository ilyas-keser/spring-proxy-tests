package org.spring.proxy.test;

public abstract class BaseTest {

    protected void startAndWait(Thread thread) {
        thread.start();
        do {
            sleep(10);
        } while (thread.isAlive());
    }

    protected void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
