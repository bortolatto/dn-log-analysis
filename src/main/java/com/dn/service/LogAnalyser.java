package com.dn.service;

import com.dn.commands.PatternCommand;
import com.dn.model.Document;
import com.dn.model.Rendering;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LogAnalyser {
    private final Path path;
    private final PatternCommand patternCommand;
    @Setter
    @Getter
    private Document currentDocument;
    @Getter
    private List<Rendering> eventRenderings = new ArrayList<>();

    public LogAnalyser(Path path, PatternCommand patternCommand) {
        this.path = path;
        this.patternCommand = patternCommand;
    }

    public void scan(List<String> lines) {
        lines.forEach(line -> patternCommand.execute(this, line));
    }

    public List<String> readLog() throws IOException {
        try (BufferedReader bf = new BufferedReader(new FileReader(path.toFile()))) {
            return bf.lines().collect(Collectors.toList());
        }
    }
}
