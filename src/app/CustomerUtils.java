package app;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;


public class CustomerUtils {

//    private static List<InvalidationListener> mListener = new LinkedList();
    private static CustomerUtils cu;
    private CustomerUtils() {}
    public static synchronized CustomerUtils getUtils() {
        if (cu == null) cu = new CustomerUtils();
        return cu;
    }
    public static void addMovieType(String... type) {
        GridPane p = (GridPane) TypeMenu.getPane();
        for (String t : type) {
            Button new_btn = new Button(t);
            new_btn.setId(t.toLowerCase());
            ObservableList children = p.getChildren();
            FilteredList<Button> btn_list = new FilteredList(children, Button -> true);
            Button last = btn_list.get(children.size() - 1);
            int row = GridPane.getRowIndex(last);
            int col = GridPane.getColumnIndex(last);
            if (row == 2 && col == 3) {
                System.out.println("Full, cannot add types any more");
            } else if (row == 1 && col == 3) {
                GridPane.setRowIndex(new_btn,2);
                GridPane.setColumnIndex(new_btn,0);
            } else {
                GridPane.setRowIndex(new_btn, row);
                GridPane.setColumnIndex(new_btn, col + 1);
            }
            new_btn.setOnAction(last.getOnAction());
            p.getChildren().add(new_btn);
        }
    }

    public static void changeTypeNameById(String id, String text) {
        GridPane p = (GridPane) TypeMenu.getPane();
        ObservableList children = p.getChildren();
        FilteredList<Button> btn_list = new FilteredList(children, Button -> true);
        for (Button b : btn_list) {
            if (b.getId().equals(id)) {
                b.setText(text);
                return;
            }
        }
    }
}
