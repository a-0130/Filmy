package projektFilmy.baza;

import jakarta.persistence.*; // @Entity, @Table....

@Entity // Informuje Hibernate, że ta klasa jest mapowana na tabelę w bazie danych
@Table(name = "filmy") // Nazwa tabeli w bazie danych, do której ta klasa jest przypisana
public class Film {

    @Id // Określa pole jako klucz główny (primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoinkrementacja w bazie danych
    private Long id;

    private String tytul;
    private int rok;
    private int czasTrwania;
    private double ocena;

    @ManyToOne // Relacja wiele filmów do jednej kategorii
    @JoinColumn(name = "id_kategoria") // Klucz obcy w tabeli FILMY wskazujący na kategorię
    private Kategoria kategoria;

    @ManyToOne // Relacja wiele filmów do jednego reżysera
    @JoinColumn(name = "id_rezyser") // Klucz obcy w tabeli FILMY wskazujący na reżysera
    private Rezyser rezyser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public int getRok() {
        return rok;
    }

    public void setRok(int rok) {
        this.rok = rok;
    }

    public int getCzasTrwania() {
        return czasTrwania;
    }

    public void setCzasTrwania(int czasTrwania) {
        this.czasTrwania = czasTrwania;
    }

    public double getOcena() {
        return ocena;
    }

    public void setOcena(double ocena) {
        this.ocena = ocena;
    }

    public Kategoria getKategoria() {
        return kategoria;
    }

    public void setKategoria(Kategoria kategoria) {
        this.kategoria = kategoria;
    }

    public Rezyser getRezyser() {
        return rezyser;
    }

    public void setRezyser(Rezyser rezyser) {
        this.rezyser = rezyser;
    }

    @Override
    public String toString() {
        return tytul + " (" + rok + ")";
    }
}
