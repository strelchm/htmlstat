package ru.strelchm.htmlstat.api;

import java.io.IOException;
import java.io.InputStream;

public interface DocumentLoader {
    InputStream getInputStreamFromUrl(String url) throws IOException;
}
