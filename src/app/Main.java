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
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.*;
import javafx.util.Duration;
import utils.FilmInfoParser;
import utils.PropertiesInfoParser;


import java.io.*;
import java.util.*;


public class Main extends Application {


    private static Stage sPrimaryStage;
    private static TypeMenuController mTypeMenuController;
    private static ListMenuController mListMenuController;
    private static StartMenuController mStartMenuController;
    private static LanguageMenuController mLanguageMenuController;




    private Pane sTypeMenuPane;
    private Pane sLanguageMenuPane;
    private SplitPane sFilmListAndInfoPane;

    private Scene sFilmListScene;
    private Scene sTypeMenuScene;
    private Scene sStartMenuScene;
    private Scene sLanguageMenuScene;



    private Pane sStartMenuPane;
    private List<Film> sFilmLinkedList;
    private List<Language> sLanguageLinkedList;
    private FilteredList<Node> sAnchorPaneList;
    private FilteredList<ImageView> sImgViewList;
    private FilteredList<Label> sLabelList;
    private FilteredList<MediaView> sMediaViewList;
    private FilteredList<Button> sOpBtnList;
    private ListView<Film> sFilmListView;
    private ListView<Language> sLanguageListView;
    private static String sFilmPath = "";
    private static String sPropPath = "";

    public static void setFilmPath(String sFilmPath) { Main.sFilmPath = sFilmPath; }
    public static void setPropertiesPath(String sPropPath) { Main.sPropPath = sPropPath; }

    public void start(Stage primaryStage) throws IOException {
        runCustomSettings();

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

        sStartMenuPane = start_menu_loader.load();
//        sStartMenuPane = FXMLLoader.load(getClass().getResource("startMenu.fxml"));
        mStartMenuController = start_menu_loader.getController();

        sStartMenuScene = mStartMenuController.getScene();
        sPrimaryStage.setScene(sStartMenuScene);
        sPrimaryStage.show();

//        sLanguageMenuPane = FXMLLoader.load(getClass().getResource("languageMenu.fxml"));
        sLanguageMenuPane = lang_menu_loader.load();
        mLanguageMenuController = lang_menu_loader.getController();
        sLanguageMenuScene = mLanguageMenuController.getScene();
        sLanguageListView = (ListView) sLanguageMenuPane.getChildren().get(0);
        sLanguageLinkedList.forEach(sLanguageListView.getItems()::add);


        sLanguageListView.setOnMousePressed(e -> {
            Language lan = sLanguageListView.getSelectionModel().getSelectedItem();
            setLanguage(lan);
            primaryStage.setScene(mStartMenuController.getScene());
        });

        sLanguageListView.setOnKeyPressed(e -> {
            if (e.getCode().isWhitespaceKey()) {
                Language lan = sLanguageListView.getSelectionModel().getSelectedItem();
                setLanguage(lan);
                primaryStage.setScene(mStartMenuController.getScene());
            }
        });

//        sTypeMenuPane = FXMLLoader.load(getClass().getResource("typeMenu.fxml"));
        sTypeMenuPane = type_menu_loader.load();
        mTypeMenuController = type_menu_loader.getController();
        sTypeMenuScene = mTypeMenuController.getScene();

//        sFilmListAndInfoPane = FXMLLoader.load(getClass().getResource("listMenu.fxml"));
        sFilmListAndInfoPane = list_menu_loader.load();
        mListMenuController = list_menu_loader.getController();
        sFilmListScene = mListMenuController.getScene();

        sAnchorPaneList = sFilmListAndInfoPane.getItems().filtered(e -> e instanceof AnchorPane);
        AnchorPane left = (AnchorPane) sAnchorPaneList.get(0);
        sFilmListView = (ListView) left.getChildren().get(0);
        AnchorPane right = (AnchorPane) sAnchorPaneList.get(1);

        ObservableList chrn = right.getChildren();
        sImgViewList = chrn.filtered(e -> e instanceof ImageView);
        sLabelList = chrn.filtered(e -> e instanceof Label);
        sMediaViewList = chrn.filtered(e -> e instanceof MediaView);
        sOpBtnList = chrn.filtered(e -> e instanceof Button);

        mTypeMenuController.setUpButtons();
        for (Button btn : sOpBtnList) {
            if (btn.getId().equals("play")) btn.setOnAction(
                    e -> goToMediaPlayer(sFilmListView
                                    .getSelectionModel()
                                    .getSelectedItem()));
        }



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

//    public void goToListMenuAndShow(String type) {
//
//        sPrimaryStage.setScene(sFilmListScene);
//        sFilmLinkedList = mListMenuController.setUpFilmByType(type);
//        sFilmListView.getItems().clear();
//        sFilmLinkedList.forEach(sFilmListView.getItems()::add);
//        sFilmListView.getSelectionModel().selectFirst();
//        sFilmListView.setOnMousePressed(event -> {
//            Film f = sFilmListView.getSelectionModel().getSelectedItem();
//            showAllInfo(f);
//        });
//
//        sFilmListView.setOnKeyPressed(event -> {
//            if (event.getCode().isWhitespaceKey()) {
//                Film f = sFilmListView.getSelectionModel().getSelectedItem();
//                showAllInfo(f);
//            }
//        });
//        sPrimaryStage.show();
//
//    }


    /*
    not implemented
     */

    public void goToMediaPlayer(Film f) {
        Media media = new Media(f.getMedia_url());
//        int width = media.widthProperty().intValue();
//        int height = media.heightProperty().intValue();

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
            mTypeMenuController.goToListMenuAndShow(f.getType());
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

    public static ListMenuController getmListMenuController() {
        return mListMenuController;
    }

    public static StartMenuController getmStartMenuController() {
        return mStartMenuController;
    }



    public static TypeMenuController getmTypeMenuController() {
        return mTypeMenuController;
    }



    public static LanguageMenuController getmLanguageMenuController() {
        return mLanguageMenuController;
    }
}
