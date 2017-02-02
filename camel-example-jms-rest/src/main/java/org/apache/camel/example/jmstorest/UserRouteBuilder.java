package org.apache.camel.example.jmstorest;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.http.common.HttpOperationFailedException;

import javax.jms.ConnectionFactory;
import java.net.ConnectException;
import java.util.Map;

public class UserRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");

        getContext().addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        from("test-jms:queue:test.queue")
                .log("\\nRetrieve JMS message: ${body}")
                .process((exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    Map<String, Object> headers = exchange.getIn().getHeaders();
                    String testHeaderValue = (String)headers.get("testHeader");
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(body);
                    ((ObjectNode)jsonNode).put("testHeader", testHeaderValue);
                    exchange.getOut().setBody(jsonNode.toString());
                    //also possible provide validation

                }))
                .log("\\nAfter processing: ${body}")
                .to("direct:correct");

                from("direct:correct")
                .log("\\nSend message: ${body}")
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .doTry()
                    .to("http://localhost:8080/user/post")
                .doCatch(ConnectException.class)
                   .log("ConnectException ${body}")
                   .to("test-jms:queue.undelivered")
                .doCatch(HttpOperationFailedException.class)
                   .log("HttpOperationFailedException ${body}")
                   .to("test-jms:queue.undelivered")
                .endDoTry();
    }
}
