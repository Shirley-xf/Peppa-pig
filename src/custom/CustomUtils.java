package custom;

import app.Main;
import dao.DbConnection;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import app.controllers.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public class CustomUtils {
    private CustomUtils() {}
    private static List<Customizable> sCustomList = new LinkedList<>();
    public static void addCustom(Customizable c) {
        sCustomList.add(c);
    }

    public static void delCustom(Customizable c) {
        sCustomList.remove(c);
    }

    public static List getCustomList() {
        return sCustomList;
    }

//    public static void addMovieType(File dir, String... all_texts) {
//
//        GridPane p = (GridPane) Main.getMenuPane();
//        for (String text : all_texts) {
//            Button new_btn = new Button(text);
//            new_btn.setId(text.toLowerCase());
//            ObservableList children = p.getChildren();
//            FilteredList<Button> btn_list = children.filtered(b -> b instanceof Button);
//            Button last = btn_list.get(btn_list.size() - 1);
//            int row = GridPane.getRowIndex(last);
//            int col = GridPane.getColumnIndex(last);
//            if (row == 2 && col == 3) {
//                System.out.println("Full, cannot add types any more");
//            } else if (row == 1 && col == 3) {
//                GridPane.setRowIndex(new_btn,2);
//                GridPane.setColumnIndex(new_btn,0);
//            } else {
//                GridPane.setRowIndex(new_btn, row);
//                GridPane.setColumnIndex(new_btn, col + 1);
//            }
//            new_btn.setOnAction(e -> TypeMenuController.getFilmByType(text));
//            p.getChildren().add(new_btn);
//        }
//    }
//
//    public static boolean changeType(String old_text, String text) {
//        GridPane p = (GridPane) Main.getMenuPane();
//        ObservableList children = p.getChildren();
//        FilteredList<Button> btn_list = children.filtered(b -> b instanceof Button);
//
//        for (Button b : btn_list) {
//            if (b.getText().equals(old_text)) {
//                try {
//                    String cmd = "update `film` set `type` = \"" + text + "\" where `type` = " + old_text + ";";
//                    b.setText(text);
//                    DbConnection.exeUpdate(cmd);
//
//                    return true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return false;
//                }
//
//            }
//        }
//        return false;
//    }
}
