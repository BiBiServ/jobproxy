package de.unibi.cebitec.bibiserv.jobproxy.server.cli;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyFactory;
import de.unibi.cebitec.bibiserv.jobproxy.server.JobProxyServer;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import org.apache.commons.cli.*;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;

public class CLI {

    private static Logger ROOT_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    private static String OPTION_FRAMEWORK_SELECT = "f";

    private static String OPTION_FRAMEWORK_LIST = "l";

    private static String OPTION_FRAMEWORK_PROPERTIES = "p";

    private static String OPTION_FRAMEWORK_DEMONISE = "d";

    private static String OPTION_FRAMEWORK_DEBUG = "debug";

    private static String OPTION_FRAMEWORK_LOG = "log";

    private static String OPTION_FRAMEWORK_HELP = "h";

    private org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CLI.class);

    private Result result;

    public CLI() {
        result = new SystemResult();
    }

    public CLI(Result result) {
        this.result = result;
    }

    private void listFrameworks() {
        StringBuilder sb = new StringBuilder();
        sb.append("Following frameworks are available:\n\n");
        boolean first = true;
        for (String n : JobProxyFactory.list()) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append(n);
        }
        result.message(sb.toString());
        result.exit(0);
    }

    private void handleServer(JobProxyServer server, boolean isDebug, boolean isDaemon) {
        server.startServer(isDebug);
        if (isDaemon) {
            LOGGER.info(String.format("Server run on %s !", server.getURI()));
            try {
                Thread.currentThread().join();
            } catch (InterruptedException ex) {
                LOGGER.error(ex.getMessage());
            }
        } else {
            LOGGER.info(String.format("Server run on %s ! Press key to stop service.", server.getURI()));
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            server.stopServer();
        }
    }

    public void run(String args[]) {
        Options options = new Options();

        Option frameworkOption = new Option(OPTION_FRAMEWORK_SELECT, true, "Framework/Plugin to be used by JobProxy.");
        frameworkOption.setRequired(false);

        Option listFrameworkOption = new Option(OPTION_FRAMEWORK_LIST, false, "List all available frameworks/plugins.");
        listFrameworkOption.setRequired(false);

        Option propertiesOption = new Option(OPTION_FRAMEWORK_PROPERTIES, true, "Configuration file (java properties style)");
        propertiesOption.setRequired(false);

        Option demoniseOption = new Option(OPTION_FRAMEWORK_DEMONISE, false, "start server in daemon mode");
        propertiesOption.setRequired(false);

        Option debugOption = new Option(OPTION_FRAMEWORK_DEBUG, true, "run server in debug mode. Logs all http request/responses. ");
        propertiesOption.setRequired(false);

        String[] levels = {Level.ERROR.toString(), Level.INFO.toString(), Level.TRACE.toString(), Level.WARN.toString(),
                Level.DEBUG.toString(), Level.ALL.toString(), Level.OFF.toString()};
        Option loggingOption = new Option(OPTION_FRAMEWORK_LOG, true, "Set Logging Level. Available Levels:" + String.join(",", levels));
        loggingOption.setRequired(false);

        Option helpOption = new Option(OPTION_FRAMEWORK_HELP, false, "Print general help or help for a specified framework together with '-f' option");

        propertiesOption.setRequired(false);

        options.addOption(frameworkOption);
        options.addOption(listFrameworkOption);
        options.addOption(propertiesOption);
        options.addOption(demoniseOption);
        options.addOption(debugOption);
        options.addOption(loggingOption);
        options.addOption(helpOption);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            Level level = Optional.ofNullable(cmd.getOptionValue(OPTION_FRAMEWORK_LOG))
                    .map(levelStr -> Level.toLevel(levelStr)).orElse(Level.ALL);

            ROOT_LOGGER.setLevel(level);
            if (cmd.hasOption(OPTION_FRAMEWORK_LIST)) {
                listFrameworks();
                return;
            }

            if (cmd.hasOption(OPTION_FRAMEWORK_SELECT)) {
                String frameworkType = cmd.getOptionValue(OPTION_FRAMEWORK_SELECT);
                Properties prop = new Properties();
                if (cmd.hasOption(OPTION_FRAMEWORK_PROPERTIES)) {
                    try {
                        prop.load(new FileInputStream(new File(cmd.getOptionValue(OPTION_FRAMEWORK_PROPERTIES))));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                }
                try {
                    JobProxyServer server = new JobProxyServer(frameworkType, prop);
                    if (cmd.hasOption(OPTION_FRAMEWORK_HELP)) {
                        result.message(JobProxyFactory.getFramework().help());
                        return;
                    } else {
                        handleServer(server, cmd.hasOption(OPTION_FRAMEWORK_DEBUG), cmd.hasOption(OPTION_FRAMEWORK_DEMONISE));
                    }
                } catch (FrameworkException e) {
                    LOGGER.error(e.getMessage());
                }
            } else {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar JobProxy.jar", options);
            }
        } catch (ParseException e) {
            LOGGER.error("Parsing failed.  Reason: " + e.getMessage());
        }
    }

    public static void main(String args[]) {

        CLI cli = new CLI();
        cli.run(args);
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
