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
import java.util.Optional;

public class RemotePerfClientController extends BorderPane implements FxmlLoaderCallbacks {

    @FXML
    private VBox hostsVBox;
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

        putOrUpdateExistingRow(newv.getHost(),memoryLabel,memPerc);

        newv.getProcessorUtilization().forEach(core -> {
            String coreLabel = String.format("Core %2d", core.getId());
            double used = core.getUtilizationPercent();

            putOrUpdateExistingRow(newv.getHost(), coreLabel,used);
        });

    }

    private void putOrUpdateExistingRow(String hostName, String rowLabel, double value) {
        Optional<HostStatsRowContainer> optHost = hostsVBox.getChildren().stream()
                .filter(item -> item instanceof HostStatsRowContainer)
                .map(item -> (HostStatsRowContainer) item)
                .filter(hostRow -> hostRow.getHost().equals(hostName)).findFirst();

        if(optHost.isPresent()) {
            System.out.println("Updating Existing Host " + hostName);
            HostStatsRowContainer hostRow = optHost.get();
            hostRow.addOrUpdateRow(rowLabel,value);
        }
        else {
            System.out.println("Found new Host " + hostName);
            HostStatsRowContainer newRow = new HostStatsRowContainer(hostName);
            newRow.addOrUpdateRow(rowLabel,value);

            hostsVBox.getChildren().add(newRow);
        }


    }

    private void hideStats() {
        hostsVBox.getChildren().clear();
    }

    @Override
    public URL getFXMLResource() {
        return getClass().getResource("RemotePerfClientController.fxml");
    }

    public RemotePerfClientController() {

    }
}
