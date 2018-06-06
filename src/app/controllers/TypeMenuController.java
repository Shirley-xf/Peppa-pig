package app.controllers;


import app.Main;
import app.datatype.Film;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import utils.FilmInfoParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;


public class TypeMenuController {

    public GridPane getTypeMenuPane() {
        return typeMenuPane;
    }

    public Scene getScene() {
        if (scene == null) {
            scene = new Scene(this.typeMenuPane);
        }
        return scene;
    }

    private Scene scene;
    @FXML private GridPane typeMenuPane;


    public void getFilmByType(String text) {
        System.out.println("getFilmByType " +text);
        goToListMenuAndShow(text);
    }

    public void goToListMenuAndShow(String type) {
        Stage ps = Main.getPrimaryStage();
        SplitPane sp_pane = Main.getmListMenuController().getFilmListSplitPane();
        ps.setScene(Main.getmListMenuController().getScene());
        List<Film> sFilmLinkedList = Main.getmListMenuController().setUpFilmByType(type);
        ListView<Film> list_view = (ListView<Film>) ((AnchorPane) sp_pane.getItems().get(0)).getChildren().get(0);
        list_view.getItems().clear();
        sFilmLinkedList.forEach(list_view.getItems()::add);
        list_view.getSelectionModel().selectFirst();
        list_view.setOnMousePressed(event -> {
            Film f = list_view.getSelectionModel().getSelectedItem();
            Main.getmListMenuController().showAllInfo(f);
        });

        list_view.setOnKeyPressed(event -> {
            if (event.getCode().isWhitespaceKey()) {
                Film f = list_view.getSelectionModel().getSelectedItem();
                Main.getmListMenuController().showAllInfo(f);
            }
        });
        Main.getPrimaryStage().show();

    }

    public void goBack() {
        Main.getPrimaryStage().setScene(Main.getmStartMenuController().getScene());
    }

    public void setUpButtons() {
        List<Button> btn_list = FilmInfoParser.getButtonList();
        GridPane this_pane = (GridPane) Main.getmTypeMenuController().getTypeMenuPane();
        for (Button btn : btn_list) {
            ObservableList chrd = this_pane.getChildren();
            FilteredList<Button> chrd_btn_list = chrd.filtered(e -> e instanceof Button);
            int row, col;
            if (chrd_btn_list.size() > 1) {
                Button last = chrd_btn_list.get(btn_list.size() - 1);
                row = GridPane.getRowIndex(last);
                col = GridPane.getColumnIndex(last);
            } else {
                row = 1;
                col = -1;
            }
            if (row == 2 && col == 3) {
                System.err.println("Eight buttons are the most, cannot add types any more, " +
                        "Please concentrate your folders e.g create an \"Other\" type");
            } else {
                if (row == 1 && col == 3) {
                    GridPane.setRowIndex(btn, 2);
                    GridPane.setColumnIndex(btn, 0);
                } else {
                    GridPane.setRowIndex(btn, row);
                    GridPane.setColumnIndex(btn, col + 1);
                }
                GridPane.setHalignment(btn, HPos.CENTER);
                btn.setOnAction(e -> getFilmByType(btn.getText()));
                this_pane.getChildren().add(btn);
                }
            }
        }
    }
