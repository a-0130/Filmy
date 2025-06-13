package projektFilmy.Kontroler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import projektFilmy.baza.Kategoria;
import projektFilmy.baza.KategoriaDAO;

public class UsunKategorieKontroler {

    @FXML private ComboBox<Kategoria> kategoriaComboBox;
    @FXML private TextField nazwaField;
    @FXML private Label komunikatLabel;

    @FXML
    public void initialize() {
        kategoriaComboBox.getItems().addAll(KategoriaDAO.pobierzWszystkie());
    }

    @FXML
    private void dodajKategorie() {
        String nazwa = nazwaField.getText().trim();

        if (!nazwa.isEmpty()) {

            if (!nazwa.matches("[A-Za-z \\-]{1,50}") ) {
                komunikatLabel.setText("Kategoria moze zawierać tylko litery oraz myślniki. max 50 znakow");
                return;
            }
            boolean dodano = KategoriaDAO.dodaj(nazwa);
            if (dodano) {
                kategoriaComboBox.getItems().clear();
                kategoriaComboBox.getItems().addAll(KategoriaDAO.pobierzWszystkie());
                nazwaField.clear();
                komunikatLabel.setText("Dodano kategorię: " + nazwa);
            } else {
                komunikatLabel.setText("Kategoria \"" + nazwa + "\" już istnieje.");
            }
        } else {
            komunikatLabel.setText("Wpisz nazwę kategorii.");
        }
    }

    @FXML
    private void usunKategorie() {
        Kategoria kategoria = kategoriaComboBox.getValue();

        if (kategoria != null) {
            boolean usunieto = KategoriaDAO.usun(kategoria.getNazwa());

            if (usunieto) {
                kategoriaComboBox.getItems().remove(kategoria);
                komunikatLabel.setText("Usunięto kategorię: " + kategoria.getNazwa());
            } else {
                komunikatLabel.setText("Nie można usunąć – kategoria przypisana do filmu.");
            }
        }
    }

    @FXML
    private void anulujKategorie() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/widok.fxml"));
            Stage stage = (Stage) kategoriaComboBox.getScene().getWindow();
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            komunikatLabel.setText("Błąd powrotu do widoku.");
        }
    }
}
