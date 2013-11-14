package org.spring.proxy.test;

import static org.junit.Assert.assertEquals;
import static org.spring.proxy.test.ThreadScope.THREAD_SCOPE;

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

/**
 * In diesem Testfall liegt der Service mit seinem Aspekt im Singleton Scope.
 * Dafür erfolgt der Zugriff auf die CallerId im {@link ThreadScope} und über
 * einen ScopedProxy
 * 
 * Klassischer Spring-Weg ohne Fehler.
 * 
 * @author Olaf Siefart, Senacor Technologies AG
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AspectMixedRightScenarioTest.TestConfiguration.class)
public class AspectMixedRightScenarioTest extends BaseTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testServiceCall() {

        CallingRetrievalServiceRunnable runnable1 = new CallingRetrievalServiceRunnable(1L, applicationContext);
        startAndWait(new Thread(runnable1));

        CallingRetrievalServiceRunnable runnable2 = new CallingRetrievalServiceRunnable(2L, applicationContext);
        startAndWait(new Thread(runnable2));

        // Prüfen, das die Service-Results unterschiedliche CallerIds enthalten
        assertEquals(1L, runnable1.getServiceCallResult().getCallerId());
        assertEquals(2L, runnable2.getServiceCallResult().getCallerId());

        // Die eine Service-Instanz muss zweimmal aufgerufen worden sein
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
            return new Service();
        }

        @Bean
        public CustomScopeConfigurer threadScopeConfigurer() {
            Map<String, Object> scopes = new HashMap<String, Object>();
            scopes.put(THREAD_SCOPE, threadScope());
            CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
            customScopeConfigurer.setScopes(scopes);
            return customScopeConfigurer;
        }

        @Bean
        public ThreadScope threadScope() {
            return new ThreadScope();
        }

        @Bean
        public CallerIdAspect callerIdAspect() {
            return new CallerIdAspect();
        }

        @Bean
        @Scope(value = THREAD_SCOPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
        public CallerId callerId() {
            return new CallerId();
        }

    }

}
