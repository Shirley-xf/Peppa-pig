package app;

import app.controllers.ListMenuController;
import app.controllers.TypeMenuController;
import custom.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.MediaView;
import javafx.stage.*;


import java.io.*;
import java.util.*;


public class Main extends Application {
    private static Pane sTypeMenuPane;
    private static SplitPane sFilmListPane;
    private static Stage sPrimaryStage;
    private static Scene sFilmListScene;
    private static Scene sTypeMenuScene;
    private static List<Film> sFilmLinkedList;
    private static FilteredList<Node> sAnchorPaneList;
    private static FilteredList<ImageView> sImgViewList;
    private static FilteredList<Label> sLabelList;
    private static FilteredList<MediaView> sMediaViewList;
    @Override
    public void start(Stage primaryStage) throws IOException {
        TypeMenuController.initFilmParse();
        sPrimaryStage = primaryStage;
        sTypeMenuPane = FXMLLoader.load(getClass().getResource("typeMenu.fxml"));
        sTypeMenuScene = new Scene(sTypeMenuPane);
        sPrimaryStage.setScene(sTypeMenuScene);
        TypeMenuController.setUpButtons();
        sPrimaryStage.show();


        sFilmListPane = FXMLLoader.load(getClass().getResource("listMenu.fxml"));
        sFilmListScene = new Scene(sFilmListPane);
        sAnchorPaneList = sFilmListPane.getItems().filtered(e -> e instanceof AnchorPane);

        AnchorPane right = (AnchorPane) sAnchorPaneList.get(1);
        ObservableList chrn = right.getChildren();
        sImgViewList = chrn.filtered(e -> e instanceof ImageView);
        sLabelList = chrn.filtered(e -> e instanceof Label);
        sMediaViewList = chrn.filtered(e -> e instanceof MediaView);


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
        sPrimaryStage.setScene(sFilmListScene);
        sPrimaryStage.show();
        AnchorPane left = (AnchorPane) sAnchorPaneList.get(0);
        ListView film_list_view = (ListView) left.getChildren().get(0);
        sFilmLinkedList = ListMenuController.queryFilmByType(type);
        film_list_view.getItems().clear();
        ObservableList<Film> obl_items = film_list_view.getItems();
        for (Film f : sFilmLinkedList) {
            obl_items.add(f);
        }
        film_list_view.getSelectionModel().selectFirst();
        film_list_view.setOnMousePressed(event -> {
            Film f = (Film) film_list_view.getSelectionModel().getSelectedItem();
            showAllInfo(f);
        });

        film_list_view.setOnKeyPressed(event -> {
            if(event.getCode().isWhitespaceKey()) {
                Film f = (Film) film_list_view.getSelectionModel().getSelectedItem();
                showAllInfo(f);
            }
        });

    }

    private static void showAllInfo(Film f) {
        System.out.println("show " + f);

        StringBuilder sb = new StringBuilder("Introduction:\n");
        File intro_file = new File(f.getIntro_url()
                .replace("file:", "")
                .replace("%20", " "));
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(intro_file));
            char[] buffer = new char[25];
            int lines = 0;
            while (reader.read(buffer) != -1 && ++lines < 11) {
                String tmp = new String(buffer);
                int last_idx = tmp.lastIndexOf(" ");
                sb.append(tmp.substring(0, last_idx) + "\n"
                        + tmp.substring(last_idx + 1, tmp.length()));
                if (lines == 10) {
                    sb.delete(sb.lastIndexOf(".") + 1, sb.length());
                }
            }
            sb.append("\n...");
        } catch (Exception e) {
            System.err.println("introduction " + e);
        }

        ImageView iv = sImgViewList.get(0);

        try  {
//            File file = new File(f.getImg_url());
            Image img = new Image(f.getImg_url());
            iv.setImage(img);
        } catch (Exception e) {
            System.out.println(f.getImg_url());
            System.err.println("Image " + e);
        }

//        try {
//            sMediaViewList.set(0, new MediaView(new Med))
//        }

        sLabelList.get(0).setText(sb.toString());
        sb = new StringBuilder("Actors:\n");
        List<String> actors = f.getActors();
        List<String> directors = f.getDirectors();
        for (String s : actors) sb.append(s + "\n");
        sb.append("Directors:\n");
        for (String s : directors) sb.append(s + "\n");
        sb.append("Duration:\n");
        sb.append(f.getDuration() + "\n");
        sb.append("Year:\n");
        sb.append(f.getYear());
        sLabelList.get(1).setText(sb.toString());
//
    }

    public static void goToTypeMenu() {
        sPrimaryStage.setScene(sTypeMenuScene);
        sPrimaryStage.show();
    }

}
