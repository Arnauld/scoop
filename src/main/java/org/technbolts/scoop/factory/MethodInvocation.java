package org.technbolts.scoop.factory;

import static org.technbolts.util.Objects.o;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.technbolts.scoop.Interceptor;
import org.technbolts.scoop.exception.InvocationException;

public class MethodInvocation {
    private static Logger log = LoggerFactory.getLogger(MethodInvocation.class);

    private Object obj;
    private Method method;
    private Object[] args;
    //
    private Interceptor[] interceptors;
    private int index;
    
    public MethodInvocation(Object obj, Method method, Object[] args, Interceptor[] interceptors) {
        super();
        this.obj = obj;
        this.method = method;
        this.args = args;
        this.interceptors = (interceptors==null)?Interceptor.EMPTY:interceptors;
        log.debug("Invocation built for method {} with #{} interceptors: [{}] ", 
                o(method.getName(), this.interceptors.length, Arrays.toString(this.interceptors)));
    }
    
    public Object getObj() {
        return obj;
    }
    
    public Method getMethod() {
        return method;
    }
    
    public Object[] getArgs() {
        return args;
    }

    public Object invokeNext() {
        if(index==interceptors.length) {
            log.debug("Final link in the chain: invoking method {}", method);
            return invoke();
        }
        
        try {
            Interceptor interceptor = interceptors[index++];
            log.debug("About to call interceptor {}", interceptor);
            return interceptor.invoke(this);
        } finally {
            index--;
        }
    }

    public Object invoke() {
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new InvocationException("Failed to invoke method <" + method + "> on instance <" + obj + "> with arguments <" + Arrays.toString(args) + ">", e);
        }
    }
}
