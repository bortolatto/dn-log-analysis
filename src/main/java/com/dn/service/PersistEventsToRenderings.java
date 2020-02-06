package com.dn.service;

import com.dn.model.Rendering;
import com.dn.model.XEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PersistEventsToRenderings {
    public void persist(List<Rendering> renderingList, Map<String, List<XEvent>> events) {
        renderingList.forEach(
                rendering -> {
                    if (events.containsKey(rendering.getUid())) {
                        rendering.getEvents().addAll(events.get(rendering.getUid()).stream()
                                .filter(e -> e.getUid().equals(rendering.getUid()))
                                .collect(Collectors.toList()));
                    }
                }
        );
    }
}
