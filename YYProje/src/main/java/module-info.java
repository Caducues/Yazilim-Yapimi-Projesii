module com.example.yyproje {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires jdk.jdi;


    opens com.example.yyproje to javafx.fxml;
    exports com.example.yyproje;
}