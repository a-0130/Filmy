package projektFilmy.baza;
import jakarta.persistence.*;
import java.util.List;  // Umożliwia korzystanie z listy obiektów – potrzebne do relacji jeden-do-wielu (1:N) np. List<Film>

@Entity // Oznacza klasę jako encję – będzie odwzorowana na tabelę w bazie danych
@Table(name = "rezyser") // Ustawia nazwę tabeli w bazie danych na "rezyser"
public class Rezyser {

    @Id // Oznacza pole jako klucz główny (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatyczne generowanie ID (np. autoinkrementacja)
    private Long id;

    private String imie;
    private String nazwisko;

    @OneToMany(mappedBy = "rezyser", cascade = CascadeType.ALL)
    // Relacja jeden-do-wielu – jeden reżyser może mieć wiele filmów.
    // 'mappedBy = "rezyser"' oznacza, że to pole jest odwzorowaniem relacji z klasy Film.
    // Cascade = ALL oznacza, że operacje na reżyserze będą wpływać na jego filmy (np. usunięcie)
    private List<Film> filmy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return imie + " " + nazwisko; // Reprezentacja tekstowa obiektu Rezyser
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public List<Film> getFilmy() {
        return filmy;
    }

    public void setFilmy(List<Film> filmy) {
        this.filmy = filmy;
    }
}
