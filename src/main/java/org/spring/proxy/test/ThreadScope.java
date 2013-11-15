package org.spring.proxy.test;

import org.springframework.context.support.SimpleThreadScope;

/**
 * Eigener ThreadScope, der die Beans in Maps verwaltet, die der Id des Thread
 * zugeordnet sind und die man von außen abfragen kann.
 * 
 * @author Olaf Siefart, Senacor Technologies AG
 * 
 */
public class ThreadScope extends SimpleThreadScope {

    public static final String THREAD_SCOPE = "thread";

}
