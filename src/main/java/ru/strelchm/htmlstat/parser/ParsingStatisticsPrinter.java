package ru.strelchm.htmlstat.parser;

import ru.strelchm.htmlstat.api.StatisticsPrinter;
import ru.strelchm.htmlstat.db.model.HtmlParsingSession;
import ru.strelchm.htmlstat.db.model.WordStatistics;
import ru.strelchm.htmlstat.db.repo.WordRepository;

import java.util.logging.Logger;

public class ParsingStatisticsPrinter implements StatisticsPrinter {
    private final WordRepository wordRepository;
    private static final Logger logger = Logger.getLogger(ParsingStatisticsPrinter.class.getName());

    public ParsingStatisticsPrinter(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public void print(HtmlParsingSession htmlParsingSession) {
        logger.info("Printing statistics history for url '" + htmlParsingSession.getUrl() + "' for session time " + htmlParsingSession.getParsedTime());
        wordRepository.getWordsByHtmlSession(htmlParsingSession)
                .parallelStream()
                .sorted(new WordStatistics.WordStatisticsComparator())
                .forEachOrdered(v -> System.out.println(v.getWord() + " - " + v.getCount()));
    }
}
