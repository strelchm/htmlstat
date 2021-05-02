package ru.strelchm.htmlstat.parser;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.strelchm.htmlstat.api.DocumentParser;
import ru.strelchm.htmlstat.db.model.HtmlParsingSession;
import ru.strelchm.htmlstat.db.model.WordStatistics;
import ru.strelchm.htmlstat.db.repo.HtmlSessionRepository;
import ru.strelchm.htmlstat.db.repo.WordRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class JsoupHtmlParserTest {

    private static final Logger TEST_LOGGER = Logger.getLogger(JsoupHtmlParserTest.class.getName());

    private final SessionFactory sessionFactory;
    private HtmlParsingSession parsingSession;
    private HtmlSessionRepository htmlSessionRepository;
    private final WordRepository wordRepository;

    private static final List<String> CORRECT_WORDS = Arrays.asList(
            "Hello",
            "world",
            "Learn",
            "java",
            "please",
            "My",
            "name",
            "is",
            "Han",
            "Solo",
            "How",
            "are",
            "u",
            "Lets",
            "watch",
            "film",
            "great",
            "hahaha",
            "Java",
            "Good",

            // повтор 1
            "Learn",
            "java",
            "please",

            // повтор 2
            "Learn",
            "java",
            "please",

            // повтор 3
            "Learn",
            "java",
            "please",

            "Han",
            "Solo"
    );

    public JsoupHtmlParserTest() {
        Configuration configuration = new Configuration();
        this.sessionFactory = configuration.configure().buildSessionFactory();
        this.wordRepository = new WordRepository(sessionFactory);
    }

    @BeforeEach
    public void setUp() {
        this.htmlSessionRepository = new HtmlSessionRepository(sessionFactory);

        this.parsingSession = new HtmlParsingSession();
        this.parsingSession.setParsedTime(LocalDateTime.now());

        this.htmlSessionRepository.save(this.parsingSession);
    }

    @AfterEach
    public void tearDown() {
        this.htmlSessionRepository.remove(this.parsingSession);
    }

    @Test
    void parse() throws IOException {
        List<WordStatistics> statisticsResult;
        URL url = this.getClass().getResource("/test.html");
        File initialFile = new File(url.getFile());
        InputStream targetStream = new FileInputStream(initialFile);

        DocumentParser parser = new JsoupHtmlParser(this.sessionFactory, this.parsingSession, this.wordRepository);
        parser.parse(targetStream, "localhost");

        statisticsResult = wordRepository.getWordsByHtmlSession(this.parsingSession);

        Map<String, Integer> entryMap = convertCorrectValuesToEntryMap();

        assertEquals(entryMap.entrySet().size(), statisticsResult.size());
        entryMap.entrySet().parallelStream().forEach(v -> {
            TEST_LOGGER.info("checking entry {" + v.getKey() + " ; " + v.getValue() + "}");
            assertTrue(checkWordExists(v, statisticsResult));
        });
    }

    private Map<String, Integer> convertCorrectValuesToEntryMap() {
        Map<String, Integer> result = new HashMap<>();

        CORRECT_WORDS.forEach(v -> {
            result.put(v, result.getOrDefault(v, 0) + 1);
        });


        return result;
    }

    private boolean checkWordExists(Map.Entry<String, Integer> entry, List<WordStatistics> statisticsResult) {
        for (WordStatistics v : statisticsResult) {
            if (entry.getKey().equals(v.getWord()) && entry.getValue() == v.getCount()) {
                return true;
            }
        }
        return false;
    }
}