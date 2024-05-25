package com.example.yyproje;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    public static Connection connection;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("girisEkrani.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

     public void init() throws Exception{
         openDatabaseConnection();
     }
    public void stop() throws Exception{
        closeDatabaseConnection();
    }

    public static void openDatabaseConnection() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://DESKTOP-V5VS053;databaseName=Quiz;integratedSecurity=true;encrypt=true;trustServerCertificate=true";
       connection = DriverManager.getConnection(connectionUrl);

        if (connection != null) {

        } else {
            System.out.println("Veritabanına bağlanırken hata oluştu!");
        }
    }
    public static Connection getConnection() {
        return connection;
    }

    public static void closeDatabaseConnection() throws SQLException {
        // Veritabanı bağlantısını kapat
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Veritabanı bağlantısı kapatıldı.");
        }
    }


    public static void main(String[] args) {

        launch();

    }
}