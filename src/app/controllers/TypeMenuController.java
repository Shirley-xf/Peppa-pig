package app.controllers;


import app.Main;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import utils.FilmInfoParser;
import java.util.List;


public class TypeMenuController {

    private static FilmInfoParser filmInfoParser;

    public static void initFilmParse() {
        filmInfoParser = new FilmInfoParser();
    }

    public static void getFilmByType(String text) {
        System.out.println("getFilmByType" + " " + text);
        Main.goToListMenuAndShow(text);
    }

    public void goBack() {
        System.out.println("goBack");
    }

    public void goToMainMenu() {
        System.out.println("goToMainMenu");
    }

    public static void setUpButtons() {
        List<Button> btn_list = filmInfoParser.getButtonList();
        GridPane this_pane = (GridPane) Main.getMenuPane();
        for (Button btn : btn_list) {
            ObservableList chrd = this_pane.getChildren();
            FilteredList<Button> chrd_btn_list = chrd.filtered(e -> e instanceof Button);
            int row, col;
            if (chrd_btn_list.size() > 2) {
                Button last = chrd_btn_list.get(btn_list.size() - 1);
                row = GridPane.getRowIndex(last);
                col = GridPane.getColumnIndex(last);
            } else {
                row = 1;
                col = -1;
            }
            if (row == 2 && col == 3) {
                System.out.println("Eight buttons are the most, cannot add types any more" +
                        "Please concentrate your folders e.g create an other type");
            } else if (row == 1 && col == 3) {
                GridPane.setRowIndex(btn,2);
                GridPane.setColumnIndex(btn,0);
            } else {
                GridPane.setRowIndex(btn, row);
                GridPane.setColumnIndex(btn, col + 1);
            }
            btn.setOnAction(e -> TypeMenuController.getFilmByType(btn.getText()));
            this_pane.getChildren().add(btn);
        }
    }

}
