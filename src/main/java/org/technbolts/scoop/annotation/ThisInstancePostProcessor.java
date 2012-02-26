package org.technbolts.scoop.annotation;

import java.lang.reflect.Field;

import org.technbolts.scoop.exception.PostProcessorException;
import org.technbolts.scoop.factory.InstanceCreationPostProcessor;

public class ThisInstancePostProcessor implements InstanceCreationPostProcessor {

    public void postProcess(Object assembledInstance, Class<?>[] mixinClasses, Object[] mixinInstances) {
        Class<?> mixinClass;
        Field field;
        try {
            for (int i = 0; i < mixinClasses.length; i++) {
                mixinClass = mixinClasses[i];
                Field[] fields = mixinClass.getDeclaredFields();
                for (int j=0;j<fields.length;j++) {
                    field = fields[j];
                    if (field.isAnnotationPresent(This.class)) {
                        if(!field.isAccessible())
                            field.setAccessible(true);
                        field.set(mixinInstances[i], assembledInstance);
                    }
                }
            }
        } catch (SecurityException e) {
            throw new PostProcessorException(e);
        } catch (IllegalArgumentException e) {
            throw new PostProcessorException(e);
        } catch (IllegalAccessException e) {
            throw new PostProcessorException(e);
        }
    }
}
