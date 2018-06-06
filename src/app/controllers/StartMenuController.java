package app.controllers;

import app.Main;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class StartMenuController {

    public GridPane getStartGridPane() {
        return startGridPane;
    }

    public Scene getScene() {
        if (scene == null) {
            scene = new Scene(this.startGridPane);
        }
        return scene;
    }

    private Scene scene;
    @FXML private GridPane startGridPane;
    public void goToTypeMenu() {
        Main.getPrimaryStage().setScene(Main.getsTypeMenuController().getScene());
    }

    public void goToLanguageMenu() {
        Main.getPrimaryStage().setScene(Main.getsLanguageMenuController().getScene());
    }
}
