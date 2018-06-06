package app;

import app.controllers.LanguageMenuController;
import app.controllers.ListMenuController;
import app.controllers.StartMenuController;
import app.controllers.TypeMenuController;
import app.datatype.Film;
import app.datatype.Language;
import custom.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.*;
import javafx.util.Duration;
import utils.PropertiesInfoParser;


import java.io.*;
import java.util.*;


public class Main extends Application {


    private static Stage sPrimaryStage;
    private static TypeMenuController sTypeMenuController;
    private static ListMenuController sListMenuController;
    private static StartMenuController sStartMenuController;
    private static LanguageMenuController sLanguageMenuController;

    private List<Film> sFilmLinkedList;
    private List<Language> sLanguageLinkedList;

    private ListView<Language> mLanguageListView;
    private static String sFilmPath = "";
    private static String sPropPath = "";

    public static void setFilmPath(String sFilmPath) { Main.sFilmPath = sFilmPath; }
    public static void setPropertiesPath(String sPropPath) { Main.sPropPath = sPropPath; }

    public void start(Stage primaryStage) throws IOException {
        runCustomSettingsAtPrev();
        try {
            sLanguageLinkedList = PropertiesInfoParser.getPropertiesList(sPropPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sPrimaryStage = primaryStage;
        FXMLLoader start_menu_loader = new FXMLLoader(getClass().getResource("startMenu.fxml"));
        FXMLLoader lang_menu_loader = new FXMLLoader(getClass().getResource("languageMenu.fxml"));
        FXMLLoader type_menu_loader = new FXMLLoader(getClass().getResource("typeMenu.fxml"));
        FXMLLoader list_menu_loader = new FXMLLoader(getClass().getResource("listMenu.fxml"));

        start_menu_loader.load();
        sStartMenuController = start_menu_loader.getController();
        sPrimaryStage.setScene(sStartMenuController.getScene());
        sPrimaryStage.show();

        lang_menu_loader.load();
        sLanguageMenuController = lang_menu_loader.getController();
        mLanguageListView = (ListView) sLanguageMenuController.getLanguagePane().getChildren().get(0);
        sLanguageLinkedList.forEach(mLanguageListView.getItems()::add);


        mLanguageListView.setOnMousePressed(e -> {
            Language lan = mLanguageListView.getSelectionModel().getSelectedItem();
            setLanguage(lan);
            primaryStage.setScene(sStartMenuController.getScene());
        });

        mLanguageListView.setOnKeyPressed(e -> {
            if (e.getCode().isWhitespaceKey()) {
                Language lan = mLanguageListView.getSelectionModel().getSelectedItem();
                setLanguage(lan);
                primaryStage.setScene(sStartMenuController.getScene());
            }
        });

        type_menu_loader.load();
        sTypeMenuController = type_menu_loader.getController();
        list_menu_loader.load();
        sListMenuController = list_menu_loader.getController();
        FilteredList anchor_panes = sListMenuController.getFilmListSplitPane().getItems().filtered(e -> e instanceof AnchorPane);
        AnchorPane left = (AnchorPane) anchor_panes.get(0);
        ListView<Film> filmListView = (ListView) left.getChildren().get(0);
        AnchorPane right = (AnchorPane) anchor_panes.get(1);

        ObservableList chrn = right.getChildren();
        FilteredList op_btns = chrn.filtered(e -> e instanceof Button);

        sTypeMenuController.setUpButtons();
        for (Button btn : (FilteredList<Button>) op_btns) {
            if (btn.getId().equals("play")) btn.setOnAction(
                    e -> goToMediaPlayer(filmListView
                                    .getSelectionModel()
                                    .getSelectedItem()));
        }

        runCustomSettingsAtPost();

    }
    public static void main(String[] args) {
        launch(args);
    }

    public void runCustomSettingsAtPrev() {
        List<Customizable> custom_list = CustomUtils.getPrevCustomList();
        Iterator<Customizable> iter = custom_list.iterator();
        while (iter.hasNext()) {
            iter.next().customSetup();
        }
    }

    public void runCustomSettingsAtPost() {
        List<Customizable> custom_list = CustomUtils.getPostCustomList();
        Iterator<Customizable> iter = custom_list.iterator();
        while (iter.hasNext()) {
            iter.next().customSetup();
        }
    }


    public void goToMediaPlayer(Film f) {
        Media media = new Media(f.getMedia_url());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        Button playButton = new Button(">");
        Button backButton = new Button("back");
        playButton.setOnAction(e -> {
            if (playButton.getText().equals(">")) {
                mediaPlayer.play();
                playButton.setText("||");
            } else {
                mediaPlayer.pause();
                playButton.setText(">");
            }
        });
        backButton.setOnAction(e -> {
            mediaPlayer.stop();
            sTypeMenuController.goToListMenuAndShow(f.getType());
        });

        Button rewindButton = new Button("<<");
        Button forwardButton = new Button(">>");
        rewindButton.setOnAction(e -> mediaPlayer.seek(mediaPlayer
                .getCurrentTime()
                .subtract(Duration.minutes(1)))
        );
        forwardButton.setOnAction(e -> mediaPlayer.seek(mediaPlayer
                .getCurrentTime()
                .add(Duration.minutes(1)))
        );
        Slider slVolume = new Slider();
        slVolume.setPrefWidth(150);
        slVolume.setMaxWidth(Region.USE_PREF_SIZE);
        slVolume.setMinWidth(30);
        slVolume.setValue(50);
        mediaPlayer.volumeProperty().bind(slVolume.valueProperty().divide(100));
        HBox volume_box = new HBox();
        HBox button_box = new HBox(10);
        button_box.setAlignment(Pos.CENTER);
        button_box.getChildren().addAll(playButton, rewindButton, forwardButton, backButton);
        volume_box.getChildren().addAll(new Label("Volume"), slVolume);
        BorderPane pane = new BorderPane();
        pane.setCenter(mediaView);
        pane.setBottom(button_box);
        pane.setTop(volume_box);
        Scene scene = new Scene(pane, 600, 400);
        sPrimaryStage.setTitle(f.getMedia_url().substring(f.getMedia_url().lastIndexOf("/") + 1
                , f.getMedia_url().length()).replace("%20", " "));
        sPrimaryStage.setScene(scene);
        sPrimaryStage.show();
    }



    public void setLanguage(Language lan) {
        System.out.println(lan.getName());
        String pro_url = lan.getProperties_url();
        // 设置properties
    }

    public static Stage getPrimaryStage() {
        return sPrimaryStage;
    }

    public static ListMenuController getsListMenuController() {
        return sListMenuController;
    }

    public static StartMenuController getsStartMenuController() {
        return sStartMenuController;
    }



    public static TypeMenuController getsTypeMenuController() {
        return sTypeMenuController;
    }



    public static LanguageMenuController getsLanguageMenuController() {
        return sLanguageMenuController;
    }
}
