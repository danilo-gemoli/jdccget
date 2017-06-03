package jdcc.settings.parser;

import jdcc.exceptions.SettingsParsingException;
import jdcc.logger.JdccLogger;
import jdcc.settings.Dictionary;
import jdcc.settings.entries.SettingsEntry;

import org.apache.commons.cli.*;

import java.util.List;

public class SimpleCmdSettingsParser extends AbstractSettingsParser
        implements CommandLineSettingsParser {
    private String[] cmdArgs;
    private Options options;
    private CommandLineParser cmdParser;
    private CommandLine commandLine;

    public SimpleCmdSettingsParser() {
        super();
        options = new Options();
        cmdParser = new DefaultParser();
        buildOptions();
    }

    @Override
    public void setCommandLineArgs(String[] args) {
        this.cmdArgs = args;
    }

    @Override
    public List<SettingsEntry> parse() throws SettingsParsingException {
        doParse();
        makeSettingsEntries();
        return settingsEntries;
    }

    private void doParse() throws SettingsParsingException {
        try {
            commandLine = cmdParser.parse(options, cmdArgs);
        } catch (ParseException e) {
            JdccLogger.logger.error("SimpleCmdSettingsParser: parse error", e);
            throw new SettingsParsingException(e.getMessage());
        }
    }

    private void makeSettingsEntries() {
        SettingsEntry entry = new SettingsEntry();
        for (Option opt : commandLine.getOptions()) {
            entry.name = opt.getOpt();
            entry.value = getOptValue(opt);
        }
        settingsEntries.add(entry);
    }

    private String getOptValue(Option opt) {
        if (opt.hasArg()) {
            return opt.getValue();
        } else if (opt.hasArgs()) {
            String[] args = opt.getValues();
            String result = String.join(" ", args);
            return result;
        }
        return "";
    }

    private void buildOptions() {
        for (Dictionary dict : Dictionary.values()) {
            Option opt = Option.builder(dict.getCmdOptName())
                    .longOpt(dict.getCmdLongOptName())
                    .desc(dict.getCmdDescription())
                    .hasArg(true)
                    .build();
            options.addOption(opt);
        }
    }
}
