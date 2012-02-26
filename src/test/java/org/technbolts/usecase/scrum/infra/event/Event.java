package org.technbolts.usecase.scrum.infra.event;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class Event<T> {

    private final String eventId;
    private final T payload;
    private final Map<String,String> headers;
    
    public Event(String eventId, T payload, Map<String,String> headers) {
        this.eventId = eventId;
        this.payload = payload;
        this.headers = Collections.unmodifiableMap(headers);
    }
    
    public Collection<String> headerNames() {
        return headers.keySet();
    }
    
    public String getHeader(String header) {
        return headers.get(header);
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public T getPayload() {
        return payload;
    }
}
