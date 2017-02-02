package org.apache.camel.example.resttofile;

import org.apache.camel.main.Main;

public final class UserMain {

    private UserMain() {
        // to comply with checkstyle rule
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.bind("userService", new UserService());
        main.addRouteBuilder(new UserRouteBuilder());
        main.run();
    }
}
