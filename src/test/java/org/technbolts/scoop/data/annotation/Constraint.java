package org.technbolts.scoop.data.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Constraint {
    String expression();
    Lang lang() default Lang.Mvel;
    
    public enum Lang {
        Mvel,
        Ocl,
        Groovy
    }
}
