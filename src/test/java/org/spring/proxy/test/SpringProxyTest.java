package org.spring.proxy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringProxyTest.TestConfiguration.class)
public class SpringProxyTest {

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

        // Prüfen, das auf unterschiedlichen Instanzen gearbeitet wird
        assertEquals(2, threadScope.getBeans().size());

        Iterator<Map<String, Object>> iterator = threadScope.getBeans().values().iterator();
        Map<String, Object> beans1 = iterator.next();
        assertEquals(1, beans1.size());
        Map<String, Object> beans2 = iterator.next();
        assertEquals(1, beans2.size());

        assertNotSame(beans1.values().iterator().next(), beans2.values().iterator().next());

    }

    @Configuration
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

    }
}
