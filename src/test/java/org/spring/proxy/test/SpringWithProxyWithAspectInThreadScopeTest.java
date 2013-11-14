package org.spring.proxy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringWithProxyWithAspectInThreadScopeTest.TestConfiguration.class)
public class SpringWithProxyWithAspectInThreadScopeTest {

    @Autowired
    private SingletonScopedBean testSingletonScopeClass;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThreadScope threadScope;

    @Test
    public void testSharingSameInstanceIfWithoutScopedProxy() {
        assertEquals(0, threadScope.getBeans().size());

        CallingBeanMethodRunnable runnable1 = new CallingBeanMethodRunnable(applicationContext);
        Thread thread1 = new Thread(runnable1);
        thread1.start();

        CallingBeanMethodRunnable runnable2 = new CallingBeanMethodRunnable(applicationContext);
        Thread thread2 = new Thread(runnable2);
        thread2.start();

        do {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (thread1.isAlive() || thread2.isAlive());

        assertEquals(2, threadScope.getBeans().size());
        assertEquals(2, threadScope.getBeans().size());
        assertEquals(2, threadScope.getBeans().size());

        assertNotSame(runnable1.getDoSomething(), runnable2.getDoSomething());

    }

    @Configuration
    @EnableAspectJAutoProxy
    public static class TestConfiguration {

        @Bean
        @Scope(value = "thread")
        public ThreadIdMethodInterceptor createTestAdvice() {
            return new ThreadIdMethodInterceptor();
        }

        @Bean
        public SingletonScopedBean singletonScopedBean() {
            return new SingletonScopedBean();
        }

        @Bean
        @Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
        @Primary
        public ThreadScopedBean threadScopedProyBean() {
            ThreadScopedBean threadScopedBean = threadScopedBean();

            Class<?>[] classes = {};
            AdvisedSupport config = new AdvisedSupport(classes);
            config.setProxyTargetClass(true);
            config.setTargetClass(ThreadScopedBean.class);
            try {
                config.setTargetSource(((Advised) threadScopedBean).getTargetSource());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ThreadIdMethodInterceptor createTestAdvice = createTestAdvice();

            config.addAdvice(createTestAdvice);
            DefaultAopProxyFactory factory = new DefaultAopProxyFactory();
            ThreadScopedBean newB = (ThreadScopedBean) factory.createAopProxy(config).getProxy();
            return newB;
        }

        @Bean
        public ThreadScopedBean threadScopedBean() {
            return new ThreadScopedBean();
        }

        @Bean
        public CustomScopeConfigurer createSessionScope() {
            Map<String, Object> scopes = new HashMap<String, Object>();
            scopes.put("thread", createTestScope());
            CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
            customScopeConfigurer.setScopes(scopes);
            return customScopeConfigurer;
        }

        @Bean
        public ThreadScope createTestScope() {
            return new ThreadScope();
        }

    }

}
