package projektFilmy.narzedzia;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import projektFilmy.baza.Film;
import projektFilmy.baza.Kategoria;
import projektFilmy.baza.Rezyser;

public class KonfiguracjaHibernate {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    // Tworzy SessionFactory na podstawie pliku konfiguracyjnego hibernate.cfg.xml
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml")    // Ładuje plik konfiguracyjny
                    .addAnnotatedClass(Film.class)     // Mapuje klasę Film
                    .addAnnotatedClass(Kategoria.class) // Mapuje klasę Kategoria
                    .addAnnotatedClass(Rezyser.class)   // Mapuje klasę Rezyser
                    .buildSessionFactory();            // Tworzy SessionFactory
        } catch (Throwable ex) {
            System.err.println("Blad tworzenia SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Zwraca globalna instancje SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Zamyka SessionFactory przy zamknieciu aplikacji
    public static void shutdown() {
        getSessionFactory().close();
    }
}
