package ru.strelchm.htmlstat.parser;

import ru.strelchm.htmlstat.api.DocumentLoader;
import ru.strelchm.htmlstat.api.DocumentStatProcessingException;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlDocumentLoader implements DocumentLoader {

    public static final String HTTPS_URL_START_PART = "https";
    public static final String HTTP_URL_START_PART = "http";
    public static final String LOCALHOST_URL_START_PART = "localhost";

    public InputStream getInputStreamFromUrl(String url) throws IOException, DocumentStatProcessingException {
        HttpURLConnection connection;
        URL targetUrl = new URL(url);

        if (url.startsWith(HTTPS_URL_START_PART)) {
            connection = (HttpsURLConnection) targetUrl.openConnection();
        } else if (url.startsWith(HTTP_URL_START_PART) || url.startsWith(LOCALHOST_URL_START_PART)) {
            connection = (HttpURLConnection) targetUrl.openConnection();
        } else {
            throw new DocumentStatProcessingException("Can't determine connection type for url '" + url + "'");
        }

        return connection.getInputStream();
    }
}
