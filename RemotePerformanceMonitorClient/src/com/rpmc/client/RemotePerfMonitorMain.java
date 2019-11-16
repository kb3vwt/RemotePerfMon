package com.rpmc.client;

import com.rpmc.client.ui.RemotePerfClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RemotePerfMonitorMain extends Application {

    RemotePerfClientController mainController;

    public static void main(String[] argv) {
        launch(argv);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainController = new RemotePerfClientController();

        primaryStage.setTitle("PerformanceMonitor");
        FXMLLoader loader = new FXMLLoader(RemotePerfClientController.class.getResource("RemotePerfClientController.fxml"));
        loader.setController(mainController);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.show();
    }
}
