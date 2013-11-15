package org.spring.proxy.test;

import static org.junit.Assert.assertEquals;
import static org.spring.proxy.test.ThreadScope.THREAD_SCOPE;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import org.springframework.aop.target.SingletonTargetSource;
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
@ContextConfiguration(classes = AspectMixedRightScenarioWithAdditionalProxyTest.TestConfiguration.class)
public class AspectMixedRightScenarioWithAdditionalProxyTest extends BaseTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testServiceCall() {

        CallingRetrievalServiceRunnable runnable1 = new CallingRetrievalServiceRunnable(1L, applicationContext);
        startAndWait(new Thread(runnable1));

        CallingRetrievalServiceRunnable runnable2 = new CallingRetrievalServiceRunnable(2L, applicationContext);
        startAndWait(new Thread(runnable2));

        // Prüfen, das die ServiceImpl-Results unterschiedliche CallerIds enthalten
        assertEquals(1L, runnable1.getServiceCallResult().getCallerId());
        assertEquals(2L, runnable2.getServiceCallResult().getCallerId());

        // Die eine ServiceImpl-Instanz muss zweimmal aufgerufen worden sein
        assertEquals(1L, runnable1.getServiceCallResult().getCount());
        assertEquals(2L, runnable2.getServiceCallResult().getCount());

    }

    @Configuration
    @EnableAspectJAutoProxy
    public static class TestConfiguration {

        @Bean
        public RetrievalService retrievalService() {
            return new RetrievalService();
        }

        @Bean
        public Service service() {
            return new ServiceImpl();
        }

        @Bean
        public CustomScopeConfigurer threadScopeConfigurer() {
            Map<String, Object> scopes = new HashMap<String, Object>();
            scopes.put(THREAD_SCOPE, new ThreadScope());
            CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
            customScopeConfigurer.setScopes(scopes);
            return customScopeConfigurer;
        }

        @Bean
        @Scope(value = THREAD_SCOPE)
        public CallerIdMethodInterceptor callerIdMethodInterceptor() {
            return new CallerIdMethodInterceptor();
        }

        @Bean
        @Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
        @Primary
        public Service threadScopedProyBean() {
            Service service = service();
            Class<?>[] classes = {};
            AdvisedSupport config = new AdvisedSupport(classes);
            config.setProxyTargetClass(true);
            config.setTargetClass(ServiceImpl.class);
            config.setTargetSource(new SingletonTargetSource(service));
            CallerIdMethodInterceptor callerIdMethodInterceptor = callerIdMethodInterceptor();
            config.addAdvice(callerIdMethodInterceptor);
            DefaultAopProxyFactory factory = new DefaultAopProxyFactory();
            Service serviceProxy = (Service) factory.createAopProxy(config).getProxy();
            return serviceProxy;
        }

        @Bean
        @Scope(value = THREAD_SCOPE)
        public CallerId callerId() {
            return new CallerId();
        }

    }

}
