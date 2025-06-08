package projektFilmy.Kontroler;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import projektFilmy.baza.UzytkownikDAO;

public class RejestracjaKontroler {

    @FXML private TextField loginField;
    @FXML private PasswordField hasloField;
    @FXML private PasswordField powtorzHasloField;
    @FXML private Label komunikatLabel;

    @FXML
    private void zarejestruj() {
        String login = loginField.getText().trim();
        String haslo = hasloField.getText().trim();
        String powtorz = powtorzHasloField.getText().trim();

        if (!czyPoprawneDane(login, haslo, powtorz)) {
            return;
        }

        if (UzytkownikDAO.zarejestruj(login, haslo)) {
            komunikatLabel.setText("Zarejestrowano pomyslnie. Kliknij 'Anuluj', aby wrocic.");
        } else {
            komunikatLabel.setText("Taki login juz istnieje.");
        }

    }

    private boolean czyPoprawneDane(String login, String haslo, String powtorzHaslo) {
        if (login.isEmpty() || haslo.isEmpty() || powtorzHaslo.isEmpty()) {
            komunikatLabel.setText("Wszystkie pola sa wymagane.");
            return false;
        }

        if (!login.matches("[A-Za-z0-9._-]{3,20}")) {
            komunikatLabel.setText("Login moze zawierac litery, cyfry, kropki, myslniki (3-20 znakow).");
            return false;
        }

        if (haslo.length() < 6 || !haslo.matches(".*\\d.*")) {
            komunikatLabel.setText("Haslo musi miec min. 6 znakow i zawierac cyfre.");
            return false;
        }

        if (!haslo.equals(powtorzHaslo)) {
            komunikatLabel.setText("Hasla nie sa takie same.");
            return false;
        }

        return true;
    }
    @FXML
    private void anuluj() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/logowanie.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginField.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            komunikatLabel.setText("Blad powrotu do logowania.");
        }
    }

    private void zamknijOkno() {
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.close();
    }
}
