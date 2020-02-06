package com.dn.service;

import com.dn.model.Rendering;
import com.dn.model.Summary;
import com.dn.model.XEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class XMLWriter {
    public static final String LEVEL_ONE = "\n";
    public static final String LEVEL_TWO = "\n\t";
    public static final String LEVEL_THREE = "\n\t\t";
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLWriter.class);
    private final String fileName;
    private XMLStreamWriter xmlStreamWriter;

    public XMLWriter(String fileName) {
        this.fileName = fileName;
    }

    public void write(List<Rendering> renderingList, Summary summary) throws XMLStreamException, FileNotFoundException {
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream(fileName), "UTF-8");
        try {
            startDocument();
            writeRenderingEvents(renderingList);
            writeSummary(summary);
            finalizeDocument();
        } catch (XMLStreamException e) {
            LOGGER.error(e.getMessage());
        } finally {
            xmlStreamWriter.close();
        }
    }

    private void startDocument() throws XMLStreamException {
        xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
        xmlStreamWriter.writeCharacters(LEVEL_ONE);
        xmlStreamWriter.writeStartElement("report");
    }

    private void writeRenderingEvents(List<Rendering> renderingList) throws XMLStreamException {
        for (Rendering rendering : renderingList) {
            xmlStreamWriter.writeCharacters(LEVEL_TWO);
            xmlStreamWriter.writeStartElement("rendering");

            writeInnerElement("document", rendering.getDocument().getId() + "");
            writeInnerElement("page", rendering.getDocument().getPageNumber() + "");
            writeInnerElement("uid", rendering.getUid());

            for (XEvent event : rendering.getEvents()) {
                writeInnerElement(event.getType().value(), event.getOccurredAt());
            }
            xmlStreamWriter.writeCharacters(LEVEL_TWO);
            xmlStreamWriter.writeEndElement();
        }
    }

    private void writeSummary(Summary summary) throws XMLStreamException {
        xmlStreamWriter.writeCharacters(LEVEL_TWO);
        xmlStreamWriter.writeStartElement("summary");
        writeInnerElement("count", summary.getTotalRenderings() + "");
        writeInnerElement("duplicates", summary.getDuplicateRenderings() + "");
        writeInnerElement("unnecessary", summary.getOrphanRenderings() + "");
        xmlStreamWriter.writeCharacters(LEVEL_TWO);
        xmlStreamWriter.writeEndElement();
    }

    private void finalizeDocument() throws XMLStreamException {
        xmlStreamWriter.writeCharacters(LEVEL_ONE);
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.flush();
    }
    
    private void writeInnerElement(String document, String data) throws XMLStreamException {
        xmlStreamWriter.writeCharacters(LEVEL_THREE);
        xmlStreamWriter.writeStartElement(document);
        xmlStreamWriter.writeCharacters(data);
        xmlStreamWriter.writeEndElement();
    }
}
