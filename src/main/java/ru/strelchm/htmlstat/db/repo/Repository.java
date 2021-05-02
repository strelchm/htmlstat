package ru.strelchm.htmlstat.db.repo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Repository<T> {
    protected final SessionFactory sessionFactory;

    public Repository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(T entity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        save(session, entity);

        session.flush();

        session.refresh(entity); // обязательно обновить, что бы коллекция @OneToMany обновилась!

        session.getTransaction().commit();
        session.close();
    }

    public void save(Session session, T entity) {
        session.save(entity);
    }

    public void remove(T entity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.refresh(entity);
        session.remove(entity);

        session.flush();

        session.getTransaction().commit();
        session.close();
    }
}
