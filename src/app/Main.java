package app;

import app.controllers.TypeMenuController;
import custom.*;
import dao.DbConnection;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;


public class Main extends Application {

    private static Pane sTypeMenuPane;
    private static SplitPane sFilmListPane;
    private static Stage sPrimaryStage;
    private static Scene film_list_scene;
    private static Scene type_menu_scene;
    private static List<Film> sFilmLinkedList;
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
        sFilmLinkedList = queryFilmByType(type);
        film_list_view.getItems().clear();
        for (Film f : sFilmLinkedList) {
            film_list_view.getItems().add(f.getName());
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

    private static LinkedList<Film> queryFilmByType(String type) {
        try {
            LinkedList<Film> lst = new LinkedList<>();
            Statement stmt = conn.createStatement();
            String cmd = "select `id`, `name`, `duration`, `year`, `type`, `intro_url`, `media_url`, `img_url` from `film` where `type` = \"" + type + "\"";
            ResultSet films_result = stmt.executeQuery(cmd);
            try {
                while (films_result.next()) {
                    Film f = new Film();
                    f.setId(films_result.getInt(1));
                    f.setName(films_result.getString(2));
                    f.setDuration(films_result.getString(3));
                    f.setYear(films_result.getInt(4));
                    f.setType(films_result.getString(5));
                    f.setIntro_url(films_result.getString(6));
                    f.setMedia_url(films_result.getString(7));
                    f.setImg_url(films_result.getString(8));
                    addDirectorsAndActors(f);
                    lst.add(f);
                }
                return lst;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private static void addDirectorsAndActors(Film f) {
        try {
            Statement stmt = conn.createStatement();
            String[] actors, directors;
            String actor_qry = "select `actor` from `film_actor` where `id` = \"" + f.getId() + "\";";
            String director_qry = "select `director` from `film_director` where `id` = \"" + f.getId() + "\";";
            ResultSet actor_set = stmt.executeQuery(actor_qry);
            ResultSet director_set = stmt.executeQuery(director_qry);
            List<String> tmp = new LinkedList<>();
            while (actor_set.next()) {
                tmp.add(actor_set.getString(1));
            }
            f.setActors(tmp);
            tmp = new LinkedList<>();
            while (director_set.next()) {
                tmp.add(director_set.getString(1));
            }
            f.setDirectors(tmp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
