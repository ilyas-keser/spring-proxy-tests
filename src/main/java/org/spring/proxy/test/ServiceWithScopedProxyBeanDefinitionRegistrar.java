package org.spring.proxy.test;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class ServiceWithScopedProxyBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinition bd = new RootBeanDefinition(ServiceImpl.class);
        bd.setScope(ThreadScope.THREAD_SCOPE);
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(bd, "service");
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
        BeanDefinitionHolder scopedProxy = ScopedProxyUtils.createScopedProxy(definitionHolder, registry, true);
        BeanDefinitionReaderUtils.registerBeanDefinition(scopedProxy, registry);
    }

}
