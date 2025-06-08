package projektFilmy.Kontroler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import projektFilmy.baza.*;

public class FilmyKontroler {

    // Tabela i kolumny
    @FXML private TableView<Film> filmTable;
    @FXML private TableColumn<Film, String> tytulColumn;                  // kolumna tytułu
    @FXML private TableColumn<Film, Integer> czasTrwaniaColumn;          // kolumna czasu trwania
    @FXML private TableColumn<Film, Integer> rokColumn;                  // kolumna roku
    @FXML private TableColumn<Film, Rezyser> rezyserColumn;              // kolumna reżysera
    @FXML private TableColumn<Film, Double> ocenaColumn;                 // kolumna oceny
    @FXML private TableColumn<Film, Kategoria> kategoriaColumn;          // kolumna kategorii

    // Pola formularza
    @FXML private TextField tytulField;
    @FXML private TextField rokField;
    @FXML private TextField czasTrwaniaField;
    @FXML private ComboBox<Rezyser> rezyserCombo;
    @FXML private ComboBox<Kategoria> kategoriaCombo;
    @FXML private TextField ocenaField;
    @FXML private Label komunikatLabel;

    // ObservableListy do powiązania z widokiem
    private ObservableList<Film> filmy = FXCollections.observableArrayList();
    private ObservableList<Rezyser> rezyserzy = FXCollections.observableArrayList();
    private ObservableList<Kategoria> kategorie = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Powiązania kolumn z polami encji Film
        tytulColumn.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        rezyserColumn.setCellValueFactory(new PropertyValueFactory<>("rezyser"));
        rokColumn.setCellValueFactory(new PropertyValueFactory<>("rok"));
        czasTrwaniaColumn.setCellValueFactory(new PropertyValueFactory<>("czasTrwania"));
        ocenaColumn.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        kategoriaColumn.setCellValueFactory(new PropertyValueFactory<>("kategoria"));

        // Wczytanie danych do ComboBoxów
        rezyserzy.setAll(RezyserDAO.pobierzWszystkich());
        kategorie.setAll(KategoriaDAO.pobierzWszystkie());
        rezyserCombo.setItems(rezyserzy);
        rezyserCombo.setEditable(true); // umożliwia wpisanie nowego reżysera
        kategoriaCombo.setItems(kategorie);
        kategoriaCombo.setEditable(true); // umożliwia wpisanie nowej kategorii

