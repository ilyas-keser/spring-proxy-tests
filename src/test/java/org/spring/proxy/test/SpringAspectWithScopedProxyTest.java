package org.spring.proxy.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringAspectWithScopedProxyTest.TestConfiguration.class)
public class SpringAspectWithScopedProxyTest {

    @Autowired
    private SingletonScopedBean testSingletonScopeClass;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThreadScope threadScope;

    @Test
    public void testScopedInCombinWithAspect() {
        assertEquals(0, threadScope.getBeans().size());
        testSingletonScopeClass.getThreadScopedBean().doSomething();
        assertEquals(1, threadScope.getBeans().size());

        CallingBeanMethodRunnable runnable = new CallingBeanMethodRunnable(applicationContext);
        Thread thread = new Thread(runnable);
        thread.start();

        do {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (thread.isAlive());

        assertEquals(2, threadScope.getBeans().size());

    }

    @Configuration
    @EnableAspectJAutoProxy
    public static class TestConfiguration {

        @Bean
        public SingletonScopedBean singletonScope() {
            return new SingletonScopedBean();
        }

        @Bean
        @Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
        public ThreadScopedBean sessionScope() {
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

        @Bean
        public NoOpAspect createTestAspect() {
            return new NoOpAspect();
        }

    }

}
