package com.rpmc.client.ui;

import com.rpmc.FxmlLoaderCallbacks;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;

public class RemotePerfClientRowController extends GridPane implements FxmlLoaderCallbacks {

    @FXML
    private ProgressBar valueProgBar;

    @FXML
    private Label labelField;

    @FXML
    void initialize() {

    }

    public RemotePerfClientRowController(String name, double val) {
        load();

        labelField.setText(name);
        valueProgBar.setProgress(val);
    }

    @Override
    public URL getFXMLResource() {
        return getClass().getResource("RemotePerfClientRowController.fxml");
    }

    public String getName() {
        return labelField.getText();
    }

    public void setValue(double value) {
        System.out.println("Updated " + labelField.getText() + " to value " + value);
        valueProgBar.setProgress(value);
    }
}
