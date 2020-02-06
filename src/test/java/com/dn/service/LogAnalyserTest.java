package com.dn.service;

import com.dn.commands.DocumentCommand;
import com.dn.commands.GetEventBuilder;
import com.dn.commands.StartingEventCommand;
import com.dn.model.Document;
import com.dn.model.Rendering;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class LogAnalyserTest {

    @Test
    public void shouldHaveOneRendering() throws IOException, XMLStreamException {
        ClassLoader classLoader = getClass().getClassLoader();
        Path path = Paths.get(Objects.requireNonNull(classLoader.getResource("simple-log-input.txt")).getFile());

        StartingEventCommand startingEventCommand = new StartingEventCommand(null);
        DocumentCommand documentCommand = new DocumentCommand(startingEventCommand);

        LogAnalyser logAnalyser = new LogAnalyser(path, documentCommand);
        List<String> logFile = logAnalyser.readLog();
        logAnalyser.scan(logFile);

        Document document = new Document(114466, 0, "WorkerThread-17");
        Rendering rendering = new Rendering(document, "1286373785873-3536");

        Assertions.assertThat(logAnalyser.getEventRenderings()).contains(rendering);

        XMLWriter xmlWriter = new XMLWriter();
        EventsExtractor eventsExtractor = new EventsExtractor(new StartingEventCommand(null));
        EventsExtractor eventsExtractor1 = new EventsExtractor(new GetEventBuilder());
        eventsExtractor.process(logFile);
        eventsExtractor1.process(logFile);

        PersistEventsToRenderings persistEventsToRenderings = new PersistEventsToRenderings();
        persistEventsToRenderings.persist(logAnalyser.getEventRenderings(), eventsExtractor.getEvents());
        persistEventsToRenderings.persist(logAnalyser.getEventRenderings(), eventsExtractor1.getEvents());


        SummaryService summaryService = new SummaryService(logAnalyser.getEventRenderings(),
                eventsExtractor.getEvents().keySet(),
                eventsExtractor1.getEvents().keySet());


        xmlWriter.write(logAnalyser.getEventRenderings(), summaryService.getSummary());
    }
}