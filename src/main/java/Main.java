import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    public static final Logger LOGGER = Logger.getGlobal();
    
    public static void main(String[] args) {
        String url = "https://www.simbirsoft.com/";

        try {
            UrlParser parser = new UrlParser();
            parser.parse(parser.getInputStreamFromUrl(url));
        } catch (Exception exception) {
            LOGGER.severe(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
