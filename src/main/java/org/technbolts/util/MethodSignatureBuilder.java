package org.technbolts.util;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MethodSignatureBuilder {
    private static Logger log = LoggerFactory.getLogger(MethodSignatureBuilder.class);

    private boolean includeOwningClass;
    private boolean typeSimpleName;
    
    public MethodSignatureBuilder() {
        this.includeOwningClass = false;
        this.typeSimpleName = true;
    }
    
    public boolean isIncludeOwningClass() {
        return includeOwningClass;
    }
    
    public MethodSignatureBuilder includeOwningClass() {
        this.includeOwningClass = true;
        return this;
    }

    public MethodSignatureBuilder excludeOwningClass() {
        this.includeOwningClass = false;
        return this;
    }
    
    public String formatSignature(Method method) {
        StringBuilder builder = new StringBuilder ();
        formatSignature(method, builder);
        return builder.toString();
    }
    
    public String formatSignature(MethodView view) {
        StringBuilder builder = new StringBuilder ();
        formatSignature(view, builder);
        return builder.toString();
    }
    
    public void formatSignature(MethodView view, StringBuilder builder) {
        log.debug("View <{}>", view.getSignature());
        appendMethodName(builder, view);
        builder.append("(");
        appendMethodParameterTypes(builder, view.getMethod());
        builder.append("):");
        appendMethodReturnType(builder, view.getMethod());
    }

    public void formatSignature(Method method, StringBuilder builder) {
        appendMethodName(builder, method);
        builder.append("(");
        appendMethodParameterTypes(builder, method);
        builder.append("):");
        appendMethodReturnType(builder, method);
    }
    
    protected void appendMethodName(StringBuilder builder, Method method) {
        if(includeOwningClass)
            appendType(builder, method.getDeclaringClass()).append("#");
        builder.append(method.getName());
    }
    
    protected void appendMethodName(StringBuilder builder, MethodView view) {
        if(includeOwningClass)
            appendType(builder, view.getBaseClass()).append("#");
        builder.append(view.getMethod().getName());
    }

    
    protected void appendMethodParameterTypes(StringBuilder builder, Method method) {
        boolean first = true;
        for(Class<?> c : method.getParameterTypes()) {
            if(first)
                first = false;
            else
                builder.append(", ");
            appendType(builder, c);
        }
    }
    
    protected void appendMethodReturnType(StringBuilder builder, Method method) {
        appendType(builder, method.getReturnType());
    }
    
    protected StringBuilder appendType(StringBuilder builder, Class<?> type) {
        if(typeSimpleName)
            builder.append(type.getSimpleName());
        else
            builder.append(type.getName());
        return builder;
    }

}
