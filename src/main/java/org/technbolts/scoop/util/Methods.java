package org.technbolts.scoop.util;

import static fj.data.List.iterableList;
import static org.technbolts.scoop.util.Equals.areEquals;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import fj.F;
import fj.F2;

public class Methods {
    
    public static boolean sameNames(Method m, Method method) {
        return m.getName().equals(method.getName());
    }
    
    public static boolean sameArgumentTypes(Method m, Method method) {
        return Arrays.equals(m.getParameterTypes(), method.getParameterTypes());
    }
    
    public static boolean sameReturnType(Method m1, Method m2) {
        return areEquals(m1.getReturnType(), m2.getReturnType());
    }
    
    /**
     * Shortcut for {@link #sameNames(Method, Method)}, {@link #sameArgumentTypes(Method, Method)}
     * and {@link #sameReturnType(Method, Method)}.
     */
    public static boolean sameSignature(Method m1, Method m2) {
        return  sameNames(m1, m2) && //
                sameArgumentTypes(m1, m2) && //
                sameReturnType(m1, m2);
    }
    
    public static F2<List<Method>, Class<?>, List<Method>> classMethodsFoldF() {
        return new F2<List<Method>, Class<?>, List<Method>> () {
            @Override
            public List<Method> f(List<Method> a, Class<?> b) {
                for(Method m : b.getDeclaredMethods())
                    a.add(m);
                return a;
            }
        };
    }
    
    public static F<Method, String> classSimpleNameF () {
        return declaringClassF().andThen(Classes.simpleNameF());
    }
    
    public static F<Method, Class<?>> declaringClassF () {
        return new F<Method, Class<?>>() {
            @Override
            public Class<?> f(Method m) {
                return m.getDeclaringClass();
            }
        };
    }
    
    public static String toClassSimpleNames(Iterable<Method> methods) {
        return toClassSimpleNames(iterableList(methods));
    }
    
    public static String toClassSimpleNames(fj.data.List<Method> list) {
        StringBuilder builder = list.map(classSimpleNameF()).foldLeft(Strings.joinF(", "), new StringBuilder());
        return builder.toString();
    }

}
