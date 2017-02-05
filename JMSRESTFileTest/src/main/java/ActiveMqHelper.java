import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.lang.StringUtils;

import javax.jms.*;

import static java.lang.Thread.sleep;

class ActiveMqHelper {

    static void sendMessage(String queueName, String headerKey, String headerValue, String message, String activeMqUrl) {
        try {
            sleep(1000);
            Session session = getSession(activeMqUrl);

            Queue queue = session.createQueue(queueName);
            //Added as a producer
            javax.jms.MessageProducer producer = session.createProducer(queue);
            // Create and send the message
            TextMessage msg = session.createTextMessage();
            msg.setStringProperty(headerKey, headerValue);
            msg.setText(message);
            producer.send(msg);
            System.out.println("Message sent -'" + msg.getText() + "'");

        } catch (Exception e) {
            System.out.println("Exception - " + e.getMessage());
        }
    }

    static String consumeMessage(String queueName, String activeMqUrl) {
        try {
            Session session = getSession(activeMqUrl);
            Destination destination = session.createQueue(queueName);

            MessageConsumer consumer = session.createConsumer(destination);
            Message message = consumer.receive();
            return ((ActiveMQTextMessage) message).getText();
        } catch (JMSException e) {
            return StringUtils.EMPTY;
        }
    }

    static Session getSession(String activeMqUrl) throws JMSException {
        //created ConnectionFactory object for creating connection
        ConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", activeMqUrl);
        //Establish the connection
        Connection connection = factory.createConnection();
        connection.start();
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

}
