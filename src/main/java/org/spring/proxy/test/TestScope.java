package org.spring.proxy.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;

public class TestScope implements org.springframework.beans.factory.config.Scope {

    private final Map<Long, Map<String, Object>> beans = new HashMap<Long, Map<String, Object>>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        long threadId = Thread.currentThread().getId();

        Map<String, Object> scopedBeans = beans.get(threadId);
        if (scopedBeans == null) {
            scopedBeans = new HashMap<String, Object>();
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

}
