package app.controllers;

import app.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Start menu controller controls the start menu (startMenu.fxml), which launches the first.
 */
public class StartMenuController implements Initializable {

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
    @FXML private ImageView logo;


    /**
     * Go to the type menu.
     */
    public void goToTypeMenu() {

        App.getPrimaryStage().setScene(App.getsTypeMenuController().getScene());

    }

    /**
     * Go to the language menu.
     */
    public void goToLanguageMenu() {
        App.getPrimaryStage().setScene(App.getsLanguageMenuController().getScene());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image(("file:" + App.getIconPath() + File.separator + "logo.png").replace(" ", "%20"));
        logo.setImage(img);
    }
}
