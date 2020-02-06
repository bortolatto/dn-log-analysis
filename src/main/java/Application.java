import com.dn.commands.DocumentCommand;
import com.dn.commands.GetEventBuilder;
import com.dn.commands.StartingEventCommand;
import com.dn.service.*;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Application {
    public static void main(String[] args) throws IOException, XMLStreamException {
        StartingEventCommand startingEventCommand = new StartingEventCommand(null);
        DocumentCommand documentCommand = new DocumentCommand(startingEventCommand);

        LogAnalyser logAnalyser = new LogAnalyser(Paths.get("/Users/bortolatto/Downloads/server.log"), documentCommand);
        List<String> lines = logAnalyser.readLog();
        logAnalyser.scan(lines);

        EventsExtractor startingRenderingEvents = new EventsExtractor(new StartingEventCommand(null));
        startingRenderingEvents.process(lines);

        EventsExtractor getEvents = new EventsExtractor(new GetEventBuilder());
        getEvents.process(lines);

        PersistEventsToRenderings persistEventsToRenderings = new PersistEventsToRenderings();
        persistEventsToRenderings.persist(logAnalyser.getEventRenderings(), startingRenderingEvents.getEvents());
        persistEventsToRenderings.persist(logAnalyser.getEventRenderings(), getEvents.getEvents());

        logAnalyser.getEventRenderings().forEach(System.out::println);

        SummaryService summary = new SummaryService(logAnalyser.getEventRenderings(),
                startingRenderingEvents.getEvents().keySet(),
                getEvents.getEvents().keySet());
        XMLWriter xmlWriter = new XMLWriter();
        xmlWriter.write(summary.getDistinctRenderings(), summary.getSummary());
    }
}
