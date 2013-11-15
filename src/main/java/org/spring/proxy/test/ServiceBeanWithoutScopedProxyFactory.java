package org.spring.proxy.test;

import static org.spring.proxy.test.ThreadScope.THREAD_SCOPE;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * Erzeugt ServiceImpl Beans mit einem Scoped Proxy und registriert diese im
 * {@link ApplicationContext}.
 * 
 * @author Olaf Siefart, Senacor Technologies AG
 * 
 */
public class ServiceBeanWithoutScopedProxyFactory implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory context = (DefaultListableBeanFactory) beanFactory;
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ServiceImpl.class);
        builder.setScope(THREAD_SCOPE);
        context.registerBeanDefinition("threadScopedBean", builder.getBeanDefinition());
    }

}
