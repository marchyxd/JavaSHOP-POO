package utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            System.out.println("Initializing Hibernate SessionFactory...");
            SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            System.out.println("Hibernate SessionFactory initialization successful");
            return factory;
        } catch (Throwable ex) {
            System.err.println("SessionFactory initialization failed: " + ex);
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory has not been initialized");
        }
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
