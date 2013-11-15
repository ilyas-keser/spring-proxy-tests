package org.spring.proxy.test;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.ClassUtils;

public class ServiceFactoryBean implements FactoryBean<Service> {

    private Service service;

    @Override
    public Service getObject() throws Exception {
        if (service == null) {
            ProxyFactory proxyFactory = new ProxyFactory(new ServiceImpl());
            service = (Service) proxyFactory.getProxy(ClassUtils.getDefaultClassLoader());
        }
        return service;
    }

    @Override
    public Class<?> getObjectType() {
        return Service.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
