package com.dn.commands;

import com.dn.model.Document;
import com.dn.service.LogAnalyser;

import java.util.regex.Pattern;

public class DocumentCommand extends PatternCommand {
    private static final String REGEX = "^(?<occurredAt>.{23}).+\\[(?<documentId>\\d+),\\s(?<documentPage>\\d)\\]";

    public DocumentCommand(PatternCommand nextCommand) {
        super(nextCommand);
        this.pattern = Pattern.compile(REGEX);
    }

    @Override
    void process(LogAnalyser logAnalyser) {
        Document document = new Document(Integer.parseInt(matcher.group("documentId")),
                Integer.parseInt(matcher.group("documentPage")),
                matcher.group("occurredAt"));
        logAnalyser.setCurrentDocument(document);
    }
}
