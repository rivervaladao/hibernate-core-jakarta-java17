package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class SessionUtil {
    private static SessionFactory sessionFactory;
    private static final ThreadLocal<Session> currentSession = new ThreadLocal<>();

    static public SessionFactory initSessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return sessionFactory;
    }
    static public void closeSessionFactory() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }
    static public Session getSession(){
        Session session = currentSession.get();
        if(session != null && session.isOpen()) return session;
        session = sessionFactory.openSession();
        return session;
    }
    static public void closeSession(){
        Session session = currentSession.get();
        if(session != null && session.isOpen())
            currentSession.set(null);
    }
    static public void getTransaction(){
        if(getSession()!= null && getSession())
    }
    static public void doWithTransaction(){
        try(Session session = getSession()) {
        }catch (Exception e){

        }finally {
            closeSession();
        }
    }
}
