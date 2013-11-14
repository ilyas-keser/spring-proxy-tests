package org.spring.proxy.test;

import static org.junit.Assert.assertNotSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringAspectTest.TestConfiguration.class)
public class SpringAspectTest {

    @Autowired
    private TestSingletonScopeClass testSingletonScopeClass;

    @Test
    public void testWithAspect() {
        assertNotSame(testSingletonScopeClass.getTestSessionScopeClass().getClass(), TestThreadScopeClass.class);
    }

    @Configuration
    @EnableAspectJAutoProxy
    public static class TestConfiguration {

        @Bean
        public TestSingletonScopeClass singletonScope() {
            return new TestSingletonScopeClass();
        }

        @Bean
        @Scope(value = "thread")
        public TestThreadScopeClass sessionScope() {
            return new TestThreadScopeClass();
        }

        @Bean
        public static CustomScopeConfigurer createSessionScope() {
            Map<String, Object> scopes = new HashMap<String, Object>();
            scopes.put("thread", new SimpleThreadScope());
            CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
            customScopeConfigurer.setScopes(scopes);
            return customScopeConfigurer;
        }

        @Bean
        public TestAspect createTestAspect() {
            return new TestAspect();
        }

    }

}
