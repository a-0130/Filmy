package projektFilmy.baza;
import org.hibernate.Session;
import org.hibernate.Transaction;
import projektFilmy.narzedzia.KonfiguracjaHibernate;
import java.util.List;

public class RezyserDAO {

    // Pobiera wszystkich reżyserów z bazy danych
    public static List<Rezyser> pobierzWszystkich() {
        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            // HQL: zapytanie do wszystkich encji typu Rezyser
            return session.createQuery("from Rezyser", Rezyser.class).list();
        }
    }
    public static Rezyser znajdzLubDodaj(String tekst) {

        // Przeszukiwanie istniejących reżyserów po imieniu i nazwisku
        for (Rezyser r : pobierzWszystkich()) {
            if ((r.getImie() + " " + r.getNazwisko()).equalsIgnoreCase(tekst)) {
                return r; // Zwraca istniejącego reżysera
            }
        }
        // Jeśli nie znaleziono, dzieli podany tekst na imię i nazwisko
        String[] czesci = tekst.split(" ", 2);
        String imie = czesci.length > 0 ? czesci[0] : "";
        String nazwisko = czesci.length > 1 ? czesci[1] : "";

        Rezyser nowy = new Rezyser(); // Tworzenie nowego obiektu reżysera
        nowy.setImie(imie);
        nowy.setNazwisko(nazwisko);

        // Zapisanie nowego reżysera do bazy danych
        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction(); // Rozpoczęcie transakcji
            session.persist(nowy); // INSERT nowego obiektu
            tx.commit(); // Zatwierdzenie transakcji
        }
        return nowy;
    }
    public static boolean dodaj(String tekst) {
        // Sprawdź, czy reżyser już istnieje
        for (Rezyser r : pobierzWszystkich()) {
            if ((r.getImie() + " " + r.getNazwisko()).equalsIgnoreCase(tekst)) {
                return false; // rezyser istnieje
            }
        }

        // Rozdzielenie tekstu na imię i nazwisko
        String[] czesci = tekst.trim().split(" ", 2);
        String imie = czesci.length > 0 ? czesci[0] : "";
        String nazwisko = czesci.length > 1 ? czesci[1] : "";

        // Dodaj nowego reżysera
        Rezyser nowy = new Rezyser();
        nowy.setImie(imie);
        nowy.setNazwisko(nazwisko);

        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(nowy);
            tx.commit();
        }

        return true;
    }


    // Usuwa reżysera z bazy danych, ale tylko jeśli nie ma przypisanych filmów
    public static boolean usun(String tekst) {
        try (Session session = KonfiguracjaHibernate.getSessionFactory().openSession()) {
            Rezyser znaleziony = session.createQuery(
                            "from Rezyser r where lower(concat(r.imie, ' ', r.nazwisko)) = :tekst", Rezyser.class)
                    .setParameter("tekst", tekst.toLowerCase())
                    .uniqueResult();

            if (znaleziony == null) return false;

            Long ileFilmow = session.createQuery(
                            "select count(f) from Film f where f.rezyser.id = :id", Long.class)
                    .setParameter("id", znaleziony.getId())
                    .uniqueResult();

            if (ileFilmow != null && ileFilmow == 0) {
                Transaction tx = session.beginTransaction();
                session.remove(session.contains(znaleziony) ? znaleziony : session.merge(znaleziony));
                tx.commit();
                return true;
            }
        }
        return false;
    }

}
