module com.example.taskmanegement {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens br.ufrn.imd.view to javafx.fxml;
    opens br.ufrn.imd.model to com.google.gson, javafx.fxml;

    exports br.ufrn.imd.view;
    exports br.ufrn.imd.model;
}