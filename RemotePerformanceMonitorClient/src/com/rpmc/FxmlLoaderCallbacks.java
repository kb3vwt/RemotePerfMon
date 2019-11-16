package com.rpmc;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public interface FxmlLoaderCallbacks {
    URL getFXMLResource();

    default void load() {
        FXMLLoader loader = new FXMLLoader(getFXMLResource());
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
