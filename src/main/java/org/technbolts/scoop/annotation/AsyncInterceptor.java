package org.technbolts.scoop.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.technbolts.scoop.Interceptor;
import org.technbolts.scoop.factory.MethodInvocation;

public class AsyncInterceptor implements Interceptor {
    
    private static Logger log = LoggerFactory.getLogger(AsyncInterceptor.class);
    
    private ExecutorService asyncExecutor;
    
    public AsyncInterceptor(ExecutorService asyncExecutor) {
        if(asyncExecutor==null)
            throw new IllegalArgumentException("No executor provided");
        this.asyncExecutor = asyncExecutor;
    }
    
    public int precedence() {
        return 16;
    }

    public Object invoke(final MethodInvocation invocation) {
        Callable<Object> callable = new Callable<Object>() {
            public Object call() throws Exception {
                try {
                    log.info("Executing task asynchronously");
                    Object result = invocation.invokeNext();
                    if (result instanceof Future) {
                        return ((Future<?>) result).get();
                    }
                    return null;
                } catch (Throwable e) {
                    throw new InvocationTargetException(e);
                }
            }
        };
        return spawn(invocation.getMethod(), callable);
    }
    
    private Object spawn(final Method method, Callable<Object> callable) {
        Future<?> result = asyncExecutor.submit(callable);
        if (Future.class.isAssignableFrom(method.getReturnType())) {
            return result;
        } else {
            return null;
        }
    }
    
}
