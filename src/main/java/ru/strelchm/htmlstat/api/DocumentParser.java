package ru.strelchm.htmlstat.api;

import java.io.IOException;
import java.io.InputStream;

public interface DocumentParser {
    void parse(InputStream inputStream, String url) throws IOException;
}
