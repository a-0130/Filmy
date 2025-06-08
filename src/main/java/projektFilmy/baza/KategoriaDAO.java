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

    // Usuwa kategorię o podanej nazwie, ale tylko jeśli nie jest przypisana do żadnego filmu
    public static void usun(String tekst) {
        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            // HQL: wyszukiwanie kategorii po nazwie (z małych liter, by było niewrażliwe na wielkość)
            Kategoria znaleziona = session.createQuery(
                            "from Kategoria k where lower(k.nazwa) = :tekst", Kategoria.class)
                    .setParameter("tekst", tekst.toLowerCase())
                    .uniqueResult(); // Zwraca jeden wynik lub null

            if (znaleziona == null) return; // Jeśli nie znaleziono – zakończ

            // HQL: policz ile filmów przypisanych jest do tej kategorii
            Long ileFilmow = session.createQuery(
                            "select count(f) from Film f where f.kategoria.id = :id", Long.class)
                    .setParameter("id", znaleziona.getId())
                    .uniqueResult();

            // Jeśli brak filmów – można bezpiecznie usunąć kategorię
            if (ileFilmow == 0) {
                Transaction tx = session.beginTransaction();
                // Jeśli encja nie jest zarządzana przez sesję, użyj merge()
                session.remove(session.contains(znaleziona) ? znaleziona : session.merge(znaleziona));
                tx.commit();
            }
        }
    }
}




