package ru.strelchm.htmlstat.api;

import ru.strelchm.htmlstat.db.model.HtmlParsingSession;

public interface StatisticsPrinter {
    void print(HtmlParsingSession htmlParsingSession);
}
