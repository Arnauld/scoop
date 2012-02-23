package org.technbolts.scoop.factory;

public interface InstanceCreationPostProcessor {
    void postProcess(Object assembledInstance, Class<?>[] mixinClasses, Object[] mixinInstances);
}
