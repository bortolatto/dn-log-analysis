package com.dn.service;

import com.dn.commands.DocumentCommand;
import com.dn.commands.GetEventBuilder;
import com.dn.commands.StartingEventCommand;
import com.dn.model.Summary;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SummaryServiceTest {
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
    public void shouldBuildSummaryWithOneEvent() throws IOException {
        LogAnalyser logAnalyser = new LogAnalyser(path, documentCommand);
        List<String> logFile = logAnalyser.readLog();
        logAnalyser.scan(logFile);

        EventsExtractor eventsExtractor = new EventsExtractor(new GetEventBuilder());
        eventsExtractor.process(logFile);

        SummaryService summaryService = new SummaryService(logAnalyser.getEventRenderings(),
                eventsExtractor.getEvents().keySet(),
                Collections.emptySet());
        Summary summary = summaryService.getSummary();

        Assertions.assertThat(summary.getTotalRenderings()).isEqualTo(1);
        Assertions.assertThat(summary.getDuplicateRenderings()).isEqualTo(0);
        Assertions.assertThat(summary.getOrphanRenderings()).isEqualTo(1);
    }

}