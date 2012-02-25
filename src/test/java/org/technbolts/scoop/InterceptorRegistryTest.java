package org.technbolts.scoop;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Currency;
import java.util.Set;

import org.hamcrest.Matcher;
import org.technbolts.scoop.data.annotation.Constraint;
import org.technbolts.scoop.data.annotation.Profiling;
import org.technbolts.scoop.data.annotation.Tx;
import org.technbolts.scoop.util.New;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InterceptorRegistryTest {

    private InterceptorRegistry interceptorRegistry;
    private Interceptor interceptor1;
    private Interceptor interceptor2;
    private Interceptor interceptor3;
    private Method methodWithdraw;
    private Set<Interceptor> collected;
    
    @BeforeMethod
    public void setUp () throws SecurityException, NoSuchMethodException {
        interceptorRegistry = new InterceptorRegistry ();
        interceptor1 = mock(Interceptor.class);
        interceptor2 = mock(Interceptor.class);
        interceptor3 = mock(Interceptor.class);
        methodWithdraw = PlaygroundModel.class.getDeclaredMethod("withdraw", int.class, Currency.class);
        collected = New.hashSet();
    }
    
    @Test
    public void annotationInterceptor_none() {
        interceptorRegistry.registerAnnotationInterceptor(Profiling.class, interceptor1);
        
        interceptorRegistry.collectInterceptors(methodWithdraw, collected);
        assertThat(collected, hasSize(0));
        assertThat(collected, isEmpty(Interceptor.class));
    }

    @Test
    public void annotationInterceptor_sameInterceptorOnMultipleAnnotation() {
        interceptorRegistry.registerAnnotationInterceptor(Constraint.class, interceptor1);
        interceptorRegistry.registerAnnotationInterceptor(Tx.class, interceptor1);
        
        interceptorRegistry.collectInterceptors(methodWithdraw, collected);
        assertThat(collected, hasSize(1));
        assertThat(collected, containsInAnyOrder(interceptor1));
    }
    
    @Test
    public void annotationInterceptor_mutipleInterceptorOnOneAnnotation() {
        interceptorRegistry.registerAnnotationInterceptor(Constraint.class, interceptor1);
        interceptorRegistry.registerAnnotationInterceptor(Constraint.class, interceptor2);
        
        interceptorRegistry.collectInterceptors(methodWithdraw, collected);
        assertThat(collected, hasSize(2));
        assertThat(collected, containsInAnyOrder(interceptor1, interceptor2));
    }
    
    @Test
    public void annotationInterceptor_twoInterceptorsByTwoAnnotations() {
        interceptorRegistry.registerAnnotationInterceptor(Constraint.class, interceptor1);
        interceptorRegistry.registerAnnotationInterceptor(Tx.class, interceptor2);
        interceptorRegistry.registerAnnotationInterceptor(Profiling.class, interceptor3);
        
        interceptorRegistry.collectInterceptors(methodWithdraw, collected);
        assertThat(collected, hasSize(2));
        assertThat(collected, containsInAnyOrder(interceptor1, interceptor2));
    }
    
    public static class PlaygroundModel {
        @Tx
        @Constraint(expression="amout>0 && currency!=null")
        public void withdraw(int amount, Currency currency) {
        }
    }
    
    protected static <T> Matcher<Collection<T>> isEmpty(Class<T> type) {
        return org.hamcrest.collection.IsEmptyCollection.<T>empty();
    }

}
