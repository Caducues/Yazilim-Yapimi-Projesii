package com.example.yyproje;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;


import java.io.File;

import java.io.IOException;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;


import static com.example.yyproje.HelloApplication.connection;


public class AyarController {



    int kelimeSayacı=8;
    int Sayi;

    public String ek_resim;
    int userId;
    int id;
    File selectedFile;


    public AyarController() throws IOException, SQLException {
        userId = UserSession.getInstance().getUserId();
        System.out.println(userId);

        SayacAyarlari();
    }





    @FXML
    private Button button_Arttir;

    @FXML
    private Button button_Azalt;

    @FXML
    private Button button_Geri;

    @FXML
    private Button button_Kaydet;

    @FXML
    private ImageView eklenenResim;

    @FXML
    private Label label_Ksayisi;

    @FXML
    private TextField text_Cumle;

    @FXML
    private Hyperlink link_Ekle;

    @FXML
    private TextField text_Ing;

    @FXML
    private TextField text_Tur;

    @FXML
    void Arttir(ActionEvent event) {
        Sayi++;
        label_Ksayisi.setText(String.valueOf(Sayi));
    }


    @FXML
    void Azalt(ActionEvent event) {
        if(Sayi>0 )
        {
            Sayi--;
            label_Ksayisi.setText(String.valueOf(Sayi));
        }


    }

    @FXML
    void Ekle(ActionEvent event)  throws IOException {


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a file");
        fileChooser.setInitialDirectory(new File("C:\\")); // Opsiyonel: Başlangıç dizinini ayarlayabilirsiniz.
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG Images", "*.jpg"));

        Stage stage = (Stage) link_Ekle.getScene().getWindow();
         selectedFile = fileChooser.showOpenDialog(stage);



        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            Image image = new Image(imagePath);

            System.out.println(imagePath);
            eklenenResim.setImage(image);
            eklenenResim.setFitHeight(200);
            eklenenResim.setFitWidth(300);

            link_Ekle.setVisible(false);
        } else {
            System.out.println("No file has been selected");
        }

    }

    private String getNextImageName() {
        kelimeSayacı++;
        return kelimeSayacı + ".jpg";
    }




    private void copyFileToResources(File sourceFile , String newFileName) throws IOException {

        String resourcesPath = "src/main/resources/Img";
        File destDir = new File(resourcesPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        File destFile = new File(destDir, newFileName);


        Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File copied");
    }



    @FXML
    void Kaydet(ActionEvent event)   throws IOException {
        String Ingilizce = text_Ing.getText();
        String Turkce = text_Tur.getText();
        String Cumle = text_Cumle.getText();

        if (Ingilizce.isEmpty() && Turkce.isEmpty()  ) {
            System.out.println("Ayarlar soru sayıcı update ediliyor.");
            Soru_Sayisi_Update(Sayi, userId);
        }
        else {
            if (!Ingilizce.isEmpty() && !Turkce.isEmpty() ) {
                try
                {
                    HelloApplication.openDatabaseConnection();
                    if (connection != null) {
                        System.out.println("Veritabanına başarıyla bağlandı!");
                        String sql = "Insert Into Kelime(Kelime_En,Kelime_Tr,Resim,Cumle) Values(?,?,?,?) ";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

                        preparedStatement.setString(1, Ingilizce);
                        preparedStatement.setString(2, Turkce);
                        preparedStatement.setString(3, getNextImageName());
                        preparedStatement.setString(4, Cumle);

                        int affectedRows = preparedStatement.executeUpdate();
                        ResultSet rs = preparedStatement.getGeneratedKeys();
                        if (rs.next()) {
                            id = rs.getInt(1);
                            copyFileToResources(selectedFile,String.valueOf(id+".jpg"));


                            System.out.println(id);
                        }
                        if (affectedRows > 0) {
                            System.out.println("Kayıt başarıyla eklendi!");


                        } else {
                            System.out.println("Kayıt eklenirken bir hata oluştu!");
                        }

                    }

                } catch (SQLException e) {
                    System.out.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
                    e.printStackTrace();

                }
            }
            else {
                System.out.println("Kelime eklemek için ingilizce, türkçe ve resim bilgileri olmalıdır.");
            }
        }

    }

    void Soru_Sayisi_Update(int sayi, int user_id)
    {
        try {
            System.out.println("5342");

            HelloApplication.openDatabaseConnection();
            if (connection != null) {
                System.out.println("Veritabanına başarıyla bağlandı!");
                String sql = "Update Ayarlar1 Set SoruSayisi = ? Where KullaniciId = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, sayi);
                preparedStatement.setInt(2, userId);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Kayıt başarıyla eklendi!");

                } else {
                    System.out.println("Kayıt eklenirken bir hata oluştu!");
                }
            }
            eklenenResim.setImage(null);
            link_Ekle.setVisible(true);
            text_Ing.setText(null);
            text_Tur.setText(null);

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
    void Geri(ActionEvent event)  throws IOException{
        try {
            Parent root = FXMLLoader.load(getClass().getResource("anaEkran.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage anaEkran = (Stage)((Node)event.getSource()).getScene().getWindow();
            anaEkran.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    void SayacAyarlari() throws IOException {

        try {
            HelloApplication.openDatabaseConnection();
            if (connection != null) {
                System.out.println("Veritabanına başarıyla bağlandı!");
                String sql = "SELECT SoruSayisi FROM Ayarlar1 WHERE KullaniciId = ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    System.out.println("Giriş başarılı!");

                    Sayi = (resultSet.getInt("SoruSayisi"));
                    System.out.println(Sayi);
                    Platform.runLater(() -> label_Ksayisi.setText(String.valueOf(Sayi)));



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