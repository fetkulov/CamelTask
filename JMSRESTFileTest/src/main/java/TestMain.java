import javax.jms.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.Thread.sleep;


public class TestMain {

    private static final String QUEUE_UNDELIVERED = "queue.undelivered";
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

        System.out.println("Correct test press 1 ");
        System.out.println("Undelivered test press 2 ");

        Scanner scan = new Scanner(System.in);
        String s = scan.next();

        if ("1".equals(s)) {
            StartUtils.startActiveMQ(testDir);
            StartUtils.startJmsToRest(testDir);
            StartUtils.startRestToFile(testDir);

            sendMessage();

            System.out.println("File equals sent message - " + checkSavedFiles());
        } else if ("2".equals(s)) {
            StartUtils.startActiveMQ(testDir);
            StartUtils.startJmsToRest(testDir);
            sendMessage();

            System.out.println("Undelivered message - " + consumeMessage());


        } else {
            System.out.println("Incorrect input! ");
        }

    }

    private static boolean checkSavedFiles() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            sleep(10000);
            String content = FileReaderUtil.readFile(testDir + "\\..\\test\\ResultFile-1.txt");
            JsonNode jsonNodeFromFile = mapper.readTree(content);
            JsonNode jsonNode = mapper.readTree(MESSAGE1);
            ((ObjectNode) jsonNode).put(TEST_HEADER_KEY, TEST_HEADER_PROPERTY);
            return ((ObjectNode) jsonNode).equals((ObjectNode) jsonNodeFromFile);
        } catch (IOException ioExc) {

            System.out.println("Test Failed");
            return false;
        }catch (InterruptedException e){
            System.out.println("Test Failed");
            return false;
        }
    }

    private static void sendMessage() {
        try {
            sleep(1000);
            Session session = getSession();

            Queue queue = session.createQueue(QUEUE_NAME);
            //Added as a producer
            javax.jms.MessageProducer producer = session.createProducer(queue);
            // Create and send the message
            TextMessage msg = session.createTextMessage();
            msg.setStringProperty(TEST_HEADER_KEY, TEST_HEADER_PROPERTY);
            msg.setText(MESSAGE1);
            producer.send(msg);
            System.out.println("Message sent -'" + msg.getText() + "'");

        } catch (Exception e) {
            System.out.println("Exception - " + e.getMessage());
        }
    }

    private static String consumeMessage() {
        try {
            Session session = getSession();
            Destination destination = session.createQueue(QUEUE_UNDELIVERED);

            MessageConsumer consumer = session.createConsumer(destination);
            Message message = consumer.receive();
            return ((ActiveMQTextMessage) message).getText();
        } catch (JMSException e) {
            return StringUtils.EMPTY;
        }
    }

    private static Session getSession() throws JMSException {
        //created ConnectionFactory object for creating connection
        ConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", URL);
        //Establish the connection
        Connection connection = factory.createConnection();
        connection.start();
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }


}
