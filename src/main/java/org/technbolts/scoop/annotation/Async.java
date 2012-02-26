package org.technbolts.scoop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks a method as a candidate for <i>asynchronous</i> execution.
 * Can also be used at the type level, in which case all of the type's methods are
 * considered as asynchronous.
 *
 * <p>In terms of target method signatures, any parameter types are supported.
 * However, the return type is constrained to either <code>void</code> or
 * <code>java.util.concurrent.Future</code>. In the latter case, the Future handle
 * returned from the proxy will be an actual asynchronous Future that can be used
 * to track the result of the asynchronous method execution. However, since the
 * target method needs to implement the same signature, it will have to return
 * a temporary Future handle that just passes the return value through: e.g.
 * Spring's {@link AsyncResult} or EJB 3.1's <code>javax.ejb.AsyncResult</code>.
 *
 * @author Juergen Hoeller
 * @since 3.0
 * @see org.springframework.aop.interceptor.AsyncExecutionInterceptor
 * @see AsyncAnnotationAdvisor
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Async {
    String executor() default "default";
}
