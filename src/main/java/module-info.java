module org.example.clientservergame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.google.gson;
    requires java.sql;

    opens org.example.clientservergame to javafx.fxml, com.google.gson;
    exports org.example.clientservergame;
}