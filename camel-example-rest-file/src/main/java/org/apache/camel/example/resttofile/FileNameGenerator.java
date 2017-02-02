package org.apache.camel.example.resttofile;

public class FileNameGenerator {

    private static int counter;

    public String generateFilename() {
        // compute the filename
        return "ResultFile-" + ++counter + ".txt";
    }

}
