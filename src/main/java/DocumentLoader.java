import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlParser {

    public static final String HTTPS_URL_START_PART = "https";
    public static final String HTTP_URL_START_PART = "http";
    public static final String LOCALHOST_URL_START_PART = "localhost";

    public InputStream getInputStreamFromUrl(String url) throws IOException {
        HttpURLConnection connection;
        URL targetUrl = new URL(url);

        if (url.startsWith(HTTPS_URL_START_PART)) {
            connection = (HttpsURLConnection) targetUrl.openConnection();
        } else if (url.startsWith(HTTP_URL_START_PART) || url.startsWith(LOCALHOST_URL_START_PART)) {
            connection = (HttpURLConnection) targetUrl.openConnection();
        } else {
            throw new UnsupportedOperationException();
        }

//        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return connection.getInputStream();
    }

    //    public void parse(BufferedReader reader) throws IOException {
//        String line;
//        boolean headParsed = false;
//
//        while ((line = reader.readLine()) != null) {
//            String trimmedString = line.trim();
//
//            if (!headParsed) {
//                if (trimmedString.contains("<body>") || trimmedString.contains("<body ")) {
//                    headParsed = true;
//                } else {
//                    continue;
//                }
//            }
//
//            if(trimmedString.contains("</body>")) {
//                break;
//            }
//
//            if (trimmedString.startsWith("<")) {
//                while (!trimmedString.endsWith("/>") && !trimmedString.endsWith(">")) {
//                    sb.append(string);
//                    string = r.readLine();
//                }
//                string = sb.toString() + string;
//                sb = new StringBuilder();
//            }
//
//            https://www.simbirsoft.com/
//
//            System.out.println(line);
////            words = line.split(regexPattern);
////            collectorService.collectData(words);
//
//        }
//    }
    public void parse(InputStream inputStream, String url) throws ParserConfigurationException, SAXException, IOException {
        Document doc = Jsoup.parse(inputStream, null, url);
        Elements newsHeadlines = doc.select("body");
        for (Element headline : newsHeadlines) {
            System.out.println(headline.text());
        }
    }
}