        // Załadowanie filmów do tabeli
        filmy.setAll(FilmDAO.pobierzWszystkie());
        filmTable.setItems(filmy);
    }

    @FXML
    private void dodajFilm() {
        try {
            // Pobieranie danych z formularza
            String tytul = tytulField.getText().trim();
            String rokText = rokField.getText().trim();
            String czasText = czasTrwaniaField.getText().trim();
            String ocenaText = ocenaField.getText().trim();
            String rezTekst = rezyserCombo.getEditor().getText().trim();
            String katTekst = kategoriaCombo.getEditor().getText().trim();

            // Walidacje pól tekstowych
            if (tytul.isEmpty()) {
                komunikatLabel.setText("Pole tytul nie moze byc puste.");
                return;
            }
            if (!tytul.matches("[A-Za-z0-9 .,!?-]+")) {
                komunikatLabel.setText("Tytul zawiera niedozwolone znaki.");
                return;
            }
            if (rokText.isEmpty() || czasText.isEmpty() || ocenaText.isEmpty()) {
                komunikatLabel.setText("Wypelnij wszystkie pola liczbowe.");
                return;
            }
            if (!rezTekst.matches("[A-Za-z -]+")) {
                komunikatLabel.setText("Rezyser moze zawierac tylko litery i myslniki.");
                return;
            }
            if (!katTekst.matches("[A-Za-z -]+")) {
                komunikatLabel.setText("Kategoria moze zawierac tylko litery i myslniki.");
                return;
            }

            // Parsowanie danych liczbowych
            int rok = Integer.parseInt(rokText);
            int czas = Integer.parseInt(czasText);
            double ocena = Double.parseDouble(ocenaText);

            if (rok <= 0) { komunikatLabel.setText("Rok musi byc wiekszy od 0."); return; }
            if (czas <= 0) { komunikatLabel.setText("Czas trwania musi byc wiekszy od 0."); return; }
            if (ocena < 0 || ocena > 10) { komunikatLabel.setText("Ocena musi byc w zakresie 0-10."); return; }

            // Tworzenie nowego filmu i zapisywanie go do bazy
            Film film = new Film();
            film.setTytul(tytul);
            film.setRok(rok);
            film.setCzasTrwania(czas);
            film.setOcena(ocena);
            film.setRezyser(RezyserDAO.znajdzLubDodaj(rezTekst));
            film.setKategoria(KategoriaDAO.znajdzLubDodaj(katTekst));

            FilmDAO.dodaj(film);       // Zapis do bazy
            filmy.add(film);           // Dodanie do tabeli
            wyczyscPola();             // Wyczyść pola formularza
            komunikatLabel.setText("Film dodany!");
        } catch (NumberFormatException e) {
            komunikatLabel.setText("Rok, czas i ocena musza byc poprawnymi liczbami.");
        } catch (Exception e) {
            komunikatLabel.setText("Blad dodawania filmu.");
        }
    }

    @FXML
    private void edytujFilm() {
        Film wybrany = filmTable.getSelectionModel().getSelectedItem(); // Pobranie wybranego filmu
        if (wybrany != null) {
            try {
                // Aktualizacja danych filmu
                wybrany.setTytul(tytulField.getText());
                wybrany.setRok(Integer.parseInt(rokField.getText()));
                wybrany.setCzasTrwania(Integer.parseInt(czasTrwaniaField.getText()));
                wybrany.setOcena(Double.parseDouble(ocenaField.getText()));
                wybrany.setRezyser(RezyserDAO.znajdzLubDodaj(rezyserCombo.getEditor().getText().trim()));
                wybrany.setKategoria(KategoriaDAO.znajdzLubDodaj(kategoriaCombo.getEditor().getText().trim()));

                FilmDAO.edytuj(wybrany); // Zapis zmian w bazie
                filmTable.refresh();     // Odświeżenie tabeli
                wyczyscPola();
            } catch (Exception e) {
                System.err.println("Błąd edycji: " + e.getMessage());
            }
        }
    }

    @FXML
    private void usunFilm() {
        Film wybrany = filmTable.getSelectionModel().getSelectedItem();
        if (wybrany != null) {
            FilmDAO.usun(wybrany);                // Usunięcie z bazy
            filmTable.getItems().remove(wybrany); // Usunięcie z tabeli
            wyczyscPola();
        }
    }

    @FXML
    private void usunRezysera() {
        String tekst = rezyserCombo.getEditor().getText().trim();
        if (!tekst.isEmpty()) {
            RezyserDAO.usun(tekst);                          // Usunięcie reżysera (jeśli możliwe)
            rezyserzy.setAll(RezyserDAO.pobierzWszystkich()); // Odświeżenie listy
            rezyserCombo.setItems(rezyserzy);
            rezyserCombo.getEditor().clear();
        }
    }

    @FXML
    private void usunKategorie() {
        String tekst = kategoriaCombo.getEditor().getText().trim();
        if (!tekst.isEmpty()) {
            KategoriaDAO.usun(tekst);                         // Usunięcie kategorii (jeśli możliwe)
            kategorie.setAll(KategoriaDAO.pobierzWszystkie()); // Odświeżenie listy
            kategoriaCombo.setItems(kategorie);
            kategoriaCombo.getEditor().clear();
        }
    }

    private void wyczyscPola() {
        tytulField.clear();
        rokField.clear();
        czasTrwaniaField.clear();
        ocenaField.clear();
        rezyserCombo.getEditor().clear();
        rezyserCombo.getSelectionModel().clearSelection();
        kategoriaCombo.getEditor().clear();
        kategoriaCombo.getSelectionModel().clearSelection();
    }
}
