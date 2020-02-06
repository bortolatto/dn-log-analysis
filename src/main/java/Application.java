import com.dn.commands.DocumentCommand;
import com.dn.commands.GetEventBuilder;
import com.dn.commands.StartingEventCommand;
import com.dn.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException, XMLStreamException {
        StartingEventCommand startingEventCommand = new StartingEventCommand(null);
        DocumentCommand documentCommand = new DocumentCommand(startingEventCommand);

        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: java -jar app.jar filename");
        }
        if (!Paths.get(args[0]).toFile().exists()) {
            throw new FileNotFoundException("File not found: " + args[0]);
        }

        Path path = Paths.get(args[0]);

        LogAnalyser logAnalyser = new LogAnalyser(path, documentCommand);
        LOGGER.info("Reading file {}", path);
        List<String> lines = logAnalyser.readLog();
        LOGGER.info("Scanning file...");
        logAnalyser.scan(lines);

        LOGGER.info("Analysing startRendering events...");
        EventsExtractor startingRenderingEvents = new EventsExtractor(new StartingEventCommand(null));
        startingRenderingEvents.process(lines);
        LOGGER.info("Analysing getRendering events...");
        EventsExtractor getEvents = new EventsExtractor(new GetEventBuilder());
        getEvents.process(lines);

        LOGGER.info("Merging documents and events...");
        MergeEventsToDocuments persistEventsToRenderings = new MergeEventsToDocuments();
        persistEventsToRenderings.merge(logAnalyser.getEventRenderings(), startingRenderingEvents.getEvents());
        persistEventsToRenderings.merge(logAnalyser.getEventRenderings(), getEvents.getEvents());

        LOGGER.info("Generating summary...");
        SummaryService summary = new SummaryService(logAnalyser.getEventRenderings(),
                startingRenderingEvents.getEvents().keySet(),
                getEvents.getEvents().keySet());

        String targetFile = path.getFileName().toString() + ".xml";
        LOGGER.info("Persisting content into XML file named {}", targetFile);
        XMLWriter xmlWriter = new XMLWriter(targetFile);
        xmlWriter.write(summary.getDistinctRenderings(), summary.getSummary());

        LOGGER.info("Success!");
    }
}
