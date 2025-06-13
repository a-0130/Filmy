package projektFilmy.Kontroler;

import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import projektFilmy.baza.Rezyser;
import projektFilmy.baza.RezyserDAO;

public class UsunRezyseraKontroler {


    @FXML private ComboBox<Rezyser> rezyserComboBox;
    @FXML private TextField imieField;
    @FXML private TextField nazwiskoField;
    @FXML private Label komunikatLabel;

    @FXML
    public void initialize() {
        rezyserComboBox.getItems().addAll(RezyserDAO.pobierzWszystkich());
    }

    @FXML
    private void usunRezysera() {
        Rezyser rezyser = rezyserComboBox.getValue();

        if (rezyser != null) {
            String pelneImieNazwisko = rezyser.getImie() + " " + rezyser.getNazwisko();
            boolean usunieto = RezyserDAO.usun(pelneImieNazwisko);

            if (usunieto) {
                rezyserComboBox.getItems().remove(rezyser);
                pokazAlert("Usunięto reżysera: " + pelneImieNazwisko);
            } else {
                pokazAlert("Nie można usunąć reżysera; Ma przypisane filmy.");
            }
        }
    }
    @FXML
    private void dodajRezysera() {
        String imie = imieField.getText().trim();
        String nazwisko = nazwiskoField.getText().trim();

        if (!imie.isEmpty() && !nazwisko.isEmpty()) {
            if (!imie.matches("[A-Za-z \\-]{1,50}") ||
                    !nazwisko.matches("[A-Za-z \\-]{1,50}")) {
                pokazAlert("Imię i nazwisko mogą zawierać tylko litery oraz myślniki. max 50 znakow.");
                return;
            }
            String pelne = imie + " " + nazwisko;
            boolean dodano = RezyserDAO.dodaj(pelne);
            if (dodano) {
                rezyserComboBox.getItems().clear();
                rezyserComboBox.getItems().addAll(RezyserDAO.pobierzWszystkich());
                imieField.clear();
                nazwiskoField.clear();
                pokazAlert("Dodano reżysera: " + pelne);
            } else {
                pokazAlert("Reżyser " + pelne + " już istnieje.");
            }
        } else {
            pokazAlert("Uzupełnij oba pola, aby dodać reżysera.");
        }
    }


    private void pokazAlert(String tekst) {
        komunikatLabel.setText(tekst);
    }
    @FXML
    private void anulujRezyser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/widok.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) rezyserComboBox.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setScene(scene);

        } catch (Exception e) {
            komunikatLabel.setText("Blad powrotu do widok.");
        }
    }
}
