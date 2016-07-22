package de.unibi.cebitec.bibiserv.jobproxy.server.cli;

import org.jbehave.core.annotations.*;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by pbelmann on 30.06.16.
 */
public class CLISteps {

    private CLI cli;

    private Map<String, String> additionalCommands;

    private Thread cliThread;
    private Properties properties;

    private String updateCommand(String command) {
        for (String key : additionalCommands.keySet()) {
            command = command.replace(key, additionalCommands.get(key));
        }
        return command;
    }

    @BeforeStory
    public void beforeStory() {
        additionalCommands = new HashMap<>();
        properties = new Properties();
    }

    @Given("jobproxy is installed.")
    public void initJobProxy() {
        cli = new CLI(new VariableResult());
    }

    @Given("I create a file $file with the contents: $content")
    public void createFile(String fileName, String content) throws IOException {
        File file = null;
        try {
            file = File.createTempFile(fileName, "out");
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "utf-8"));
            writer.write(content);
            writer.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        properties.load(new FileInputStream(file));
        additionalCommands.put(fileName, file.getAbsolutePath());
    }

    @When("I run jobproxy with the parameters: $command")
    public void run(String command) {
        cli.run(updateCommand(command).trim().split(" "));
    }

    @When("I start the jobproxy server with the parameters: $command")
    public void start(String command) {
        Runnable cliRunnable = () -> {
            cli.run(updateCommand(command).trim().split(" "));
        };
        cliThread = new Thread(cliRunnable);
        cliThread.start();

        //wait until jobproxy is started
        Runnable webTargetRunnable = () -> {
            Client client = ClientBuilder.newClient();
            Response res = null;
            while (res == null) {
                try {
                    res = client.target(properties.getProperty("serveruri", "http://localhost:9999/")).path("/ping").request().get();
                } catch (ProcessingException e) {
                    //ProcessingException is thrown until server is available
                }
            }
        };
        Thread webtargetThread = new Thread(webTargetRunnable);
        webtargetThread.start();
        try {
            webtargetThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("the output should be $output")
    public void testOutput(String output) {
        VariableResult result = (VariableResult) cli.getResult();
        assertEquals(output, result.getMessage());
    }

    @Then("the output should contain the values $output")
    public void testOutputWithMultipleValues(String valuesStr) {
        VariableResult result = (VariableResult) cli.getResult();
        String[] valueArr = valuesStr.trim().split(" ");
        Arrays.asList(valueArr).forEach(value -> {
                    assertTrue(result.getMessage().contains(value));
                }
        );
    }

    @Then("the exit status should be $exit")
    public void testExit(int exit) {
        VariableResult result = (VariableResult) cli.getResult();
        assertEquals(exit, result.getExit());
    }

    @Then("the GET request using the url $request should be successful")
    public void testIsAlive(String request) {
        String path = request.substring(request.lastIndexOf("/"));
        String url = request.replace(path, "");
        Client client = ClientBuilder.newClient();
        Response res = client.target(url).path(path).request().get();
        assertEquals(Response.Status.Family.SUCCESSFUL, res.getStatusInfo().getFamily());
    }

    @AfterStory
    public void afterStory() {
        if (cliThread != null) {
            cliThread.stop();
        }
        properties.clear();
    }
}
