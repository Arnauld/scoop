package org.technbolts.util;

import java.lang.reflect.Method;

import fj.F;

public class MethodView {
    
    public static F<MethodView, Boolean> sameBaseClassAndMethodSignatureAs(final MethodView a) {
        return new F<MethodView, Boolean>() {
            @Override
            public Boolean f(MethodView b) {
                return a.getBaseClass()==b.getBaseClass() && 
                        Methods.sameSignature(a.getMethod(),b.getMethod());
            }
        };
    }

    
    public static MethodView viewOf(Class<?> baseClass, String methodName, Class<?>...parameterTypes) throws SecurityException, NoSuchMethodException {
        return new MethodView(baseClass, baseClass.getMethod(methodName, parameterTypes));
    }

    public static MethodView viewOf(Class<?> baseClass, Method method) {
        return new MethodView(baseClass, method);
    }
    
    public static F<MethodView, Class<?>> declaringClassF () {
        return new F<MethodView, Class<?>>() {
            @Override
            public Class<?> f(MethodView m) {
                return m.getMethod().getDeclaringClass();
            }
        };
    }
    
    public static F<MethodView, Class<?>> baseClassF () {
        return new F<MethodView, Class<?>>() {
            @Override
            public Class<?> f(MethodView m) {
                return m.getBaseClass();
            }
        };
    }

    private final Class<?> baseClass;
    private final Method method;
    private String signature;

    public MethodView(Class<?> baseClass, Method method) {
        super();
        this.baseClass = baseClass;
        this.method = method;
    }
    
    public MethodView withSignature(String signature) {
        this.signature = signature;
        return this;
    }
    
    public String getSignature() {
        return signature;
    }

    /**
     * @return the baseClass
     */
    public Class<?> getBaseClass() {
        return baseClass;
    }

    /**
     * @return the method
     */
    public Method getMethod() {
        return method;
    }

}
