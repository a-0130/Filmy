package projektFilmy.Kontroler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import projektFilmy.baza.*;

public class FilmyKontroler {

    // Tabela i kolumny
    @FXML private TableView<Film> filmTable; // tabela w której będą wyświetlane filmy
    @FXML private TableColumn<Film, String> tytulColumn;                  // kolumna tytułu
    @FXML private TableColumn<Film, Integer> czasTrwaniaColumn;          // kolumna czasu trwania
    @FXML private TableColumn<Film, Integer> rokColumn;                  // kolumna roku
    @FXML private TableColumn<Film, Rezyser> rezyserColumn;              // kolumna reżysera
    @FXML private TableColumn<Film, Double> ocenaColumn;                 // kolumna oceny
    @FXML private TableColumn<Film, Kategoria> kategoriaColumn;          // kolumna kategorii

    // Pola formularza
    @FXML private TextField tytulField; // pole tekstowe, w którym uzytkownik wpisuje tytuł filmu
    @FXML private TextField rokField;
    @FXML private TextField czasTrwaniaField;
    @FXML private ComboBox<Rezyser> rezyserCombo; // rozwijane pole, z którego użytkownik może wybrać reżysera
    @FXML private ComboBox<Kategoria> kategoriaCombo;
    @FXML private TextField ocenaField;
    @FXML private Label komunikatLabel; // etykieta, w której wyświetlane są komunikaty

    // ObservableListy do powiązania z widokiem
    private ObservableList<Film> filmy = FXCollections.observableArrayList(); // Lista filmów, która jest powiązana z tabelą – wszystko, co tu się znajdzie, pojawi się w tabeli.
    private ObservableList<Rezyser> rezyserzy = FXCollections.observableArrayList(); // Lista reżyserów, która jest powiązana z rozwijaną listą reżyserów.
    private ObservableList<Kategoria> kategorie = FXCollections.observableArrayList(); // Lista kategorii, która jest powiązana z rozwijaną listą kategorii.

    @FXML
    public void initialize() {
        // Powiązania kolumn z polami encji Film
        tytulColumn.setCellValueFactory(new PropertyValueFactory<>("tytul")); // w kolumnie "Tytul" w tabeli będzie wyświetlana wartość pola tytul z każdego obiektu Film
        rezyserColumn.setCellValueFactory(new PropertyValueFactory<>("rezyser")); //W kolumnie "Rezyser" będzie wyświetlany reżyser filmu (czyli obiekt typu Rezyser, który ma swoją metodę toString()).
        rokColumn.setCellValueFactory(new PropertyValueFactory<>("rok"));
        czasTrwaniaColumn.setCellValueFactory(new PropertyValueFactory<>("czasTrwania"));
        ocenaColumn.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        kategoriaColumn.setCellValueFactory(new PropertyValueFactory<>("kategoria"));

        // Wczytanie danych do ComboBoxów
        rezyserzy.setAll(RezyserDAO.pobierzWszystkich()); // pobiera wszystkich reżyserów z bazy danych i dodaje do listy rezyserzy
        kategorie.setAll(KategoriaDAO.pobierzWszystkie()); // pobiera wszystkie kategorie z bazy danych i dodaje do listy kategorie
        rezyserCombo.setItems(rezyserzy); // ustawia listę rezyserów jako elementy rozwijanej listy reżyserów
        rezyserCombo.setEditable(true); // umożliwia wpisanie nowego reżysera
        kategoriaCombo.setItems(kategorie);
        kategoriaCombo.setEditable(true); // umożliwia wpisanie nowej kategorii

        // Załadowanie filmów do tabeli
        filmy.setAll(FilmDAO.pobierzWszystkie()); // pobiera wszystkie filmy z bazy danych i dodaje do listy filmy
        filmTable.setItems(filmy); // ustawia listę filmów jako elementy wyswietlane w tabeli
    }

