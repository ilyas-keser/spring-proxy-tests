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
    private TestSingletonScopeClass testSingletonScopeClass;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TestScope testScope;

    @Test
    public void testScopedInCombinWithAspect() {
        assertEquals(0, testScope.getBeans().size());
        testSingletonScopeClass.getTestSessionScopeClass().doSomething();
        assertEquals(1, testScope.getBeans().size());

        TestRunnable runnable = new TestRunnable(applicationContext);
        Thread thread = new Thread(runnable);
        thread.start();

        do {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (thread.isAlive());

        assertEquals(2, testScope.getBeans().size());

    }

    @Configuration
    @EnableAspectJAutoProxy
    public static class TestConfiguration {

        @Bean
        public TestSingletonScopeClass singletonScope() {
            return new TestSingletonScopeClass();
        }

        @Bean
        @Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
        public TestThreadScopeClass sessionScope() {
            return new TestThreadScopeClass();
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
        public TestScope createTestScope() {
            return new TestScope();
        }

        @Bean
        public TestAspect createTestAspect() {
            return new TestAspect();
        }

    }

}
