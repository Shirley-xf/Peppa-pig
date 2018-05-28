package app.controllers;


import app.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.awt.*;

public class TypeMenuController {

    @FXML
    public Pane typeMenuPane;

    public static void getFilmByType(String s) {
        System.out.println("getFilmByType");
        Main.switchToListMenu();
    }

    public void goBack() {
        System.out.println("goBack");
    }

    public void goToMainMenu() {
        System.out.println("goToMainMenu");
    }
}
