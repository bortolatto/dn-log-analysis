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

    public XMLWriter(String fileName) {
        this.fileName = fileName;
    }

    public void write(List<Rendering> renderingList, Summary summary) throws XMLStreamException, FileNotFoundException {
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream(fileName), "UTF-8");
        try {
            startDocument(xmlStreamWriter);
            writeRenderingEvents(renderingList, xmlStreamWriter);
            writeSummary(summary, xmlStreamWriter);
            finalizeDocument(xmlStreamWriter);
        } catch (XMLStreamException e) {
            LOGGER.error(e.getMessage());
        } finally {
            xmlStreamWriter.close();
        }
    }

    private void startDocument(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
        xmlStreamWriter.writeCharacters(LEVEL_ONE);
        xmlStreamWriter.writeStartElement("report");
    }

    private void finalizeDocument(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeCharacters(LEVEL_ONE);
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.flush();
    }

    private void writeRenderingEvents(List<Rendering> renderingList, XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        for (Rendering rendering : renderingList) {
            xmlStreamWriter.writeCharacters(LEVEL_TWO);
            xmlStreamWriter.writeStartElement("rendering");

            writeInnerElement(xmlStreamWriter, "document", rendering.getDocument().getId() + "");
            writeInnerElement(xmlStreamWriter, "page", rendering.getDocument().getPageNumber() + "");
            writeInnerElement(xmlStreamWriter, "uid", rendering.getUid());

            for (XEvent event : rendering.getEvents()) {
                writeInnerElement(xmlStreamWriter, event.getType().value(), event.getOccurredAt());
            }
            xmlStreamWriter.writeCharacters(LEVEL_TWO);
            xmlStreamWriter.writeEndElement();
        }
    }

    private void writeSummary(Summary summary, XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeCharacters(LEVEL_TWO);
        xmlStreamWriter.writeStartElement("summary");
        writeInnerElement(xmlStreamWriter, "count", summary.getTotalRenderings() + "");
        writeInnerElement(xmlStreamWriter, "duplicates", summary.getDuplicateRenderings() + "");
        writeInnerElement(xmlStreamWriter, "unnecessary", summary.getOrphanRenderings() + "");
        xmlStreamWriter.writeCharacters(LEVEL_TWO);
        xmlStreamWriter.writeEndElement();
    }

    private void writeInnerElement(XMLStreamWriter xmlStreamWriter, String document, String data) throws XMLStreamException {
        xmlStreamWriter.writeCharacters(LEVEL_THREE);
        xmlStreamWriter.writeStartElement(document);
        xmlStreamWriter.writeCharacters(data);
        xmlStreamWriter.writeEndElement();
    }
}
