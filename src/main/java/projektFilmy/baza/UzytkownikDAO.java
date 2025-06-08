package projektFilmy.baza;
import org.hibernate.Session;// Import klasy Hibernate Session – połączenie z bazą danych
import org.hibernate.Transaction;// Import klasy Transaction – pozwala zarządzać transakcjami (BEGIN, COMMIT)
import org.hibernate.query.Query;// Import klasy Query – służy do tworzenia zapytań HQL
import projektFilmy.narzedzia.KonfiguracjaHibernate;

public class UzytkownikDAO {

    // Rejestruje nowego użytkownika w bazie danych, jeśli login nie jest już zajęty
    public static boolean zarejestruj(String login, String haslo) {
        try (Session sesja = KonfiguracjaHibernate.getSessionFactory().openSession()) {

            Transaction tx = sesja.beginTransaction(); // Rozpoczęcie transakcji – zapis do bazy wymaga transakcji

            // Tworzenie zapytania HQL: sprawdzanie, czy użytkownik o danym loginie już istnieje
            // "from Uzytkownik" – HQL operuje na nazwach klas
            Query<Uzytkownik> zapytanie = sesja.createQuery(
                    "from Uzytkownik where login = :login", Uzytkownik.class);
            zapytanie.setParameter("login", login); // Przekazanie parametru do zapytania


            if (!zapytanie.getResultList().isEmpty()) {
                return false; // Jeśli lista wyników nie jest pusta, login już istnieje – rejestracja nieudana
            }

            Uzytkownik nowy = new Uzytkownik(login, haslo); // Tworzymy nowego użytkownika i zapisujemy do bazy
            sesja.persist(nowy); // Hibernate wykona INSERT

            tx.commit(); // Zatwierdzenie transakcji – dane trafią do bazy
            return true; // Rejestracja zakończona powodzeniem
        }
    }

    // Próbuje zalogować użytkownika – zwraca true, jeśli login i hasło się zgadzają
    public static boolean zaloguj(String login, String haslo) {
        try (Session sesja = KonfiguracjaHibernate.getSessionFactory().openSession()) {

            // Tworzymy zapytanie HQL, które sprawdzi, czy istnieje użytkownik o podanym loginie i haśle
            Query<Uzytkownik> zapytanie = sesja.createQuery(
                    "from Uzytkownik where login = :login and haslo = :haslo", Uzytkownik.class);

            // Ustawiamy parametry zapytania
            zapytanie.setParameter("login", login);
            zapytanie.setParameter("haslo", haslo);

            // Jeśli lista wyników nie jest pusta, logowanie zakończone sukcesem
            return !zapytanie.getResultList().isEmpty();
        }
    }
}
