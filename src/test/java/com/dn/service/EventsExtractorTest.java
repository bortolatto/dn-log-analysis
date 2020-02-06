package com.dn.service;

import com.dn.commands.DocumentCommand;
import com.dn.commands.GetEventBuilder;
import com.dn.commands.StartingEventCommand;
import com.dn.model.XEvent;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class EventsExtractorTest {
    private static DocumentCommand documentCommand;
    private static Path path;

    @BeforeClass
    public static void setUp() {
        StartingEventCommand startingEventCommand = new StartingEventCommand(null);
        documentCommand = new DocumentCommand(startingEventCommand);
        ClassLoader classLoader = LogAnalyserTest.class.getClassLoader();
        path = Paths.get(Objects.requireNonNull(classLoader.getResource("simple-log-input.txt")).getFile());
    }

    @Test
    public void shouldExtractStartRenderingEventsFromLog() throws IOException {
        LogAnalyser logAnalyser = new LogAnalyser(path, documentCommand);
        List<String> logFile = logAnalyser.readLog();

        EventsExtractor eventsExtractor = new EventsExtractor(new StartingEventCommand(null));
        eventsExtractor.process(logFile);
        List<XEvent> xEvents = eventsExtractor.getEvents().get("1286373785873-3536");

        Assertions.assertThat(xEvents.size()).isEqualTo(1);
        Assertions.assertThat(xEvents.get(0).getType()).isEqualTo(XEvent.EventType.START_RENDERING);
    }

    @Test
    public void shouldExtractGetRenderingEventsFromLog() throws IOException {
        LogAnalyser logAnalyser = new LogAnalyser(path, documentCommand);
        List<String> logFile = logAnalyser.readLog();

        EventsExtractor eventsExtractor = new EventsExtractor(new GetEventBuilder());
        eventsExtractor.process(logFile);
        List<XEvent> xEvents = eventsExtractor.getEvents().get("1286373785873-3536");

        Assertions.assertThat(xEvents.size()).isEqualTo(1);
        Assertions.assertThat(xEvents.get(0).getType()).isEqualTo(XEvent.EventType.GET);
    }
}