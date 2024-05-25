package com.example.yyproje;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.yyproje.HelloApplication.connection;


public class AnalizController {


    @FXML
    private Label label_Dogru;

    @FXML
    private Label label_SBDogru;

    @FXML
    private Label label_Yanlış;

    int userId = UserSession.getInstance().getUserId();
    int Basarisayi;
    int KelimeSayisi;

    @FXML
    private Button button_Geri2;

    public AnalizController() throws SQLException {
        userId = UserSession.getInstance().getUserId();

        try {
            HelloApplication.openDatabaseConnection();
            if (connection != null) {
                System.out.println("Veritabanına başarıyla bağlandı 1!");

                // İlk SQL sorgusu
                String sql1 = "SELECT COUNT(*) AS KelimeSayi FROM Kelime";
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                if (resultSet1.next()) {
                    KelimeSayisi = resultSet1.getInt("KelimeSayi");
                } else {
                    System.out.println("Hata 1");
                    return;
                }

                // İlk resultSet ve preparedStatement'i kapat
                resultSet1.close();
                preparedStatement1.close();

                // İkinci SQL sorgusu
                String sql2 = "SELECT COUNT(BasariTarih) AS BasariSayi FROM Basari WHERE KullaniciId = ?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setInt(1, userId);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                if (resultSet2.next()) {
                    Basarisayi = resultSet2.getInt("BasariSayi");
                    System.out.println(KelimeSayisi);
                    System.out.println(Basarisayi);
                    Platform.runLater(() -> {label_Dogru.setText(String.valueOf(Basarisayi)); });
                    Platform.runLater(() -> {label_Yanlış.setText(String.valueOf(KelimeSayisi));});
                    double ort = (double) KelimeSayisi / Basarisayi;
                    System.out.println(ort);
                    Platform.runLater(() -> {
                        label_SBDogru.setText(String.format("%.2f", ort));
                    });
                }

                // İkinci resultSet ve preparedStatement'i kapat
                resultSet2.close();
                preparedStatement2.close();
            }
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Bağlantıyı kapat
            if (connection != null) {
                try {
                    HelloApplication.closeDatabaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void Geri(ActionEvent event) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("anaEkran.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage ogrAnaliz = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ogrAnaliz.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
