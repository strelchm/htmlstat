package ru.strelchm.htmlstat.parser;

import ru.strelchm.htmlstat.api.HtmlParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Deprecated
public class BufferedStreamHtmlParser implements HtmlParser {
    @Override
    @Deprecated
    public void parse(InputStream inputStream, String url) throws IOException {
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        boolean headParsed = false;


        while ((line = reader.readLine()) != null) {
            String trimmedString = line.trim();

            if (!headParsed) {
                if (trimmedString.contains("<body>") || trimmedString.contains("<body ")) {
                    headParsed = true;
                } else {
                    continue;
                }
            }

            if (trimmedString.contains("</body>")) {
                break;
            }

            if (trimmedString.startsWith("<")) {
                // todo
            }

            System.out.println(line);
        }
    }
}
