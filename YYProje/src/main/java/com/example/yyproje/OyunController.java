package com.example.yyproje;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;

import com.sun.jdi.Value;
import java.net.URL;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.yyproje.HelloApplication.connection;


public class OyunController {

    int userId;
    String Cevap;
    int KelimeId;
    String Cumle;
    String ImgName;
    LocalDate bugun = LocalDate.now();
    int sorSayi, aktif_soru_id;
    Random random = new Random();
    Date SorulmaTarihi;
    List<Integer> SecilenKelimeler;


    @FXML
    private Button button_Cevap;

    @FXML
    private Button button_Geri;

    @FXML
    private ImageView image_WordImg;

    @FXML
    private Label label_Cumle;

    @FXML
    private Label label_Soru;

    @FXML
    private TextField tfield_Tahmin;


    public OyunController() throws SQLException {
        System.out.println("Oyuna girildi");
        userId = UserSession.getInstance().getUserId();
        // Soru Sayısını çekme
        try {
            HelloApplication.openDatabaseConnection();
            if (connection != null) {
                System.out.println("Veritabanına başarıyla bağlandı 1!");
                String sql = "Select SoruSayisi from Ayarlar1 Where KullaniciId= ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    sorSayi = (resultSet.getInt("SoruSayisi"));
                }
                else
                {
                    System.out.println("Hata 1");
                    return;
                }

                SecilenKelimeler = new ArrayList<>();
                sql = "Select * from Kelime";
                PreparedStatement ps3 = connection.prepareStatement(sql);
                resultSet = ps3.executeQuery();

                while (resultSet.next()) {
                    KelimeId = resultSet.getInt("KelimeId");
                    //System.out.println("user id -> " + userId + "   kelime_id ->" +kelime_id);

                    boolean sonuc = SoruUygunMu(userId, KelimeId);
                    if (sonuc) {
                        SecilenKelimeler.add(resultSet.getInt("KelimeId"));
                        System.out.println("Seçilen kelime id:" + resultSet.getInt("KelimeId"));
                        sorSayi --;
                        if (sorSayi == 0 ) break;
                    }
                }
                aktif_soru_id = 0;


                SoruSor();
                System.out.println("Soru Soruluyor");
            }
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Bağlantıyı kapat
            if (connection != null) {
                try {
                    HelloApplication.closeDatabaseConnection();
                    System.out.println("Sistem kapanıyor hata yok ");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    boolean SoruUygunMu(int kullaniciid, int kelimeid)
    {
        try {
            HelloApplication.openDatabaseConnection();
            if (connection != null) {
                String sql = "Select BasariTarih from Basari Where KullaniciId= ? and KelimeId= ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, kullaniciid);
                preparedStatement.setInt(2, kelimeid);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    SorulmaTarihi = (resultSet.getDate("BasariTarih"));
                    //System.out.println(SorulmaTarihi);
                    if (SorulmaTarihi == null) return true;
                    else {
                        switch (resultSet.getFetchSize()) {
                            case 1:
                                if (SorulmaTarihi.toLocalDate().isBefore(LocalDate.now().plusDays(-1))) {
                                    return true;
                                } else return false;
                            case 2:
                                if (SorulmaTarihi.toLocalDate().isBefore(LocalDate.now().plusDays(-7))) {
                                    return true;
                                } else return false;

                            case 3:
                                if (SorulmaTarihi.toLocalDate().isBefore(LocalDate.now().plusMonths(-1))) {
                                    return true;
                                } else return false;
                            case 4:
                                if (SorulmaTarihi.toLocalDate().isBefore(LocalDate.now().plusMonths(-6))) {
                                    return true;
                                } else return false;
                            case 5:
                                if (SorulmaTarihi.toLocalDate().isBefore(LocalDate.now().plusYears(-1))) {
                                    return true;
                                } else return false;
                            default:
                                return false;
                        }
                    }
                }
                else
                {
                    return  true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }

    void SoruSor() {
        try {
            HelloApplication.openDatabaseConnection();
            if (connection != null) {
                String sql = "SELECT*FROM Kelime WHERE KelimeId = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, SecilenKelimeler.get(aktif_soru_id));
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String soru = resultSet.getString("Kelime_En");
                    Cevap = resultSet.getString("Kelime_Tr");
                    KelimeId = resultSet.getInt("KelimeId");
                    ImgName = resultSet.getString("Resim");
                    Cumle = resultSet.getString("Cumle");


                    System.out.println(KelimeId);
                    System.out.println(ImgName);
                    System.out.println(Cumle);

                    //String AbsolutePath = new File(".").getAbsolutePath();
                    //Path path = Paths.get(getClass().getResource("/Img/" + ImgName).u);
                    //System.out.println(path);
                    //if (Files.exists(path)) {

                    //}
                    URL resource = getClass().getResource("/Img/" + ImgName);
                    if (resource != null) {
                        Image image = new Image(getClass().getResource("/Img/" + ImgName).toExternalForm());
                        Platform.runLater(() -> {
                            image_WordImg.setImage(image);
                        });
                    }

                    Platform.runLater(() -> {label_Soru.setText(soru); });
                    Platform.runLater(() -> {tfield_Tahmin.setText(""); });
                    Platform.runLater(()->{label_Cumle.setText(Cumle);});
                } else {
                    System.out.println("Kelime getirilemedi.");
                }
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
    void cevap(ActionEvent event) throws SQLException {
        if(tfield_Tahmin.getText().equals(Cevap))
        {
            System.out.println("Dogru");
            try {
                HelloApplication.openDatabaseConnection();
                if (connection != null) {
                    System.out.println("Veritabanına başarıyla bağlandı!");
                    String sql = "Insert Into Basari(KullaniciId,KelimeId,BasariTarih) Values(?,?,?) ";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, userId);
                    preparedStatement.setInt(2, KelimeId);
                    preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Kayıt başarıyla eklendi!");
                        if (aktif_soru_id< SecilenKelimeler.size()-1) {
                            aktif_soru_id++;
                            SoruSor();
                        }
                        else
                        {
                            System.out.println("Kelimeler Bitti");
                            Platform.runLater(() -> {label_Soru.setText("Bitti"); });
                            Platform.runLater(() -> {image_WordImg.setImage(null); ;});
                            Platform.runLater(() -> {tfield_Tahmin.setVisible(false); });
                            Platform.runLater(() -> {button_Cevap.setVisible(false); });
                            Platform.runLater(() -> {label_Cumle.setVisible(false); });

                        }
                    }
                    else {
                        System.out.println("Kayıt eklenirken bir hata oluştu!");
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
        else {
            System.out.println("Yanlış");
        }
    }

    @FXML
    void geri(ActionEvent event) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("anaEkran.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage oyunuOyna = (Stage)((Node)event.getSource()).getScene().getWindow();
            oyunuOyna.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}
