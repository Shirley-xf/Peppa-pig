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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import utils.FilmInfoParser;

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
        SplitPane sp_pane = Main.getsListMenuController().getFilmListSplitPane();
        ps.setScene(Main.getsListMenuController().getScene());
        List<Film> filmLinkedList = Main.getsListMenuController().setUpFilmByType(type);
        ListView<Film> list_view = (ListView<Film>) ((AnchorPane) sp_pane.getItems().get(0)).getChildren().get(0);
        list_view.getItems().clear();
        filmLinkedList.forEach(list_view.getItems()::add);
        list_view.getSelectionModel().selectFirst();
        list_view.setOnMousePressed(event -> {
            Film f = list_view.getSelectionModel().getSelectedItem();
            Main.getsListMenuController().showAllInfo(f);
        });

        list_view.setOnKeyPressed(event -> {
            if (event.getCode().isWhitespaceKey()) {
                Film f = list_view.getSelectionModel().getSelectedItem();
                Main.getsListMenuController().showAllInfo(f);
            }
        });
        Main.getPrimaryStage().show();

    }

    public void goBack() {
        Main.getPrimaryStage().setScene(Main.getsStartMenuController().getScene());
    }

    public void setUpButtons() {
        List<Button> btn_list = FilmInfoParser.getButtonList();
        GridPane this_pane = Main.getsTypeMenuController().getTypeMenuPane();
        ObservableList chrd = this_pane.getChildren();
        FilteredList<Button> chrd_btn_list = chrd.filtered(e -> (e instanceof Button) && !((Button) e).getId().equals("go_back_btn"));
        chrd.removeAll(chrd_btn_list);
        for (Button btn : btn_list) {
            chrd = this_pane.getChildren();
            chrd_btn_list = chrd.filtered(e -> e instanceof Button);
            int row, col;
            if (chrd_btn_list.size() > 1) {
                Button last = chrd_btn_list.get(chrd_btn_list.size() - 1);
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
