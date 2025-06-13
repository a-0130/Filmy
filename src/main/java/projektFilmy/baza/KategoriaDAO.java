package projektFilmy.baza;
import org.hibernate.Session;
import org.hibernate.Transaction;
import projektFilmy.narzedzia.KonfiguracjaHibernate; // Import klasy konfigurującej połączenie z bazą danych (SessionFactory)
import java.util.List;

public class KategoriaDAO {

    // Pobiera wszystkie rekordy typu Kategoria z bazy danych
    public static List<Kategoria> pobierzWszystkie() {
        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            // HQL: zapytanie do wszystkich encji Kategoria
            return session.createQuery("from Kategoria", Kategoria.class).list();
        }
    }

    // Zwraca istniejącą kategorię o podanej nazwie lub dodaje nową, jeśli nie istnieje
    public static Kategoria znajdzLubDodaj(String tekst) {
        // Sprawdzamy, czy dana kategoria już istnieje (ignorując wielkość liter)
        for (Kategoria k : pobierzWszystkie()) {
            if (k.getNazwa().equalsIgnoreCase(tekst)) {
                return k; // Znaleziona – zwracamy istniejącą
            }
        }
        Kategoria nowa = new Kategoria(); // Jeśli nie znaleziono – tworzymy nową kategorię
        nowa.setNazwa(tekst);

        // Zapisujemy nową kategorię do bazy danych
        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction(); // Rozpoczęcie transakcji
            session.persist(nowa); // Zapisanie nowej encji (INSERT)
            tx.commit(); // Zatwierdzenie transakcji
        }
        return nowa;
    }
    public static boolean dodaj(String nazwa) {
        for (Kategoria k : pobierzWszystkie()) {
            if (k.getNazwa().equalsIgnoreCase(nazwa)) {
                return false;
            }
        }

        Kategoria nowa = new Kategoria();
        nowa.setNazwa(nazwa);

        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(nowa);
            tx.commit();
        }

        return true;
    }

    public static boolean usun(String nazwa) {
        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            Kategoria znaleziona = session.createQuery(
                            "from Kategoria k where lower(k.nazwa) = :nazwa", Kategoria.class)
                    .setParameter("nazwa", nazwa.toLowerCase())
                    .uniqueResult();

            if (znaleziona == null) return false;

            Long ileFilmow = session.createQuery(
                            "select count(f) from Film f where f.kategoria.id = :id", Long.class)
                    .setParameter("id", znaleziona.getId())
                    .uniqueResult();

            if (ileFilmow != null && ileFilmow == 0) {
                Transaction tx = session.beginTransaction();
                session.remove(session.contains(znaleziona) ? znaleziona : session.merge(znaleziona));
                tx.commit();
                return true;
            }
        }

        return false;
    }

}




