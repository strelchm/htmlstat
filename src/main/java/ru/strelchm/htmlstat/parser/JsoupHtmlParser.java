package ru.strelchm.htmlstat.parser;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import ru.strelchm.htmlstat.api.DocumentParser;
import ru.strelchm.htmlstat.db.model.HtmlParsingSession;
import ru.strelchm.htmlstat.db.model.Word;
import ru.strelchm.htmlstat.db.repo.WordRepository;

import java.io.IOException;
import java.io.InputStream;

public class JsoupHtmlParser implements DocumentParser {
    public static final String DELIMITERS = "[ ,.!?\'\";:()\n\r\t\\[\\]]";

    private final SessionFactory sessionFactory;
    private final HtmlParsingSession parsingSession;
    private final WordRepository wordRepository;

    public JsoupHtmlParser(SessionFactory sessionFactory, HtmlParsingSession parsingSession, WordRepository wordRepository) {
        this.sessionFactory = sessionFactory;
        this.parsingSession = parsingSession;
        this.wordRepository = wordRepository;
    }

    public void parse(InputStream inputStream, String url) throws IOException {
        Document doc = Jsoup.parse(inputStream, null, url);
        Element body = doc.body();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        for (Node child : body.childNodes()) {
            getTextFromElement(session, child);
        }

        session.flush();

        session.getTransaction().commit();
        session.close();
    }

    private void getTextFromElement(Session session, Node element) {
        for (Node child : element.childNodes()) {
            if (child instanceof TextNode) {
                String trimmedText = ((TextNode) child).text().trim();

                if (!trimmedText.isEmpty()) {
                    for (String s : trimmedText.split(DELIMITERS)) {
                        if (!s.isEmpty()) {
                            Word word = new Word();
                            word.setText(s);
                            word.setHtmlParsingSession(parsingSession);
                            wordRepository.save(session, word);
                        }
                    }
                }
            }

            if (child instanceof Element) {
                getTextFromElement(session, child);
            }
        }
    }
}
