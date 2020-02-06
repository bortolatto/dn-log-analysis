package com.dn.service;

import com.dn.model.Rendering;
import com.dn.model.Summary;
import com.dn.model.XEvent;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class XMLWriter {
    public void write(List<Rendering> renderingList, Summary summary) throws XMLStreamException, FileNotFoundException {
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream("teste.xml"), "UTF-8");
        try {
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
            xmlStreamWriter.writeCharacters("\n");
            xmlStreamWriter.writeStartElement("report");

            for (Rendering rendering : renderingList) {
                xmlStreamWriter.writeCharacters("\n\t");
                xmlStreamWriter.writeStartElement("rendering");

                writeInnerElement(xmlStreamWriter, "document", rendering.getDocument().getId() + "");
                writeInnerElement(xmlStreamWriter, "page", rendering.getDocument().getPageNumber() + "");
                writeInnerElement(xmlStreamWriter, "uid", rendering.getUid());

                for (XEvent event : rendering.getEvents()) {
                    writeInnerElement(xmlStreamWriter, event.getType().value(), event.getOccurredAt());
                }
                xmlStreamWriter.writeCharacters("\n\t");
                xmlStreamWriter.writeEndElement();
            }

            // Summary
            xmlStreamWriter.writeCharacters("\n\t");
            xmlStreamWriter.writeStartElement("summary");
            writeInnerElement(xmlStreamWriter, "count", summary.getTotalRenderings() + "");
            writeInnerElement(xmlStreamWriter, "duplicates", summary.getDuplicateRenderings() + "");
            writeInnerElement(xmlStreamWriter, "unnecessary", summary.getOrphanRenderings() + "");
            xmlStreamWriter.writeCharacters("\n\t");
            xmlStreamWriter.writeEndElement();

            xmlStreamWriter.writeCharacters("\n");
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } finally {
            xmlStreamWriter.close();
        }
    }

    private void writeInnerElement(XMLStreamWriter xmlStreamWriter, String document, String data) throws XMLStreamException {
        xmlStreamWriter.writeCharacters("\n\t\t");
        xmlStreamWriter.writeStartElement(document);
        xmlStreamWriter.writeCharacters(data);
        xmlStreamWriter.writeEndElement();
    }
}
