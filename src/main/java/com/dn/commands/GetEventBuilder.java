package com.dn.commands;

import com.dn.model.EventBuilder;
import com.dn.model.XEvent;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetEventBuilder implements EventBuilder {
    private static final String REGEX = "(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2},\\d+).+getRendering with arguments\\s\\[(\\d{13}-\\d+)";
    protected Pattern pattern;
    protected Matcher matcher;

    public GetEventBuilder() {
        pattern = Pattern.compile(REGEX);
    }

    @Override
    public XEvent build(String line) {
        matcher = pattern.matcher(line);
        matcher.find();
        return new XEvent(XEvent.EventType.GET, matcher.group(1), matcher.group(2));
    }

    @Override
    public Predicate<String> predicate() {
        return pattern.asPredicate();
    }
}
