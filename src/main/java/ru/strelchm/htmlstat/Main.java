package ru.strelchm.htmlstat;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.strelchm.htmlstat.api.DocumentLoader;
import ru.strelchm.htmlstat.api.DocumentParser;
import ru.strelchm.htmlstat.api.StatisticsPrinter;
import ru.strelchm.htmlstat.model.HtmlParsingSession;
import ru.strelchm.htmlstat.parser.JsoupHtmlParser;
import ru.strelchm.htmlstat.repository.HtmlSessionRepository;
import ru.strelchm.htmlstat.repository.WordRepository;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class Main {
    public static final Logger GLOBAL_LOGGER = Logger.getGlobal();

    public static void main(String[] args) {
        String url = "https://www.simbirsoft.com/";

        Configuration configuration = new Configuration();
        SessionFactory sessionFactory = configuration.configure().buildSessionFactory();

        WordRepository wordRepository = new WordRepository(sessionFactory);
        HtmlSessionRepository htmlSessionRepository = new HtmlSessionRepository(sessionFactory);

        HtmlParsingSession htmlParsingSession = new HtmlParsingSession();
        htmlParsingSession.setParsedTime(LocalDateTime.now());
        htmlSessionRepository.save(htmlParsingSession);

        try {
            DocumentLoader loader = new HtmlDocumentLoader();
            DocumentParser htmlParser = new JsoupHtmlParser(htmlParsingSession, wordRepository);
            htmlParser.parse(loader.getInputStreamFromUrl(url), url);

            StatisticsPrinter statisticsPrinter = new ParsingStatisticsPrinter(wordRepository);
            statisticsPrinter.print(htmlParsingSession);
        } catch (Exception exception) {
            GLOBAL_LOGGER.severe(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
