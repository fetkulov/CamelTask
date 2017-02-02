package org.apache.camel.example.jmstorest;

import org.apache.camel.main.Main;

public final class UserMain {

    private UserMain() {
        // to comply with checkstyle rule
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        main.addRouteBuilder(new UserRouteBuilder());
        main.run();
    }
}