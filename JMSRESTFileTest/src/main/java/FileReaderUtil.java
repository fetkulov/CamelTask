

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by ildar.fetkulov on 1/31/2017.
 */
public class FileReaderUtil {

    static String readFile(String filename) {

        BufferedReader br = null;
        FileReader fr = null;
        StringBuilder fileContent = new StringBuilder();

        try {

            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String sCurrentLine;

            br = new BufferedReader(new FileReader(filename));

            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
                fileContent.append(sCurrentLine);
            }
            return fileContent.toString();

        } catch (IOException e) {

            e.printStackTrace();
            return "";

        }
    }
}
