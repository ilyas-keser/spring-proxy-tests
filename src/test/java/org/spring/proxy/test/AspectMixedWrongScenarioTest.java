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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * In diesem Testfall liegt der Service selber im Singleton Scope und der Aspekt
 * im {@link ThreadScope} und ist nicht über einen ScopedProxy geschützt.
 * 
 * Fehlerszenario mit Aufrufen unter der falschen {@link CallerId}.
 * 
 * 
 * @author Olaf Siefart, Senacor Technologies AG
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AspectMixedWrongScenarioTest.TestConfiguration.class)
public class AspectMixedWrongScenarioTest extends BaseTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DirtiesContext
    public void testRetrievalServiceCall() {

        CallingRetrievalServiceRunnable runnable1 = new CallingRetrievalServiceRunnable(1L, applicationContext);
        startAndWait(new Thread(runnable1));

        CallingRetrievalServiceRunnable runnable2 = new CallingRetrievalServiceRunnable(2L, applicationContext);
        startAndWait(new Thread(runnable2));

        // Nachweisen, das die Service-Results die gleiche CallerId enthalten
        assertEquals(1L, runnable1.getServiceCallResult().getCallerId());
        assertEquals(1L, runnable2.getServiceCallResult().getCallerId());

        // Nachweisen, das der Service zwei mal aufgerufen wurde
        assertEquals(1L, runnable1.getServiceCallResult().getCount());
        assertEquals(2L, runnable2.getServiceCallResult().getCount());

    }

    @Test
    @DirtiesContext
    public void testServiceCall() {

        CallingServiceRunnable runnable1 = new CallingServiceRunnable(1L, applicationContext);
        startAndWait(new Thread(runnable1));

        CallingServiceRunnable runnable2 = new CallingServiceRunnable(2L, applicationContext);
        startAndWait(new Thread(runnable2));

        // Nachweisen, das die Service-Results die gleiche CallerId enthalten
        assertEquals(1L, runnable1.getServiceCallResult().getCallerId());
        assertEquals(1L, runnable2.getServiceCallResult().getCallerId());

        // Nachweisen, das der Service zwei mal aufgerufen wurde
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
        @Scope(value = THREAD_SCOPE)
        public CallerIdAspect callerIdAspect() {
            return new CallerIdAspect();
        }

        @Bean
        @Scope(value = THREAD_SCOPE)
        public CallerId callerId() {
            return new CallerId();
        }

    }

}
