package app.controllers;


import app.Main;
import javafx.fxml.*;
import javafx.scene.layout.Pane;


public class TypeMenuController {

    @FXML
    public Pane typeMenuPane;


    //should be refactored
    public static void getFilmByType(String s) {
        System.out.println("getFilmByType" + " " + s);
        Main.switchToListMenu();
        // query by text s
    }

    public void goBack() {
        System.out.println("goBack");
    }

    public void goToMainMenu() {
        System.out.println("goToMainMenu");
    }
}
