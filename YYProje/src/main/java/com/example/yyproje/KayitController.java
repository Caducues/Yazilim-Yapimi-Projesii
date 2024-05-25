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
import java.net.URL;
import java.sql.*;

import java.io.IOException;

import static com.example.yyproje.HelloApplication.connection;

public class KayitController {


    @FXML
    private Button button_Kayit;

    @FXML
    private TextField tfield_Kullanici;

    @FXML
    private TextField tfield_Sifre;

    @FXML
    private TextField tfield_Sifre2;

    @FXML
    private TextField tfield_Email;
    @FXML
    void kayit(ActionEvent event) throws IOException, SQLException {
        String Kullanici = tfield_Kullanici.getText();
        String Sifre = tfield_Sifre.getText();

        String Email = tfield_Email.getText();

        try {
            if (kulanici_varmi(Kullanici) == true) {
                System.out.println("Bu isimde kullanıcı var!. Başka kullanıcı ismi seçmelisiniz.");
            }
            else {
                HelloApplication.openDatabaseConnection();
                if (connection != null) {
                    System.out.println("Veritabanına başarıyla bağlandı!");

                    String sql = "Insert Into Kullanici(KullaniciAdi, KullaniciMail,KullaniciSifre) Values(?,?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, Kullanici);
                    preparedStatement.setString(3, Sifre);
                    preparedStatement.setString(2, Email);
                    int affectedRows = preparedStatement.executeUpdate();
                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    if (rs.next()) {
                        int new_kullanici_di = rs.getInt(1);
                        kullanici_ayar_ekle(new_kullanici_di);
                        System.out.println("Giriş başarılı!");
                        loadScene(event, "/com/example/yyproje/girisEkrani.fxml");
                    } else {
                        System.out.println("Kullanıcı adı veya şifre yanlış!");
                    }
                }
            }
            } catch(SQLException e){
                System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
                e.printStackTrace();
            }
        finally{
            if (connection != null) {
                try {
                    HelloApplication.closeDatabaseConnection();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            }


    }

    boolean kulanici_varmi(String kullanici_adi) throws SQLException {
        try {
             //  System.out.println("2");
            HelloApplication.openDatabaseConnection();
            if (connection != null) {
                String sql = "Select * from Kullanici Where KullaniciAdi = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, kullanici_adi);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {

                    if (resultSet.getInt("KullaniciID") > 0 )
                    {
                        HelloApplication.closeDatabaseConnection();
                        return true;}
                    else {
                        HelloApplication.closeDatabaseConnection();
                        return false;}
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        finally {
            if (connection != null) {
                try {
                    HelloApplication.closeDatabaseConnection();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    void kullanici_ayar_ekle(int kullanici_id) throws SQLException {
        try {
            HelloApplication.openDatabaseConnection();
            if (connection != null) {
                //System.out.println("Veritabanına başarıyla bağlandı!");
                String sql = "Insert Into Ayarlar1(KullaniciId) Values(?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, kullanici_id);

                int rowsAffected = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
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

