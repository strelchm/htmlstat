package ru.strelchm.htmlstat.db.repo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.strelchm.htmlstat.db.model.HtmlParsingSession;

import java.util.List;

public class HtmlSessionRepository extends Repository<HtmlParsingSession> {
    public HtmlSessionRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<HtmlParsingSession> getAllByUrl(String url) {
        List<HtmlParsingSession> list;
        Session session = sessionFactory.openSession();

        Query<HtmlParsingSession> query = session.createQuery("SELECT s FROM HtmlParsingSession s WHERE url = :url ORDER BY parsedTime DESC");
        query.setParameter("url", url);
        list = query.list();

        session.close();

        return list;
    }

    public void removeAllByUrl(String url) {
        List<HtmlParsingSession> parsingSessions = getAllByUrl(url);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        for (HtmlParsingSession parsingSession : parsingSessions) {
            session.refresh(parsingSession);
            session.remove(parsingSession);
        }

        session.flush();

        session.getTransaction().commit();
        session.close();
    }
}
