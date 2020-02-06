package com.dn.commands;

import com.dn.model.EventBuilder;
import com.dn.model.Rendering;
import com.dn.model.XEvent;
import com.dn.service.LogAnalyser;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.dn.model.XEvent.EventType;

public class StartingEventCommand extends PatternCommand implements EventBuilder {
    private static final String REGEX = "(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2},\\d+)\\s\\[(WorkerThread-\\d+)\\].+startRendering returned\\s(\\d{13}-\\d+)";

    public StartingEventCommand(PatternCommand nextCommand) {
        super(nextCommand);
        this.pattern = Pattern.compile(REGEX);
    }

    @Override
    void process(LogAnalyser logAnalyser) {
        logAnalyser.getEventRenderings().add(new Rendering(logAnalyser.getCurrentDocument(), matcher.group(3)));
    }

    @Override
    public XEvent build(String line) {
        matches(line);
        return new XEvent(EventType.START_RENDERING, matcher.group(1), matcher.group(3));
    }

    @Override
    public Predicate<String> predicate() {
        return Pattern.compile(REGEX).asPredicate();
    }
}
