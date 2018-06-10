package app.controllers;

import app.Main;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

/**
 * Start menu controller controls the start menu (startMenu.fxml), which launches the first.
 */
public class StartMenuController {

    /**
     * Gets the pane, which is a grid pane.
     *
     * @return the start grid pane
     */
    public GridPane getStartGridPane() {
        return startGridPane;
    }

    /**
     * Gets scene.
     *
     * @return the scene
     */
    public Scene getScene() {
        if (scene == null) {
            scene = new Scene(this.startGridPane);
        }
        return scene;
    }

    private Scene scene;
    @FXML private GridPane startGridPane;

    /**
     * Go to the type menu.
     */
    public void goToTypeMenu() {
        Main.getPrimaryStage().setScene(Main.getsTypeMenuController().getScene());
    }

    /**
     * Go to the language menu.
     */
    public void goToLanguageMenu() {
        Main.getPrimaryStage().setScene(Main.getsLanguageMenuController().getScene());
    }
}
