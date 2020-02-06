package com.dn.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class XEvent {
    @Getter
    private final EventType type;
    @Getter
    private final String occurredAt;
    @Getter
    @ToString.Exclude
    private final String uid;

    public enum EventType {
        START_RENDERING("start"),
        GET("get");
        private String type;

        EventType(String type) {
            this.type = type;
        }

        public String value() {
            return type;
        }
    }
}
