package org.spring.proxy.test;

import static org.spring.proxy.test.ThreadScope.THREAD_SCOPE;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

/**
 * Erzeugt ServiceImpl Beans mit einem Scoped Proxy und registriert diese im
 * {@link ApplicationContext}.
 * 
 * @author Olaf Siefart, Senacor Technologies AG
 * 
 */
public class ServiceBeanWithScopedProxyFactory implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ServiceImpl.class);
        builder.setScope(THREAD_SCOPE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, "threadScopedBean");
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
        BeanDefinitionHolder scopedProxy = ScopedProxyUtils.createScopedProxy(definitionHolder, registry, true);
        BeanDefinitionReaderUtils.registerBeanDefinition(scopedProxy, registry);
    }

}
