package projektFilmy.baza;
import org.hibernate.Session;// Import klasy Hibernate Session – reprezentuje połączenie z bazą danych
import org.hibernate.Transaction;// Import klasy Transaction – pozwala zarządzać transakcjami w Hibernate
import projektFilmy.narzedzia.KonfiguracjaHibernate;// Import klasy udostępniającej SessionFactory (połączenie z bazą)
import java.util.List;

public class FilmDAO {

    // Pobiera wszystkie rekordy typu Film z bazy danych
    public static List<Film> pobierzWszystkie() {
        // Otwiera nową sesję Hibernate do wykonania operacji SELECT
        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            // HQL: zapytanie do wszystkich obiektów typu Film (czyli: SELECT * FROM filmy)
            return session.createQuery("from Film", Film.class).list();
        }
    }

    // Dodaje nowy film do bazy danych
    public static void dodaj(Film film) {
        // Otwiera sesję do połączenia z bazą
        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            // Rozpoczęcie transakcji – wszystkie operacje od teraz są "tymczasowe",
            // dopóki nie zostaną zatwierdzone (commit). Dzięki temu możliwe jest
            // wykonanie operacji atomowo – czyli albo całość się powiedzie, albo nic.
            Transaction tx = session.beginTransaction();

            // Zapisanie obiektu Film do bazy (Hibernate wykona INSERT)
            session.persist(film);

            // Zatwierdzenie transakcji – dane trafiają na stałe do bazy danych
            tx.commit();
        }
    }

    // Aktualizuje istniejący rekord filmu
    public static void edytuj(Film film) {
        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            // Rozpoczęcie transakcji – aby zmiany były bezpieczne i spójne
            Transaction tx = session.beginTransaction();

            // Aktualizacja danych obiektu Film (Hibernate wykona UPDATE)
            session.merge(film);
            tx.commit(); // Zatwierdzenie zmian w bazie danych
        }
    }

    // Usuwa film z bazy danych
    public static void usun(Film film) {
        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            // Rozpoczęcie transakcji – usuwanie też wymaga bezpieczeństwa transakcyjnego
            Transaction tx = session.beginTransaction();

            // Pobranie obiektu z bazy po ID – Hibernate musi najpierw go załadować
            Film filmDoUsuniecia = session.get(Film.class, film.getId());

            if (filmDoUsuniecia != null) {
                // Zerowanie relacji – żeby nie było konfliktu z kluczami obcymi (FK)
                filmDoUsuniecia.setRezyser(null);
                filmDoUsuniecia.setKategoria(null);

                // Usunięcie obiektu z bazy (Hibernate wykona DELETE)
                session.remove(filmDoUsuniecia);
            }

            tx.commit(); // Zatwierdzenie operacji usunięcia
        }
    }
}
