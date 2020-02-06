package com.dn.service;

import com.dn.commands.DocumentCommand;
import com.dn.commands.GetEventBuilder;
import com.dn.commands.StartingEventCommand;
import com.dn.model.Rendering;
import com.dn.model.XEvent;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class MergeEventsToDocumentsTest {
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
    public void shouldMergeEventsAndDocuments() throws IOException {
        LogAnalyser logAnalyser = new LogAnalyser(path, documentCommand);
        List<String> logFile = logAnalyser.readLog();
        logAnalyser.scan(logFile);

        EventsExtractor startRenderingEvents = new EventsExtractor(new StartingEventCommand(null));
        startRenderingEvents.process(logFile);
        EventsExtractor getRenderingEvents = new EventsExtractor(new GetEventBuilder());
        getRenderingEvents.process(logFile);

        MergeEventsToDocuments mergeEventsToDocuments = new MergeEventsToDocuments();
        mergeEventsToDocuments.merge(logAnalyser.getEventRenderings(), startRenderingEvents.getEvents());
        mergeEventsToDocuments.merge(logAnalyser.getEventRenderings(), getRenderingEvents.getEvents());

        Rendering rendering = logAnalyser.getEventRenderings().get(0);
        XEvent startEvent = rendering.getEvents().get(0);
        XEvent getEvent = rendering.getEvents().get(1);

        Assertions.assertThat(rendering.getEvents().size()).isEqualTo(2);
        Assertions.assertThat(startEvent.getType()).isEqualTo(XEvent.EventType.START_RENDERING);
        Assertions.assertThat(getEvent.getType()).isEqualTo(XEvent.EventType.GET);
    }
}