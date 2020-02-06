package com.dn.service;

import com.dn.model.EventBuilder;
import com.dn.model.XEvent;
import lombok.Getter;

import java.util.*;

public class EventsExtractor {
    private final EventBuilder eventBuilder;
    @Getter
    private Map<String, List<XEvent>> events = new HashMap<>();

    public EventsExtractor(EventBuilder eventBuilder) {
        this.eventBuilder = eventBuilder;
    }

    public void process(List<String> lines) {
        lines.stream().filter(eventBuilder.predicate()).forEach(line -> add(eventBuilder.build(line)));
    }

    public void add(XEvent event) {
        if (events.containsKey(event.getUid())) {
            events.get(event.getUid()).add(event);
        } else {
            ArrayList<XEvent> _events = new ArrayList<>();
            _events.add(event);
            events.put(event.getUid(), _events);
        }
    }

    public Optional<List<XEvent>> getEventsByUid(String uid) {
        if (!events.containsKey(uid)) {
            return Optional.empty();
        }
        return Optional.of(events.get(uid));
    }
}