package org.spring.proxy.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.ObjectFactory;

/**
 * Eigener ThreadScope, der die Beans in Maps verwaltet, die der Id des Thread
 * zugeordnet sind und die man von außen abfragen kann.
 * 
 * @author Olaf Siefart, Senacor Technologies AG
 * 
 */
public class ThreadScope implements org.springframework.beans.factory.config.Scope {

    public static final String THREAD_SCOPE = "thread";

    private final Map<Long, Map<String, Object>> beans = new ConcurrentHashMap<Long, Map<String, Object>>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        long threadId = Thread.currentThread().getId();

        Map<String, Object> scopedBeans = beans.get(threadId);
        if (scopedBeans == null) {
            scopedBeans = new ConcurrentHashMap<String, Object>();
            beans.put(threadId, scopedBeans);
        }

        Object object = scopedBeans.get(name);
        if (object == null) {
            object = objectFactory.getObject();
            scopedBeans.put(name, object);
        }

        return object;
    }

    @Override
    public Object remove(String name) {
        return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }

    public Map<Long, Map<String, Object>> getBeans() {
        return beans;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getBeansForType(Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : beans.values()) {
            for (Object object : map.values()) {
                if (clazz.isInstance(object)) {
                    result.add((T) object);
                }
            }
        }

        return result;
    }

}
