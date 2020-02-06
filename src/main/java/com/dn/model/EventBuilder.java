package com.dn.model;

import java.util.function.Predicate;

public interface EventBuilder {
    XEvent build(String line);

    Predicate<String> predicate();
}
