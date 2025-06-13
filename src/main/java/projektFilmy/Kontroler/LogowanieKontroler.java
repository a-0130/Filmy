package projektFilmy.Kontroler;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import projektFilmy.baza.UzytkownikDAO;

public class LogowanieKontroler {

    @FXML private TextField loginField;
    @FXML private PasswordField hasloField; // pole tekstowe (ukryte znaki) hasła, w którym użytkownik wpisuje hasło
    @FXML private Label komunikatLabel;

    @FXML
    private void zaloguj() {
        String login = loginField.getText().trim(); // Pobiera tekst wpisany przez użytkownika w polu login i usuwa spacje z początku i końca.
        String haslo = hasloField.getText().trim();

        if (!czyPoprawneDane(login, haslo)) {
            return;
        }

        if (UzytkownikDAO.zaloguj(login, haslo)) {
            otworzWidokGlownegoOkna();
        } else {
            komunikatLabel.setText("Niepoprawny login lub haslo albo uzytkownik nie istnieje.");
        }
    }

    @FXML
    private void zarejestruj() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/rejestracja.fxml")); // laduje plik fxml do rejestracji
            Stage stage = (Stage) loginField.getScene().getWindow(); // pobiera scenę z okna logowania aby mozna zamienic 
            stage.setScene(new Scene(root)); // ustawia scenę z pliku fxml do rejestracji
        } catch (Exception e) {
            komunikatLabel.setText("Blad otwierania okna rejestracji."); 
        }
    }

    private boolean czyPoprawneDane(String login, String haslo) {
        if (login.isEmpty() || haslo.isEmpty()) {
            komunikatLabel.setText("Login i haslo sa wymagane.");
            return false;
        }

        if (!login.matches("[A-Za-z0-9._-]{3,20}")) { // matches() - sprawdza, czy tekst pasuje do wzorca
            komunikatLabel.setText("Login moze zawierac litery, cyfry, myślniki, podkreslniki (3-20 znakow).");
            return false;
        }

        if (haslo.length() < 6 || !haslo.matches(".*\\d.*")) {
            komunikatLabel.setText("Haslo musi miec min. 6 znakow i zawierac co najmniej jedna cyfre.");
            return false;
        }

        return true;
    }

    private void otworzWidokGlownegoOkna() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/widok.fxml"));
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            komunikatLabel.setText("Blad ladowania widoku.");
        }
    }
}
