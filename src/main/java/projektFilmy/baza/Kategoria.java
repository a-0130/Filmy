package projektFilmy.baza;
import jakarta.persistence.*;
import java.util.List;

@Entity // Oznacza klasę jako encję – będzie odwzorowana na tabelę w bazie danych
@Table(name = "kategoria") // Ustawia nazwę tabeli w bazie danych na "kategoria"
public class Kategoria {

    @Id // Oznacza pole jako klucz główny (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatyczne generowanie ID (np. autoinkrementacja)
    private Long id;
    private String nazwa;

    @OneToMany(mappedBy = "kategoria", cascade = CascadeType.ALL)
    // Relacja jeden-do-wielu – jedna kategoria może mieć wiele filmów.
    // 'mappedBy = "kategoria"' oznacza, że to pole odwzorowuje relację z klasy Film.
    // Cascade = ALL oznacza, że operacje na kategorii będą wpływać na przypisane filmy (np. usuwanie)
    private List<Film> filmy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return nazwa; // Reprezentacja tekstowa kategorii – zwraca tylko nazwę
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public List<Film> getFilmy() {
        return filmy;
    }

    public void setFilmy(List<Film> filmy) {
        this.filmy = filmy;
    }
}
