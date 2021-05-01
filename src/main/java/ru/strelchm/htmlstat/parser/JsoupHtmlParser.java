package ru.strelchm.htmlstat.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import ru.strelchm.htmlstat.api.HtmlParser;
import ru.strelchm.htmlstat.model.HtmlParsingSession;
import ru.strelchm.htmlstat.model.Word;
import ru.strelchm.htmlstat.repository.WordRepository;

import java.io.IOException;
import java.io.InputStream;

public class JsoupHtmlParser implements HtmlParser {
    public static final String DELIMITERS = "[ ,.!?\'\";:()\n\r\t\\[\\]]";

    private HtmlParsingSession parsingSession;
    private WordRepository wordRepository;

    public JsoupHtmlParser(HtmlParsingSession parsingSession, WordRepository wordRepository) {
        this.parsingSession = parsingSession;
        this.wordRepository = wordRepository;
    }

    public void parse(InputStream inputStream, String url) throws IOException {
        Document doc = Jsoup.parse(inputStream, null, url);
        Element body = doc.body();

        for (Node child : body.childNodes()) {
            getTextFromElement(child);
        }
    }

    private void getTextFromElement(Node element) {
        for (Node child : element.childNodes()) {
            if (child instanceof TextNode) {
                String trimmedText = ((TextNode) child).text().trim();

                if (!trimmedText.isEmpty()) {
                    for (String s : trimmedText.split(DELIMITERS)) {
                        if (!s.isEmpty()) {
                            Word word = new Word();
                            word.setText(s);
                            word.setHtmlParsingSession(parsingSession);
                            wordRepository.save(word);
                        }
                    }
                }
            }

            if (child instanceof Element) {
                getTextFromElement(child);
            }
        }
    }
}