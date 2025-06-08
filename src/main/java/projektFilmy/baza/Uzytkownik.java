package projektFilmy.baza;

// Importuje wszystkie adnotacje JPA potrzebne do mapowania encji (np. @Entity, @Id, @Column itd.)
import jakarta.persistence.*;

@Entity // Oznacza klasę jako encję – będzie odwzorowana na tabelę w bazie danych
@Table(name = "uzytkownicy") // Ustawia nazwę tabeli w bazie danych na "uzytkownicy"
public class Uzytkownik {

    @Id // Oznacza pole jako klucz główny (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Wartość będzie generowana automatycznie (autoinkrementacja)
    private int id;

    @Column(unique = true, nullable = false) // Kolumna 'login' musi być unikalna i nie może być pusta
    private String login;

    @Column(nullable = false) // Kolumna 'haslo' nie może być pusta
    private String haslo;

    // Konstruktor domyślny – wymagany przez Hibernate
    public Uzytkownik() {
    }

    // Konstruktor z parametrami – używany np. przy rejestracji użytkownika
    public Uzytkownik(String login, String haslo) {
        this.login = login;
        this.haslo = haslo;
    }

    @Override
    public String toString() {
        return login; // Reprezentacja tekstowa obiektu – wyświetla login
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }
}
