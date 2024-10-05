import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class readFiles {



    public String getData(String filepath) {
        String data = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            // Read each line from the file and print it
            while ((line = br.readLine()) != null) {
                data += line +"\n";
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        return data;
    }
}
