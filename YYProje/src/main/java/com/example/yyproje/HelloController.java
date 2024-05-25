package com.example.yyproje;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;


import java.io.IOException;


public class HelloController {

    @FXML
    private Button button_Analiz;

    @FXML
    private Button button_Ayarlar;

    @FXML
    private Button button_Oyna;
    @FXML
    void Analiz(ActionEvent event) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ogrAnaliz.fxml"));
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

    @FXML
    void Ayarlar(ActionEvent event) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("kelimeEkle.fxml"));
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


    @FXML
    void Oyna(ActionEvent event) throws IOException  {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("oyunuOyna.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage anaEkran = (Stage)((Node)event.getSource()).getScene().getWindow();
            anaEkran.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
