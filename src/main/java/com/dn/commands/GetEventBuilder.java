package com.dn.commands;

import com.dn.model.EventBuilder;
import com.dn.model.XEvent;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetEventBuilder implements EventBuilder {
    private static final String REGEX = "^(?<occurredAt>.{23}).+getRendering.+\\s\\[(?<uid>\\d{13}-\\d+)\\]";
    protected Pattern pattern;
    protected Matcher matcher;

    public GetEventBuilder() {
        pattern = Pattern.compile(REGEX);
    }

    @Override
    public XEvent build(String line) {
        matcher = pattern.matcher(line);
        matcher.find();
        return new XEvent(XEvent.EventType.GET, matcher.group("occurredAt"), matcher.group("uid"));
    }

    @Override
    public Predicate<String> predicate() {
        return pattern.asPredicate();
    }
}
