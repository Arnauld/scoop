package org.technbolts.scoop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;

public class InterceptorRegistry {
    
    private static Logger log = LoggerFactory.getLogger(InterceptorRegistry.class);

    private ArrayListMultimap<Class<? extends Annotation>, Interceptor> annotationInterceptors;
    private ArrayListMultimap<Class<?>, Interceptor> classInterceptors;
    private ArrayListMultimap<Class<?>, Interceptor> implementingInterceptors;
    
    public InterceptorRegistry() {
        annotationInterceptors = ArrayListMultimap.create();
        classInterceptors = ArrayListMultimap.create();
        implementingInterceptors = ArrayListMultimap.create();
    }
    
    public void registerAnnotationInterceptor(Class<? extends Annotation> what, Interceptor interceptor) {
        annotationInterceptors.put(what, interceptor);
    }

    public void registerMixinClassInterceptor(Class<?> what, Interceptor interceptor) {
        classInterceptors.put(what, interceptor);
    }
    
    public void registerImplementingInterceptor(Class<?> what, Interceptor interceptor) {
        implementingInterceptors.put(what, interceptor);
    }
    
    public void collectInterceptors(Method method, Set<Interceptor> collected) {
        log.debug("Looking for annotation on method <{}>", method);
        int count = 0;
        for(Map.Entry<Class<? extends Annotation>, Interceptor> e : annotationInterceptors.entries()) {
            if(method.isAnnotationPresent(e.getKey())) {
                collected.add(e.getValue());
                count++;
            }
        }
        log.debug("#{} interceptors found on method <{}>", count, method);
    }
    
    public void collectInterceptors(Object mixinInstance, Set<Interceptor> collected) {
        Class<? extends Object> klazz = mixinInstance.getClass();
        log.debug("Looking for annotation on instance <{}> ({})", mixinInstance, klazz);
        
        List<Interceptor> interceptors = classInterceptors.get(klazz);
        int count = interceptors.size();
        collected.addAll(interceptors);
        
        for(Map.Entry<Class<?>, Interceptor> e : implementingInterceptors.entries()) {
            if(e.getKey().isAssignableFrom(klazz)) {
                count++;
                collected.add(e.getValue());
            }
        }
        log.debug("#{} interceptors found for instance <{}>", count, mixinInstance);
    }
}
