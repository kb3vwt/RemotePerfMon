package com.rpmc.client.ui;

import com.rpmc.FxmlLoaderCallbacks;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;

public class HostStatsRowContainer extends VBox implements FxmlLoaderCallbacks {
    @FXML
    private Label hostnameLabel;

    @FXML
    private VBox statVBox;

    @FXML
    void initialize() {

    }

    public HostStatsRowContainer(String hostname) {
        load();
        this.hostnameLabel.setText(hostname);
    }

    @Override
    public URL getFXMLResource() {
        return getClass().getResource("HostRowContainer.fxml");
    }

    private RemotePerfClientRowController getRow(String label) {
        Optional<RemotePerfClientRowController> optRow = statVBox.getChildren().stream()
                .filter(node -> node instanceof RemotePerfClientRowController)
                .map(node -> (RemotePerfClientRowController) node)
                .filter(r -> r.getName().equals(label))
                .findFirst();

        if(optRow.isPresent())
            return optRow.get();

        return null;
    }

    public String getHost() {
        return hostnameLabel.getText();
    }

    public void addOrUpdateRow(String label, double value) {
        RemotePerfClientRowController existingRow = getRow(label);

        if(existingRow != null) {
            System.out.println("Updating existing stat row for " + label + " on host " + getHost());
            existingRow.setValue(value);
        }
        else {
            System.out.println("New stat row for " + label + " on host " + getHost());
            RemotePerfClientRowController newRow = new RemotePerfClientRowController(label,value);
            statVBox.getChildren().add(newRow);
        }
    }
}