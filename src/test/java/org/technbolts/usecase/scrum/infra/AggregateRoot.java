package org.technbolts.usecase.scrum.infra;

public interface AggregateRoot<TYPE, ID extends Id<ID>> {
    ID getAggregateId();
}
