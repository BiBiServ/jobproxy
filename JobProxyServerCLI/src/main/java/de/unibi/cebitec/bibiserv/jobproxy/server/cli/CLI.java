package de.unibi.cebitec.bibiserv.jobproxy.server.cli;


import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyFactory;
import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyServer;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class CLI {

    static final Logger LOGGER = LoggerFactory.getLogger(JobProxyServer.class);

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
        // and exit
        result.exit(0);
    }

    private void handleServer(JobProxyServer server, boolean isDebug, boolean isDaemon) {
        server.startServer(isDebug);
        if (isDaemon) {
            LOGGER.info(String.format("Server run on %s !",server.getURI()));
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

        Option frameworkOption = new Option("f", true, "Framework/Plugin to be used by JobProxy.");
        frameworkOption.setRequired(false);

        Option listFrameworkOption = new Option("l", false, "List all available frameworks/plugins.");
        listFrameworkOption.setRequired(false);

        Option propertiesOption = new Option("p", true, "Configuration file (java properities style)");
        propertiesOption.setRequired(false);
        
        Option demoniseOption = new Option("d",false,"start server in daemon mode");
        propertiesOption.setRequired(false);
        
        Option debugOption = new Option("debug",false,"run server in debug mode. Logs all http request/responses. ");
        propertiesOption.setRequired(false);
        
        Option helpOption = new Option("h",false,"Print general help or help for a specified framework together with '-f' option");

        propertiesOption.setRequired(false);

        options.addOption(frameworkOption);
        options.addOption(listFrameworkOption);
        options.addOption(propertiesOption);
        options.addOption(demoniseOption);
        options.addOption(debugOption);       
        options.addOption(helpOption);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("l")) {
                // list all frameworks
                listFrameworks();
                return;
            }

            if (cmd.hasOption("f")) {
                String frameworkType = cmd.getOptionValue("f");
                Properties prop = new Properties();
                if (cmd.hasOption("p")) {
                    try {
                        prop.load(new FileInputStream(new File(cmd.getOptionValue("p"))));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                }
                try {
                    JobProxyServer server = new JobProxyServer(frameworkType, prop);
                    if (cmd.hasOption("h")) {
                        result.message(JobProxyFactory.getFramework().help());
                        return;
                    } else {
                        handleServer(server, cmd.hasOption("debug"), cmd.hasOption("d"));
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
