package ru.strelchm.htmlstat.parser;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import ru.strelchm.htmlstat.api.DocumentParser;
import ru.strelchm.htmlstat.model.HtmlParsingSession;
import ru.strelchm.htmlstat.model.Word;
import ru.strelchm.htmlstat.repository.WordRepository;

import java.io.IOException;
import java.io.InputStream;

public class JsoupHtmlParser implements DocumentParser {
    public static final String DELIMITERS = "[ ,.!?\'\";:()\n\r\t\\[\\]]";

    private SessionFactory sessionFactory;
    private HtmlParsingSession parsingSession;
    private WordRepository wordRepository;

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
//        int index = 0;

        for (Node child : element.childNodes()) {
            if (child instanceof TextNode) {
                String trimmedText = ((TextNode) child).text().trim();

                if (!trimmedText.isEmpty()) {
                    for (String s : trimmedText.split(DELIMITERS)) {
                        if (!s.isEmpty()) {
//                            ++index;
                            Word word = new Word();
                            word.setText(s);
                            word.setHtmlParsingSession(parsingSession);
                            wordRepository.save(session, word);

//                            if (index % 20 == 0) {
//                                session.flush();
//                                session.clear();
//                            }
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
