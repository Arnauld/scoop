package org.technbolts.scoop.factory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.technbolts.scoop.util.Objects.o;

import java.lang.reflect.Method;

import org.technbolts.scoop.Interceptor;
import org.technbolts.scoop.data.Person;
import org.technbolts.scoop.factory.MethodInvocation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MethodInvocationTest {

    private Method methodAppendFullName, methodGetFullName;
    private Person person;
    private StringBuilder stringBuilder;

    @BeforeMethod
    public void setUp () throws SecurityException, NoSuchMethodException {
        methodAppendFullName = Person.class.getDeclaredMethod("appendFullName", String.class, StringBuilder.class);
        methodGetFullName = Person.class.getDeclaredMethod("getFullName", String.class);
        person = new Person();
        person.setFirstName("Carmen");
        person.setLastName("McCallum");
        stringBuilder = new StringBuilder();
    }
    
    @Test
    public void invokeNext_nullInterceptor () {
        MethodInvocation invocation = new MethodInvocation(person, methodGetFullName, o("%2$s %1$s"), null);
        assertThat((String)invocation.invokeNext(), equalTo("McCallum Carmen"));
    }
    
    @Test
    public void invoke_nullInterceptor () {
        MethodInvocation invocation = new MethodInvocation(person, methodGetFullName, o("%2$s %1$s"), null);
        assertThat((String)invocation.invoke(), equalTo("McCallum Carmen"));
    }

    @Test
    public void invokeNext_oneInterceptor () {
        
        Interceptor interceptor = upAndDownStreamInterceptor("what", stringBuilder);
        MethodInvocation invocation = new MethodInvocation(person, methodAppendFullName, //
                o("%2$s %1$s", stringBuilder), 
                new Interceptor[] {interceptor});
        Object result = invocation.invokeNext();
        assertThat(result, nullValue());
        assertThat(stringBuilder.toString(), equalTo("<what>McCallum Carmen</what>"));
    }
    
    @Test
    public void invokeNext_fourInterceptors () {
        Interceptor interceptor1 = upAndDownStreamInterceptor("auth", stringBuilder);
        Interceptor interceptor2 = upAndDownStreamInterceptor("audit", stringBuilder);
        Interceptor interceptor3 = upAndDownStreamInterceptor("prof", stringBuilder);
        Interceptor interceptor4 = upAndDownStreamInterceptor("tx", stringBuilder);
        MethodInvocation invocation = new MethodInvocation(person, methodAppendFullName, o("%2$s %1$s", stringBuilder), 
                new Interceptor[] {interceptor1, interceptor2, interceptor3, interceptor4});
        
        assertThat(invocation.invokeNext(), nullValue());
        assertThat(stringBuilder.toString(), equalTo("<auth><audit><prof><tx>McCallum Carmen</tx></prof></audit></auth>"));
    }

    public static Interceptor upAndDownStreamInterceptor(final String name, final StringBuilder chain) {
        Interceptor interceptor = new Interceptor() {
            public int precedence() {
                return 1;
            }
            public Object invoke(MethodInvocation invocation) {
                try {
                    chain.append("<" + name + ">");
                    return invocation.invokeNext();
                }
                finally {
                    chain.append("</" + name + ">");
                }
            }
        };
        return interceptor;
    }
}