    @FXML // Oznacza, że metoda jest powiązana z przyciskiem w pliku fxml
    private void dodajFilm() {
        try {
            // Pobieranie danych z formularza
            String tytul = tytulField.getText().trim(); // tytulField - pole tekstowe zdefiniowane w pliku fxml
            String rokText = rokField.getText().trim(); // getText() - pobiera tekst z pola tekstowego
            String czasText = czasTrwaniaField.getText().trim(); // trim() - usuwa białe znaki z początku i końca tekstu
            String ocenaText = ocenaField.getText().trim();
            String rezTekst = rezyserCombo.getEditor().getText().trim();  // getEditor() - pobiera edytor ComboBoxa
            String katTekst = kategoriaCombo.getEditor().getText().trim();

            // Walidacje pól tekstowych
            if (tytul.isEmpty()) {
                komunikatLabel.setText("Pole tytul nie moze byc puste.");
                return;
            }
            if (!tytul.matches("[A-Za-z0-9 .,!?-]+")) { // matches() - sprawdza, czy tekst pasuje do wzorca
                komunikatLabel.setText("Tytul zawiera niedozwolone znaki.");
                return; // przerywa dalsze wykonywanie metody, gdy walidacja nie jest
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
            int rok = Integer.parseInt(rokText); // parseInt() - konwertuje tekst na liczbę całkowitą
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
            film.setRezyser(RezyserDAO.znajdzLubDodaj(rezTekst)); // szukanie reżysera w bazie lub dodawanie go, jeśli nie istnieje
            film.setKategoria(KategoriaDAO.znajdzLubDodaj(katTekst)); // szukanie kategorii w bazie lub dodawanie go, jeśli nie istnieje

            FilmDAO.dodaj(film);       // Zapis do bazy
            filmy.add(film);           // Dodanie do tabeli
            wyczyscPola();             // Wyczyść pola formularza
            komunikatLabel.setText("Film dodany!");
        } catch (NumberFormatException e) { // NumberFormatException - wyjątek, który jest rzucany, gdy nie można przekonwertować tekstu na liczbę
            komunikatLabel.setText("Rok, czas i ocena musza byc poprawnymi liczbami.");
        } catch (Exception e) { // Exception - wyjątek, który jest rzucany, gdy występuje błąd podczas wykonywania metody
            komunikatLabel.setText("Blad dodawania filmu.");
        }
    }

    @FXML
    private void edytujFilm() {
        Film wybrany = filmTable.getSelectionModel().getSelectedItem(); // Pobranie wybranego filmu
        if (wybrany != null) {
            try {
                String tytul = tytulField.getText().trim();
                String rokText = rokField.getText().trim();
                String czasText = czasTrwaniaField.getText().trim();
                String ocenaText = ocenaField.getText().trim();
                String rezyser = rezyserCombo.getEditor().getText().trim();
                String kategoria = kategoriaCombo.getEditor().getText().trim();

                if (tytul.isEmpty() || rokText.isEmpty() || czasText.isEmpty() || ocenaText.isEmpty() || rezyser.isEmpty() || kategoria.isEmpty()) {
                    komunikatLabel.setText("Wszystkie pola muszą być wypełnione.");
                    return;
                }

                int rok = Integer.parseInt(rokText);
                int czas = Integer.parseInt(czasText);
                double ocena = Double.parseDouble(ocenaText);

                if (rok <= 0) {
                    komunikatLabel.setText("Nieprawidłowy rok.");
                    return;
                }

                if (czas <= 0) {
                    komunikatLabel.setText("Czas trwania musi być większy od zera.");
                    return;
                }

                if (ocena < 0.0 || ocena > 10.0) {
                    komunikatLabel.setText("Ocena musi być w zakresie 0 - 10");
                    return;
                }

                wybrany.setTytul(tytul);
                wybrany.setRok(rok);
                wybrany.setCzasTrwania(czas);
                wybrany.setOcena(ocena);
                wybrany.setRezyser(RezyserDAO.znajdzLubDodaj(rezyser));
                wybrany.setKategoria(KategoriaDAO.znajdzLubDodaj(kategoria));

                FilmDAO.edytuj(wybrany); // Zapis zmian w bazie
                filmTable.refresh();     // Odświeżenie tabeli
                wyczyscPola();
            } catch (NumberFormatException e) {
                komunikatLabel.setText("Rok, czas trwania i ocena muszą być liczbami.");
            } catch (Exception e) {
                komunikatLabel.setText("Błąd edycji: " + e.getMessage());
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


    @FXML
    private void pokazUsunRezysera() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/usunRezysera.fxml"));
            Stage stage = (Stage) rezyserCombo.getScene().getWindow();
            stage.setWidth(600);
            stage.setHeight(520);
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            komunikatLabel.setText("Blad ladowania widoku.");
        }
    }
    @FXML
    private void pokazUsunKategorit() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/usunKategorie.fxml"));
            Stage stage = (Stage) kategoriaCombo.getScene().getWindow();
            stage.setWidth(600);
            stage.setHeight(520);
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            komunikatLabel.setText("Blad ladowania widoku.");
        }
    }
    @FXML
    private void wczytajDane() {
        Film wybrany = filmTable.getSelectionModel().getSelectedItem();
        if (wybrany != null) {
            tytulField.setText(wybrany.getTytul());
            rokField.setText(String.valueOf(wybrany.getRok()));
            rezyserCombo.setValue(wybrany.getRezyser());
            kategoriaCombo.setValue(wybrany.getKategoria());
            ocenaField.setText(String.valueOf(wybrany.getOcena()));
            czasTrwaniaField.setText(String.valueOf(wybrany.getCzasTrwania()));
        }
    }


}
