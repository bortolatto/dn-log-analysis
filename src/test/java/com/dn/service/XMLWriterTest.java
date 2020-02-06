package com.dn.service;

import com.dn.commands.DocumentCommand;
import com.dn.commands.GetEventBuilder;
import com.dn.commands.StartingEventCommand;
import com.dn.model.Summary;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class XMLWriterTest {
    public static final String FILE_NAME = "test.xml";
    private static DocumentCommand documentCommand;
    private static Path path;

    @BeforeClass
    public static void setUp() {
        StartingEventCommand startingEventCommand = new StartingEventCommand(null);
        documentCommand = new DocumentCommand(startingEventCommand);
    }

    @Before
    public void setup() throws IOException {
        deleteFiles();
    }

    @After
    public void tearDown() throws IOException {
        deleteFiles();
    }

    private void deleteFiles() throws IOException {
        Files.deleteIfExists(Paths.get(FILE_NAME));
    }

    @Test
    public void shouldWriteXMLMultipleGets() throws IOException, XMLStreamException {
        ClassLoader classLoader = LogAnalyserTest.class.getClassLoader();
        path = Paths.get(Objects.requireNonNull(classLoader.getResource("multiple-gets-input.txt")).getFile());

        StringBuilder sb = generateXml(path);

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<report>\n" +
                "\t<rendering>\n" +
                "\t\t<document>115075</document>\n" +
                "\t\t<page>0</page>\n" +
                "\t\t<uid>1286377111759-5215</uid>\n" +
                "\t\t<start>2010-10-06 09:58:31,759</start>\n" +
                "\t\t<get>2010-10-06 09:58:32,407</get>\n" +
                "\t\t<get>2010-10-06 09:58:32,407</get>\n" +
                "\t</rendering>\n" +
                "\t<rendering>\n" +
                "\t\t<document>115456</document>\n" +
                "\t\t<page>0</page>\n" +
                "\t\t<uid>1286377111932-4784</uid>\n" +
                "\t\t<start>2010-10-06 09:58:31,932</start>\n" +
                "\t</rendering>\n" +
                "\t<summary>\n" +
                "\t\t<count>2</count>\n" +
                "\t\t<duplicates>0</duplicates>\n" +
                "\t\t<unnecessary>1</unnecessary>\n" +
                "\t</summary>\n" +
                "</report>";

        Assertions.assertThat(sb.toString().replaceAll("\n\r", ""))
                .isEqualTo(xml.replace("\n", "").replace("\r", ""));
    }

    @Test
    public void shouldWriteXMLWithNotAdditionalEitherGetsOrStarts() throws IOException, XMLStreamException {
        ClassLoader classLoader = LogAnalyserTest.class.getClassLoader();
        path = Paths.get(Objects.requireNonNull(classLoader.getResource("simple-log-input.txt")).getFile());

        StringBuilder sb = generateXml(path);

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<report>\n" +
                "\t<rendering>\n" +
                "\t\t<document>114466</document>\n" +
                "\t\t<page>0</page>\n" +
                "\t\t<uid>1286373785873-3536</uid>\n" +
                "\t\t<start>2010-10-06 09:03:05,873</start>\n" +
                "\t\t<get>2010-10-06 09:03:06,547</get>\n" +
                "\t</rendering>\n" +
                "\t<summary>\n" +
                "\t\t<count>1</count>\n" +
                "\t\t<duplicates>0</duplicates>\n" +
                "\t\t<unnecessary>0</unnecessary>\n" +
                "\t</summary>\n" +
                "</report>";

        Assertions.assertThat(sb.toString().replaceAll("\n\r", ""))
                .isEqualTo(xml.replace("\n", "").replace("\r", ""));
    }

    @Test
    public void shouldWriteXMLWithAdditionalStarts() throws IOException, XMLStreamException {
        ClassLoader classLoader = LogAnalyserTest.class.getClassLoader();
        path = Paths.get(Objects.requireNonNull(classLoader.getResource("multiple-starts-input.txt")).getFile());

        StringBuilder sb = generateXml(path);

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<report>\n" +
                "\t<rendering>\n" +
                "\t\t<document>115351</document>\n" +
                "\t\t<page>0</page>\n" +
                "\t\t<uid>1286377125801-1246</uid>\n" +
                "\t\t<start>2010-10-06 09:58:45,801</start>\n" +
                "\t\t<start>2010-10-06 09:58:46,020</start>\n" +
                "\t\t<get>2010-10-06 09:58:47,089</get>\n" +
                "\t</rendering>\n" +
                "\t<rendering>\n" +
                "\t\t<document>115455</document>\n" +
                "\t\t<page>0</page>\n" +
                "\t\t<uid>1286377126430-4413</uid>\n" +
                "\t\t<start>2010-10-06 09:58:46,430</start>\n" +
                "\t\t<get>2010-10-06 09:58:47,030</get>\n" +
                "\t</rendering>\n" +
                "\t<summary>\n" +
                "\t\t<count>2</count>\n" +
                "\t\t<duplicates>1</duplicates>\n" +
                "\t\t<unnecessary>0</unnecessary>\n" +
                "\t</summary>\n" +
                "</report>";

        Assertions.assertThat(sb.toString().replaceAll("\n\r", ""))
                .isEqualTo(xml.replace("\n", "").replace("\r", ""));
    }

    @Test
    public void shouldWriteXMLStartingWithoutGet() throws IOException, XMLStreamException {
        ClassLoader classLoader = LogAnalyserTest.class.getClassLoader();
        path = Paths.get(Objects.requireNonNull(classLoader.getResource("orphan-input.txt")).getFile());

        StringBuilder sb = generateXml(path);

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<report>\n" +
                "\t<rendering>\n" +
                "\t\t<document>114466</document>\n" +
                "\t\t<page>0</page>\n" +
                "\t\t<uid>1286373785873-3536</uid>\n" +
                "\t\t<start>2010-10-06 09:03:05,873</start>\n" +
                "\t</rendering>\n" +
                "\t<summary>\n" +
                "\t\t<count>1</count>\n" +
                "\t\t<duplicates>0</duplicates>\n" +
                "\t\t<unnecessary>1</unnecessary>\n" +
                "\t</summary>\n" +
                "</report>";

        Assertions.assertThat(sb.toString().replaceAll("\n\r", ""))
                .isEqualTo(xml.replace("\n", "").replace("\r", ""));
    }

    private StringBuilder generateXml(Path path) throws IOException, XMLStreamException {
        LogAnalyser logAnalyser = new LogAnalyser(path, documentCommand);
        List<String> logFile = logAnalyser.readLog();
        logAnalyser.scan(logFile);

        EventsExtractor eventsExtractor = new EventsExtractor(new GetEventBuilder());
        eventsExtractor.process(logFile);

        EventsExtractor startEvents = new EventsExtractor(new StartingEventCommand(null));
        startEvents.process(logFile);

        MergeEventsToDocuments mergeEventsToDocuments = new MergeEventsToDocuments();
        mergeEventsToDocuments.merge(logAnalyser.getEventRenderings(), startEvents.getEvents());
        mergeEventsToDocuments.merge(logAnalyser.getEventRenderings(), eventsExtractor.getEvents());

        SummaryService summaryService = new SummaryService(logAnalyser.getEventRenderings(),
                startEvents.getEvents().keySet(),
                eventsExtractor.getEvents().keySet());
        Summary summary = summaryService.getSummary();

        XMLWriter xmlWriter = new XMLWriter(FILE_NAME);
        xmlWriter.write(summaryService.getDistinctRenderings(), summary);

        LogAnalyser xmlReader = new LogAnalyser(Paths.get(FILE_NAME), null);
        StringBuilder sb = new StringBuilder();
        xmlReader.readLog().forEach(sb::append);
        return sb;
    }
}