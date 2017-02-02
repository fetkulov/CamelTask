package org.apache.camel.example.resttofile;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.language.bean.BeanLanguage;
import org.apache.camel.model.rest.RestBindingMode;


public class UserRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // configure we want to use restleton port 8080 as the component for the rest DSL
        // and for the swagger api-doc as well
        restConfiguration().component("restlet").apiContextPath("api-doc")
                .port(8080)
                // and we enable json binding mode
                .bindingMode(RestBindingMode.json)
                // and output using pretty print
                .dataFormatProperty("prettyPrint", "true");

        // this user REST service is json only
        rest("/user").consumes("application/json").produces("application/json")

                .get("/listPojos").outTypeList(MyPojo.class)
                .to("bean:userService?method=listPojos")

                .post("/post").type(MyPojo.class)
                .to("direct:sayHello");


        from("direct:sayHello").log("got a new request")
                .log("Receive ${body}")
                .process((exchange) -> {

                            MyPojo myPojo = exchange.getIn().getBody(MyPojo.class);
                            exchange.getOut().setBody(myPojo.toString());
                        }
                )
                .setHeader(Exchange.FILE_NAME, BeanLanguage.bean(FileNameGenerator.class, "generateFilename"))
                .to("file://test");
    }

}
