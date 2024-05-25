package com.example.yyproje;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.example.yyproje.HelloApplication.connection;

public class GirisController {


    @FXML
    private Button button_Giris;

    @FXML
    private TextField tfield_Kullanici;

    @FXML
    private TextField tfield_Sifre;

    @FXML
    private Button button_Kayit;

    @FXML
    private Hyperlink link_unuttum;


    @FXML
    void unuttum(ActionEvent event) throws IOException {
        loadScene(event, "/com/example/yyproje/unuttum.fxml");
    }



    @FXML
    void giris(ActionEvent event) throws IOException {
        String kullanici = tfield_Kullanici.getText();
        String sifre = tfield_Sifre.getText();
        try {
            HelloApplication.openDatabaseConnection();
            if (connection != null) {
                System.out.println("Veritabanına başarıyla bağlandı!");
                String sql = "SELECT KullaniciId FROM Kullanici WHERE KullaniciAdi = ? AND KullaniciSifre = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, kullanici);
                preparedStatement.setString(2, sifre);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    System.out.println("Giriş başarılı!");

                    int Id = (resultSet.getInt("KullaniciId"));
                    UserSession.getInstance().setUserId(Id);
                    System.out.println(Id);


                    loadScene(event, "/com/example/yyproje/anaEkran.fxml");
                } else {
                    System.out.println("Kullanıcı adı veya şifre yanlış!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
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
    void kayit(ActionEvent event) throws IOException {
        loadScene(event, "/com/example/yyproje/kayitol.fxml");
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
