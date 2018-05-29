package app;

import app.controllers.TypeMenuController;
import custom.CustomUtils;
import custom.Customizable;
import dao.DbConnection;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;


public class Main extends Application {

    private static Pane sMenuPane;
    private static Parent sListMenuPar;
    private static Stage sPrimaryStage;
    public static Connection conn;

    @Override
    public void start(Stage primaryStage) throws IOException {
        sPrimaryStage = primaryStage;
        sMenuPane = FXMLLoader.load(getClass().getResource("typeMenu.fxml"));
        ObservableList children = sMenuPane.getChildren();
        initDefaultBtn(children);
        sPrimaryStage.setScene(new Scene(sMenuPane, 600, 400));
        sPrimaryStage.show();
        sListMenuPar = FXMLLoader.load(getClass().getResource("listMenu.fxml"));
        runCustomSettings();
    }

    public static Pane getMenuPane() {
        return sMenuPane;
    }

    public static void main(String[] args) {
        conn = DbConnection.getConnection();
        launch(args);
    }

    public void runCustomSettings() {
        List<Customizable> custom_list = CustomUtils.getCustomList();
        Iterator<Customizable> iter = custom_list.iterator();
        while (iter.hasNext()) {
            iter.next().customSetup();
        }
    }

    public static void switchToListMenu() {

        Scene scene = new Scene(sListMenuPar);
        sPrimaryStage.setScene(scene);
        sPrimaryStage.show();
    }

    private void initDefaultBtn(ObservableList oblst) {
        FilteredList<Button> flst = oblst.filtered(Button -> true);
        for (Button btn : flst) {
            switch (btn.getId()) {
                case "type1":
                    btn.setOnAction(e -> TypeMenuController.getFilmByType(btn.getText()));
                    break;
                default:
                    break;
            }
        }
    }
}
