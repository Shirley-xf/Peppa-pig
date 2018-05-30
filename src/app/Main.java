package app;

import app.controllers.TypeMenuController;
import custom.CustomUtils;
import custom.Customizable;
import dao.DbConnection;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;


public class Main extends Application {

    private static Pane sTypeMenuPane;
    private static SplitPane sFilmListPane;
    private static Stage sPrimaryStage;
    private static Scene film_list_scene;
    private static Scene type_menu_scene;
    public static Connection conn = DbConnection.getConnection();

    @Override
    public void start(Stage primaryStage) throws IOException {
        sPrimaryStage = primaryStage;
        sTypeMenuPane = FXMLLoader.load(getClass().getResource("typeMenu.fxml"));
        ObservableList children = sTypeMenuPane.getChildren();
        initDefaultBtn(children);
        type_menu_scene = new Scene(sTypeMenuPane);
        sPrimaryStage.setScene(type_menu_scene);
        sPrimaryStage.show();


        sFilmListPane = FXMLLoader.load(getClass().getResource("listMenu.fxml"));
        film_list_scene = new Scene(sFilmListPane);
        runCustomSettings();
    }

    public static Pane getMenuPane() {
        return sTypeMenuPane;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void runCustomSettings() {
        List<Customizable> custom_list = CustomUtils.getCustomList();
        Iterator<Customizable> iter = custom_list.iterator();
        while (iter.hasNext()) {
            iter.next().customSetup();
        }
    }

    public static void goToListMenuAndShow(String type) {
        sPrimaryStage.setScene(film_list_scene);
        sPrimaryStage.show();
        FilteredList<Node> anchorPaneList = sFilmListPane.getItems().filtered(AnchorPane -> true);
        AnchorPane left = (AnchorPane) anchorPaneList.get(0);
        ListView film_list_view = (ListView) left.getChildren().get(0);
        ResultSet results = queryFilm(type);
        film_list_view.getItems().clear();
        try {
            while (results.next()) {
                film_list_view.getItems().add(results.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void goToTypeMenu() {
        sPrimaryStage.setScene(type_menu_scene);
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

    private static ResultSet queryFilm(String type) {
        try {
            Statement stmt = conn.createStatement();
            String cmd = "select `name` from `film` where `type` = \"" + type + "\";";
            return stmt.executeQuery(cmd);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
