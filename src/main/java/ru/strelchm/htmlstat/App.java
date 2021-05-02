package ru.strelchm.htmlstat;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.strelchm.htmlstat.api.DocumentLoader;
import ru.strelchm.htmlstat.api.DocumentParser;
import ru.strelchm.htmlstat.api.DocumentStatProcessingException;
import ru.strelchm.htmlstat.api.StatisticsPrinter;
import ru.strelchm.htmlstat.db.model.HtmlParsingSession;
import ru.strelchm.htmlstat.parser.HtmlDocumentLoader;
import ru.strelchm.htmlstat.parser.JsoupHtmlParser;
import ru.strelchm.htmlstat.parser.ParsingStatisticsPrinter;
import ru.strelchm.htmlstat.db.repo.HtmlSessionRepository;
import ru.strelchm.htmlstat.db.repo.WordRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

public class App {
    public static final Logger GLOBAL_LOGGER = Logger.getGlobal();
    public static final Logger MAIN_LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        AppMode mode;
        String url;

        try {
            if (args.length < 1) {
                throw new DocumentStatProcessingException("Application has not input parameters. App mode can't be determined");
            }

            mode = AppMode.getByKey(args[0]);

            // инициализация конфигурации Hibernate / репозиториев доступа к БД
            Configuration configuration = new Configuration();
            SessionFactory sessionFactory = configuration.configure().buildSessionFactory();

            WordRepository wordRepository = new WordRepository(sessionFactory);
            HtmlSessionRepository htmlSessionRepository = new HtmlSessionRepository(sessionFactory);

            MAIN_LOGGER.info("Application mode is: " + mode.name());

            if (args.length < 2) {
                throw new DocumentStatProcessingException("Url input parameter not found");
            }

            url = args[1];

            // сценарии разных режимов
            switch (mode) {
                case PARSING:
                    // создаем сеанс парсинга
                    HtmlParsingSession htmlParsingSession = new HtmlParsingSession();
                    htmlParsingSession.setUrl(url);
                    htmlParsingSession.setParsedTime(LocalDateTime.now());
                    htmlSessionRepository.save(htmlParsingSession);

                    DocumentLoader loader = new HtmlDocumentLoader();
                    DocumentParser htmlParser = new JsoupHtmlParser(sessionFactory, htmlParsingSession, wordRepository);
                    htmlParser.parse(loader.getInputStreamFromUrl(url), url);

                    StatisticsPrinter statisticsPrinter = new ParsingStatisticsPrinter(wordRepository);
                    statisticsPrinter.print(htmlParsingSession);
                    break;
                case HISTORY:
                    StatisticsPrinter printer;
                    List<HtmlParsingSession> sessions = htmlSessionRepository.getAllByUrl(url);

                    if (sessions.isEmpty()) {
                        MAIN_LOGGER.warning("No sessions found for url '" + url + "'");
                    }

                    for (HtmlParsingSession session : sessions) {
                        printer = new ParsingStatisticsPrinter(wordRepository);
                        printer.print(session);
                    }
                    break;
                case CLEAR:
                    htmlSessionRepository.removeAllByUrl(url);
                    MAIN_LOGGER.info("Successfully clearing history for url '" + url + "'");
                    break;
                default:
                    throw new DocumentStatProcessingException("Unknown parsing mode '" + mode.name() + "'");
            }
        } catch (Exception exception) {
            GLOBAL_LOGGER.severe(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
