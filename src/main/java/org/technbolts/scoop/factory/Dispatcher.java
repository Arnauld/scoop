package org.technbolts.scoop.factory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.technbolts.scoop.Interceptor;
import org.technbolts.scoop.InterceptorRegistry;
import org.technbolts.scoop.util.Methods;
import org.technbolts.scoop.util.New;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class Dispatcher implements MethodInterceptor {

    private final Class<?>[] mixinClasses;
    private final Object[] mixinInstances;
    private final InterceptorRegistry interceptorRegistry;
    private final Set<Interceptor> interceptors;

    public Dispatcher(Class<?>[] mixinClasses, InterceptorRegistry interceptorRegistry, Set<Interceptor> interceptors) throws InstantiationException, IllegalAccessException {
        this.mixinClasses = mixinClasses;
        this.mixinInstances = new Object[mixinClasses.length];
        this.interceptors = interceptors;
        this.interceptorRegistry = interceptorRegistry;
        for (int i = 0; i < mixinClasses.length; i++) {
            mixinInstances[i] = mixinClasses[i].newInstance();
        }
    }

    protected void postProcess(Object assembledInstance, List<InstanceCreationPostProcessor> postProcessors) throws IllegalArgumentException, IllegalAccessException {
        for(InstanceCreationPostProcessor postProcessor: postProcessors) {
            postProcessor.postProcess(assembledInstance, mixinClasses, mixinInstances);
        }
    }

    /**
     * All generated proxied methods call this method instead of the original method. The original method may either be
     * invoked by normal reflection using the Method object, or by using the MethodProxy (faster).
     * 
     * @param obj
     *            "this", the enhanced object
     * @param proxyMethod
     *            intercepted Method
     * @param args
     *            argument array; primitive types are wrapped
     * @param proxy
     *            used to invoke super (non-intercepted method); may be called as many times as needed
     * @throws Throwable
     *             any exception may be thrown; if so, super method will not be invoked
     * @return any value compatible with the signature of the proxied method. Method returning void will ignore this
     *         value.
     * @see MethodProxy
     */
    public Object intercept(Object obj, Method proxyMethod, Object[] args, MethodProxy proxy) throws Throwable {
        for (int i = 0; i < mixinClasses.length; i++) {
            Class<?> mixinClass = mixinClasses[i];
            for (Method mixinMethod : mixinClass.getMethods()) {
                if (Methods.sameSignature(mixinMethod, proxyMethod)) {
                    Object mixinInstance = mixinInstances[i];
                    return newInvocation(mixinClass, mixinInstance, proxyMethod, mixinMethod, args).invokeNext();
                }
            }
        }
        throw new IllegalStateException("No implementation found");
    }

    protected MethodInvocation newInvocation(Class<?> mixinClass, Object mixinInstance, //
            Method proxyMethod, Method mixinMethod, Object[] args) {
        Set<Interceptor> interceptors = collectInterceptors(mixinInstance, proxyMethod, mixinMethod, interceptorRegistry);
        if (interceptors.isEmpty())
            return new MethodInvocation(mixinInstance, mixinMethod, args, Interceptor.EMPTY);
        else {
            Interceptor[] interceptorArray = interceptors.toArray(new Interceptor[interceptors.size()]);
            Arrays.sort(interceptorArray, new Comparator<Interceptor>() {
                public int compare(Interceptor o1, Interceptor o2) {
                    return o1.precedence() - o2.precedence();
                }
            });
            return new MethodInvocation(mixinInstance, mixinMethod, args, interceptorArray);
        }
    }

    protected Set<Interceptor> collectInterceptors(Object mixinInstance, Method proxyMethod, Method mixinMethod,
            InterceptorRegistry interceptorRegistry) {
        Set<Interceptor> selected = New.hashSet();
        interceptorRegistry.collectInterceptors(proxyMethod, selected);
        interceptorRegistry.collectInterceptors(mixinMethod, selected);
        interceptorRegistry.collectInterceptors(mixinInstance, selected);
        selected.addAll(interceptors);
        return selected;
    }

}