import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class Application {
    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(String.valueOf(Paths.get("/Users/bortolatto/Downloads/server.log"))));
        String line = "";
        while((line = bf.readLine()) != null) {
            System.out.println(line);
        }
    }
}
