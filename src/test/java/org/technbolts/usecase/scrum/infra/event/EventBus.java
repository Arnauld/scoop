package org.technbolts.usecase.scrum.infra.event;

public interface EventBus {

    public void publish(Event<?> event);
}
