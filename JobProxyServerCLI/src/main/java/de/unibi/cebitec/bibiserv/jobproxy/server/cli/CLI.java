package de.unibi.cebitec.bibiserv.jobproxy.server.cli;

import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyFactory;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import de.unibi.cebitec.bibiserv.jobproxy.server.JobProxyServer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Scanner;

public class CLI {

    static final Logger LOGGER = LoggerFactory.getLogger(JobProxyServer.class);

    public static void main(String args[]){

        Options options = new Options();

        Option frameworkOption = new Option("f", true, "Framework/Plugin to be used by JobProxy.");
        frameworkOption.setRequired(false);
        
        Option listFrameworkOption = new Option("l",false,"List all available frameworks/plugins.");
        listFrameworkOption.setRequired(false);
        
        Option propertiesOption = new Option("p",true,"Configuration file (java properities style)");
        propertiesOption.setRequired(false);
        
        Option helpOption = new Option("h",false,"Print general help or help for a specified framework together with '-f' option");
        propertiesOption.setRequired(false);
        
        options.addOption(frameworkOption);
        options.addOption(listFrameworkOption);
        options.addOption(propertiesOption);
        options.addOption(helpOption);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("l")) {
                // list all frameworks
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
                System.out.println(sb.toString());
               // and exit 
               System.exit(0);
            } 
            
            if (cmd.hasOption("f")) {
                String frameworkType = cmd.getOptionValue("f");          
                Properties prop = new Properties();
                if (cmd.hasOption("p")) {
                    try {
                        prop.load(new FileInputStream(new File(cmd.getOptionValue("p"))));
                    } catch (IOException e){
                        LOGGER.error(e.getMessage());
                    }
                }
                try {
                    JobProxyServer server = new JobProxyServer(frameworkType, prop);
                    if (cmd.hasOption("h")) {
                        System.out.println(JobProxyFactory.getFramework().help());
                    } else {
                        server.startServer();
                        LOGGER.info(String.format("Server run on %s ! Press key to stop service.",server.getURI()));
                        Scanner scanner = new Scanner(System.in);
                        scanner.nextLine();
                        server.stopServer();
                    }
                } catch (FrameworkException e) {
                    LOGGER.error(e.getMessage());   
                }
        } else {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "java -jar JobProxy.jar", options );
            }
        } catch (ParseException e) {
            LOGGER.error( "Parsing failed.  Reason: " + e.getMessage() );
        } 
    }

}
