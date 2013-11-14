package org.spring.proxy.test;

/**
 * Kapselt die CallerId des Aufrufer. Muss vom Aufrufer gesetzt werden.
 * 
 * @author Olaf Siefart, Senacor Technologies AG
 * 
 */
public class CallerId {

    private Long callerId;

    // Getter / Setter
    public Long getCallerId() {
        return callerId;
    }

    public void setCallerId(Long callerId) {
        this.callerId = callerId;
    }

}