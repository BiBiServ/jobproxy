package de.unibi.cebitec.bibiserv.jobproxy.server.cli;

import de.unibi.cebitec.bibiserv.jobproxy.server.JobProxyServer;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class CLI {

    static final Logger logger = LoggerFactory.getLogger(JobProxyServer.class);

    public static void main(String args[]){

        Options options = new Options();

        Option zookeeperOption = new Option("z", true, "Please provide the zookeeper url and port: '<URL>':'<PORT>'");
        zookeeperOption.setRequired(true);

        Option frameworkOption = new Option("f", true, "Please provide your framework: JavaDocker, Chronos or DRMAA");
        frameworkOption.setRequired(true);

        options.addOption(zookeeperOption);
        options.addOption(frameworkOption);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse( options, args);
            String zookeeperUrl = cmd.getOptionValue("z");
            String frameworkType = cmd.getOptionValue("f");

            if(zookeeperUrl != null || frameworkType != null) {
                URI serveruri = new URI("http://localhost:9999/");
                JobProxyServer server = new JobProxyServer(serveruri);
                server.startServer(zookeeperUrl, frameworkType);

                logger.info(String.format("Server run on %s ! Press key to stop service.", zookeeperUrl));
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
                server.stopServer();
            }
        } catch (ParseException e) {
            System.err.println( "Parsing failed.  Reason: " + e.getMessage() );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
