package br.ufrn.imd.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TaskApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/task.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Task Management Application");
        primaryStage.setScene(new Scene(root, 894, 639)); // Ajuste o tamanho conforme necessário
        // Bloquear maximização
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}