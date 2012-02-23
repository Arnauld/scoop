package org.technbolts.scoop;

import org.technbolts.scoop.factory.MethodInvocation;


public interface Interceptor {
    
    public static final Interceptor[] EMPTY = new Interceptor[0];
    
    /**
     * Interceptor precedence determines the order in which interceptors are evaluated. 
     * Interceptors with higher precedence are evaluated first.
     * @return
     */
    int precedence();
    
    /**
     * Allows the {@link Interceptor} to do some processing on the invocation before and/or 
     * after the rest of the processing of the processing by the {@link MethodInvocation} or to 
     * short-circuit the processing and just return the invocation value.
     * 
     * @param invocation
     */
    Object invoke(MethodInvocation invocation);
}
