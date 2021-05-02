package ru.strelchm.htmlstat.db.repo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.strelchm.htmlstat.db.model.HtmlParsingSession;
import ru.strelchm.htmlstat.db.model.Word;
import ru.strelchm.htmlstat.db.model.WordStatistics;

import java.util.List;
import java.util.stream.Collectors;

public class WordRepository extends Repository<Word> {
    public WordRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<WordStatistics> getWordsByHtmlSession(HtmlParsingSession parsingSession) {
        List<Object[]> list;
        Session session = sessionFactory.openSession();

        Query<Object[]> query = session.createQuery("SELECT w.text, COUNT(*) AS entries FROM Word w GROUP BY w.text, w.htmlParsingSession HAVING w.htmlParsingSession = :htmlParsingSession ORDER BY entries DESC");
        query.setParameter("htmlParsingSession", parsingSession);
        list = query.list();

        session.close();

        return list.stream().map(v -> new WordStatistics((String) v[0], (Long) v[1])).collect(Collectors.toList());
    }
}
