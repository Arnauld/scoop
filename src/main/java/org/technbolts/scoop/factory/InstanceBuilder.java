package org.technbolts.scoop.factory;

import java.util.List;
import java.util.Set;

import net.sf.cglib.proxy.Enhancer;

import org.technbolts.scoop.Interceptor;
import org.technbolts.scoop.InterceptorRegistry;
import org.technbolts.util.New;

public class InstanceBuilder {

    private Class<?>[] mixins;
    private Class<?> superClazz;
    private Class<?>[] interfaces;
    private InterceptorRegistry interceptorRegistry;
    private Set<Interceptor> interceptors;
    private List<InstanceCreationPostProcessor> postProcessors;
    
    public InstanceBuilder() {
        this.interceptors = New.hashSet();
        this.postProcessors = New.arrayList();
    }
    
    public InstanceBuilder withInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }
    
    public InstanceBuilder withInterceptors(Interceptor... interceptors) {
        for(Interceptor interceptor : interceptors)
            this.interceptors.add(interceptor);
        return this;
    }

    public InstanceBuilder withSuperClazz(Class<?> superClazz) {
        this.superClazz = superClazz;
        return this;
    }

    public InstanceBuilder withInterfaces(Class<?>... interfaces) {
        this.interfaces = interfaces;
        return this;
    }

    public InstanceBuilder withMixins(Class<?>... mixins) {
        this.mixins = mixins;
        return this;
    }

    public InstanceBuilder withInterceptorRegistry(InterceptorRegistry interceptorRegistry) {
        this.interceptorRegistry = interceptorRegistry;
        return this;
    }
    
    public InstanceBuilder withPostProcessors(InstanceCreationPostProcessor... postProcessors) {
        for(InstanceCreationPostProcessor postProcessor : postProcessors)
            this.postProcessors.add(postProcessor);
        return this;
    }

    protected Dispatcher newDispatcher() throws InstantiationException, IllegalAccessException {
        return new Dispatcher(mixins, interceptorRegistry, interceptors);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T newInstance() throws InstantiationException, IllegalAccessException {
        Dispatcher dispatcher = newDispatcher();

        Enhancer e = new Enhancer();
        if (superClazz != null)
            e.setSuperclass(superClazz);
        e.setInterfaces(interfaces);
        e.setCallback(dispatcher);
        
        Object bean = e.create();
        dispatcher.postProcess(bean, postProcessors);
        return (T)bean;
    }

}
