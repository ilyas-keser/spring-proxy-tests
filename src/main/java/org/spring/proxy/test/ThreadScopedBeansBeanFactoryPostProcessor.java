package org.spring.proxy.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class ThreadScopedBeansBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory context = (DefaultListableBeanFactory) beanFactory;
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ThreadScopedBean.class);
        builder.setScope("thread");
        context.registerBeanDefinition("threadScopedBean", builder.getBeanDefinition());
    }

}
