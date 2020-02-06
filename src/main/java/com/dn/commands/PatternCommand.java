package com.dn.commands;

import com.dn.service.LogAnalyser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PatternCommand {
    protected PatternCommand nextCommand;
    protected Pattern pattern;
    protected Matcher matcher;

    protected PatternCommand(PatternCommand nextCommand) {
        this.nextCommand = nextCommand;
    }

    boolean matches(String line) {
        matcher = pattern.matcher(line);
        return matcher.find();
    }

    public void execute(LogAnalyser logAnalyser, String line) {
        if (matches(line)) {
            process(logAnalyser);
        } else if (nextCommand != null) {
            nextCommand.execute(logAnalyser, line);
        }
    }

    abstract void process(LogAnalyser logAnalyser);
}
