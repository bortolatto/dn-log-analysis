package com.dn.service;

import com.dn.commands.DocumentCommand;
import com.dn.commands.StartingEventCommand;
import com.dn.model.Document;
import com.dn.model.Rendering;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class LogAnalyserTest {
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
    public void shouldHaveOneRendering() throws IOException {
        LogAnalyser logAnalyser = new LogAnalyser(path, documentCommand);
        List<String> logFile = logAnalyser.readLog();
        logAnalyser.scan(logFile);

        Document document = new Document(114466, 0, "WorkerThread-17");
        Rendering rendering = new Rendering(document, "1286373785873-3536");

        Assertions.assertThat(logAnalyser.getEventRenderings()).contains(rendering);
    }

    @Test
    public void shouldReadFileWithTenLines() throws IOException {
        LogAnalyser logAnalyser = new LogAnalyser(path, documentCommand);
        List<String> logFile = logAnalyser.readLog();

        Assertions.assertThat(logFile.size()).isEqualTo(10);
    }
}