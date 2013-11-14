package org.spring.proxy.test;

/**
 * Kapselt das Ergebnis eines Service-Aufrufs. Enthält sowohl die Id des
 * Aufrufers, als auch einen Zähler, zum wie oft die Service-Methode nun
 * durchlaufen wurde.
 * 
 * @author Olaf Siefart, Senacor Technologies AG
 * 
 */
public class ServiceCallResult {

    private final long callerId;
    private final int count;

    public ServiceCallResult(long callerId, int count) {
        this.callerId = callerId;
        this.count = count;
    }

    public long getCallerId() {
        return callerId;
    }

    public int getCount() {
        return count;
    }

}