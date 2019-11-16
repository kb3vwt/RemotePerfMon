package com.rpmc.client.ui;

import com.rpmc.FxmlLoaderCallbacks;
import com.rpmc.client.RemoteStatsClient;
import com.rpmc.model.ComputerState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RemotePerfClientController extends BorderPane implements FxmlLoaderCallbacks {

    @FXML
    private VBox statsVBox;
    @FXML
    private TextField groupTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private ToggleButton listenToggleButton;

    private RemoteStatsClient client;

    @FXML
    void initialize() {
        groupTextField.disableProperty().bind(listenToggleButton.selectedProperty());
        portTextField.disableProperty().bind(listenToggleButton.selectedProperty());

        listenToggleButton.selectedProperty().addListener((obs,oldv,newv) -> {
            if(newv) {
                try {
                    client = new RemoteStatsClient(groupTextField.getText(),Integer.parseInt(portTextField.getText()));

                    showStats();



                    client.start();
                } catch (IOException e) {
                    Alert a = new Alert(Alert.AlertType.ERROR,"Failed to listen on selected address and port");
                    a.showAndWait();
                    listenToggleButton.setSelected(false);
                }
            }
            else if(oldv && !newv) {
                hideStats();
                client.stop();
            }
        });
    }

    private void showStats() {
        client.getCurrentState().addListener((obs,oldv,newv) -> {
            Platform.runLater(() -> {
                System.out.println("New State Seen");
                addOrUpdate(newv);
            });
        });
    }

    private void addOrUpdate(ComputerState newv) {
        double usedGiB = newv.getMemoryUtilization().getInUse();
        double totalGiB = newv.getMemoryUtilization().getTotalGiB();
        String memoryLabel = String.format("Memory",usedGiB,totalGiB);
        double memPerc = usedGiB / totalGiB;

        putOrUpdateExistingRow(memoryLabel,memPerc);

        newv.getProcessorUtilization().forEach(core -> {
            String coreLabel = String.format("Core %2d", core.getId());
            double used = core.getUtilizationPercent();

            putOrUpdateExistingRow(coreLabel,used);
        });

    }

    private void putOrUpdateExistingRow(String rowLabel, double value) {
        Optional<RemotePerfClientRowController> optRow = statsVBox.getChildren().stream()
                .filter(item -> item instanceof RemotePerfClientRowController)
                .map(item -> (RemotePerfClientRowController) item)
                .filter(row -> row.getName().equals(rowLabel)).findFirst();

        if(optRow.isPresent()) {
            optRow.get().setValue(value);
        }
        else {
            statsVBox.getChildren().add(new RemotePerfClientRowController(rowLabel,value));
        }
    }

    private void hideStats() {
        statsVBox.getChildren().clear();
    }

    @Override
    public URL getFXMLResource() {
        return getClass().getResource("RemotePerfClientController.fxml");
    }

    public RemotePerfClientController() {

    }
}
