package com.dn.commands;

import com.dn.model.Document;
import com.dn.service.LogAnalyser;

import java.util.regex.Pattern;

public class DocumentCommand extends PatternCommand {
    private static final String REGEX = ".+\\[(WorkerThread-\\d+)\\].+Executing request startRendering.+\\[(\\d+).+(\\d+)\\]";

    public DocumentCommand(PatternCommand nextCommand) {
        super(nextCommand);
        this.pattern = Pattern.compile(REGEX);
    }

    @Override
    void process(LogAnalyser logAnalyser) {
        Document document = new Document(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), matcher.group(1));
        logAnalyser.setCurrentDocument(document);
    }
}
