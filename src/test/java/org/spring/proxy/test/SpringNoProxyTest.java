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
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringNoProxyTest.TestConfiguration.class)
public class SpringNoProxyTest {

    @Autowired
    private SingletonScopedBean testSingletonScopeClass;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThreadScope threadScope;

    @Test
    public void testSharingSameInstanceIfWithoutScopedProxy() {
        assertEquals(1, threadScope.getBeans().size());

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

        assertEquals(1, threadScope.getBeans().size());

    }

    @Configuration
    public static class TestConfiguration {

        @Bean
        public SingletonScopedBean singletonScopedBean() {
            return new SingletonScopedBean();
        }

        @Bean
        @Scope(value = "thread")
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
