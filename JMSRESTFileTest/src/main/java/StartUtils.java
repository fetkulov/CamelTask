import java.io.File;
import java.io.IOException;

class StartUtils {

    static void startActiveMQ(String currentDir) {
        try {
            startProcess("cmd /c activemq.bat", currentDir + "\\..\\..\\apache-activemq-5.9.0\\bin\\");
            System.out.println("ActiveMQ started");
        } catch (IOException e) {
            System.out.println("ActiveMQ wasn't started");
        }
    }

    static void startJmsToRest(String currentDir) {
        try {
            startProcess("java -jar camel-example-jms-rest-2.18.1.jar", currentDir + "\\..\\");
            System.out.println("JmsToRest started");
        } catch (IOException e) {
            System.out.println("JmsToRest wasn't started");
        }
    }

    static void startRestToFile(String currentDir) {
        try {
            startProcess("java -jar camel-example-rest-file-2.18.2.jar", currentDir + "\\..\\");
            System.out.println("RestToFile started");
        } catch (IOException e) {
            System.out.println("RestToFile wasn't started");
        }
    }

    private static void startProcess(String command, String path) throws IOException {
        Process p = Runtime.getRuntime().exec(command, null, new File(path));
    }
}
