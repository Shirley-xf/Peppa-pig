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
}
