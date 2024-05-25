package com.example.yyproje;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.yyproje.HelloApplication.connection;

public class UnuttumController {


    @FXML
    private Button button_Yenile;

    @FXML
    private TextField tfield_Kullanici;

    @FXML
    private TextField tfield_Mail;

    @FXML
    private TextField tfield_Sifre;




    @FXML
    void yenile(ActionEvent event) {
        String kullanici = tfield_Kullanici.getText();
        String email = tfield_Mail.getText();
        String sifre = tfield_Sifre.getText();

        try {

            HelloApplication.openDatabaseConnection();



            if (connection != null) {
                System.out.println("Veritabanına başarıyla bağlandı!");
                String sql = "UPDATE Kullanici SET KullaniciSifre = ? WHERE KullaniciAdi = ? AND KullaniciMail = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, sifre);
                preparedStatement.setString(2, kullanici);
                preparedStatement.setString(3, email);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Şifre başarıyla güncellendi!");
                    loadScene(event, "/com/example/yyproje/girisEkrani.fxml");
                } else {
                    System.out.println("Kullanıcı adı veya mail yanlış!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    void loadScene(ActionEvent event, String fxmlFile) throws IOException {
        URL fxmlLocation = getClass().getResource(fxmlFile);
        if (fxmlLocation == null) {
            throw new IOException("FXML dosyası bulunamadı: " + fxmlFile);
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}



