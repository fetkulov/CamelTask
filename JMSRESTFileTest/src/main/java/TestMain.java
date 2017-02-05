import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.activemq.ActiveMQConnection;

import java.io.IOException;

import static java.lang.Thread.sleep;


public class TestMain {

    private static final String UNDELIVERED_QUEUE_NAME = "queue.undelivered";
    private static String URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String QUEUE_NAME = "test.queue";
    private static String MESSAGE1 = "{\n" +
            " \"myAction\": \"testAction\", \n" +
            " \"myBody\": \"testBody\"\n" +
            "}";
    private static String TEST_HEADER_KEY = "testHeader";
    private static String TEST_HEADER_PROPERTY = "testHeaderProperty";

    private static String testDir = System.getProperty("user.dir");


    public static void main(String args[]) {

        Process aciveMq = StartUtils.startActiveMQ(testDir);
        Process jmsToRest = StartUtils.startJmsToRest(testDir);
        ActiveMqHelper.sendMessage(QUEUE_NAME, TEST_HEADER_KEY, TEST_HEADER_PROPERTY, MESSAGE1, URL);

        System.out.println("Undelivered message - " + ActiveMqHelper.consumeMessage(UNDELIVERED_QUEUE_NAME, URL));

        Process restToFile = StartUtils.startRestToFile(testDir);

        ActiveMqHelper.sendMessage(QUEUE_NAME, TEST_HEADER_KEY, TEST_HEADER_PROPERTY, MESSAGE1, URL);

        System.out.println("File equals sent message - " + checkSavedFiles());

        aciveMq.destroy();
        jmsToRest.destroy();
        restToFile.destroy();// but some subprocesses on Win systems will not be destroyed

    }

    private static boolean checkSavedFiles() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            sleep(10000);
            String content = FileReaderUtil.readFile(testDir + "\\..\\test\\ResultFile-1.txt");
            JsonNode jsonNodeFromFile = mapper.readTree(content);
            JsonNode jsonNode = mapper.readTree(MESSAGE1);
            ((ObjectNode) jsonNode).put(TEST_HEADER_KEY, TEST_HEADER_PROPERTY);
            return  jsonNode.equals(jsonNodeFromFile);
        } catch (IOException ioExc) {

            System.out.println("Test Failed");
            return false;
        } catch (InterruptedException e) {
            System.out.println("Test Failed");
            return false;
        }
    }


}
