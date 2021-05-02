package ru.strelchm.htmlstat;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.strelchm.htmlstat.api.DocumentLoader;
import ru.strelchm.htmlstat.api.DocumentParser;
import ru.strelchm.htmlstat.api.ParserException;
import ru.strelchm.htmlstat.api.StatisticsPrinter;
import ru.strelchm.htmlstat.model.HtmlParsingSession;
import ru.strelchm.htmlstat.parser.HtmlDocumentLoader;
import ru.strelchm.htmlstat.parser.JsoupHtmlParser;
import ru.strelchm.htmlstat.parser.ParsingStatisticsPrinter;
import ru.strelchm.htmlstat.repository.HtmlSessionRepository;
import ru.strelchm.htmlstat.repository.WordRepository;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class Main {
    public static final Logger GLOBAL_LOGGER = Logger.getGlobal();
    public static final Logger MAIN_LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        AppMode mode;

        try {
            if(args.length < 1) {
                throw new ParserException("Application has not input parameters. App mode can't be determined");
            }

            mode = AppMode.getByKey(args[0]);

            // инициализация конфигурации Hibernate / репозиториев доступа к БД
            Configuration configuration = new Configuration();
            SessionFactory sessionFactory = configuration.configure().buildSessionFactory();

            WordRepository wordRepository = new WordRepository(sessionFactory);
            HtmlSessionRepository htmlSessionRepository = new HtmlSessionRepository(sessionFactory);

            MAIN_LOGGER.info("Application mode is: " + mode.name());

            // сценарии разных режимов
            switch (mode) {
                case PARSING:
                    if(args.length < 2) {
                        throw new ParserException("Url input parameter not found");
                    }

                    String url = args[1];

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

                    break;
                case CLEAR:

                    break;
                default:
                    throw new ParserException("Unknown parsing mode '" + mode.name() + "'");
            }
        } catch (Exception exception) {
            GLOBAL_LOGGER.severe(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
