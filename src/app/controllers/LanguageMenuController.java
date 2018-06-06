package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.File;

public class LanguageMenuController {
    public AnchorPane getLanguagePane() {
        return languagePane;
    }

    public Scene getScene() {
        if (scene == null) {
            scene = new Scene(this.languagePane);
        }
        return scene;
    }

    private Scene scene;
    @FXML private AnchorPane languagePane;
}
