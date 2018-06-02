package app;

import app.controllers.ListMenuController;
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
import utils.PropertiesInfoParser;


import java.io.*;
import java.util.*;


public class Main extends Application {
    private static Pane sTypeMenuPane;
    private static Pane sLanguageMenuPane;
    private static SplitPane sFilmListAndInfoPane;
    private static Stage sPrimaryStage;
    private static Scene sFilmListScene;
    private static Scene sTypeMenuScene;
    private static Scene sStartMenuScene;
    private static Scene sLanguageMenuScene;

    private static Pane sStartMenuPane;
    private static List<Film> sFilmLinkedList;
    private static List<Language> sLanguageLinkedList;
    private static FilteredList<Node> sAnchorPaneList;
    private static FilteredList<ImageView> sImgViewList;
    private static FilteredList<Label> sLabelList;
    private static FilteredList<MediaView> sMediaViewList;
    private static FilteredList<Button> sOpBtnList;
    private static ListView<Film> sFilmListView;
    private static ListView<Language> sLanguageListView;
    private static String sFilmPath = "";
    private static String sPropPath = "";

    public static void setFilmPath(String sFilmPath) { Main.sFilmPath = sFilmPath; }
    public static void setPropertiesPath(String sPropPath) { Main.sPropPath = sPropPath; }

    public void start(Stage primaryStage) throws IOException {
        runCustomSettings();
        TypeMenuController.initFilmParse(sFilmPath);
        try {
            sLanguageLinkedList = PropertiesInfoParser.getPropertiesList(sPropPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sPrimaryStage = primaryStage;
        sStartMenuPane = FXMLLoader.load(getClass().getResource("startMenu.fxml"));
        sStartMenuScene = new Scene(sStartMenuPane);
        sPrimaryStage.setScene(sStartMenuScene);
        sPrimaryStage.show();

        sLanguageMenuPane = FXMLLoader.load(getClass().getResource("languageMenu.fxml"));

        sLanguageMenuScene = new Scene(sLanguageMenuPane);
        sLanguageListView = (ListView) sLanguageMenuPane.getChildren().get(0);
        sLanguageLinkedList.forEach(sLanguageListView.getItems()::add);


        sLanguageListView.setOnMousePressed(e -> {
            Language lan = (Language) sLanguageListView.getSelectionModel().getSelectedItem();
            setLanguage(lan);
            goToStartMenu();
        });

        sLanguageListView.setOnKeyPressed(e -> {
            if (e.getCode().isWhitespaceKey()) {
                Language lan = (Language) sLanguageListView.getSelectionModel().getSelectedItem();
                setLanguage(lan);
                goToStartMenu();
            }
        });

        sTypeMenuPane = FXMLLoader.load(getClass().getResource("typeMenu.fxml"));
        sTypeMenuScene = new Scene(sTypeMenuPane);

        sFilmListAndInfoPane = FXMLLoader.load(getClass().getResource("listMenu.fxml"));
        sFilmListScene = new Scene(sFilmListAndInfoPane);

        sAnchorPaneList = sFilmListAndInfoPane.getItems().filtered(e -> e instanceof AnchorPane);
        AnchorPane left = (AnchorPane) sAnchorPaneList.get(0);
        sFilmListView = (ListView) left.getChildren().get(0);
        AnchorPane right = (AnchorPane) sAnchorPaneList.get(1);

        ObservableList chrn = right.getChildren();
        sImgViewList = chrn.filtered(e -> e instanceof ImageView);
        sLabelList = chrn.filtered(e -> e instanceof Label);
        sMediaViewList = chrn.filtered(e -> e instanceof MediaView);
        sOpBtnList = chrn.filtered(e -> e instanceof Button);

        TypeMenuController.setUpButtons();
        for (Button btn : sOpBtnList) {
            if (btn.getId().equals("play")) btn.setOnAction(
                    e -> goToMediaPlayer((Film) sFilmListView
                                    .getSelectionModel()
                                    .getSelectedItem()));
        }



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
        sFilmLinkedList = ListMenuController.setUpFilmByType(type);
        sFilmListView.getItems().clear();
        sFilmLinkedList.forEach(sFilmListView.getItems()::add);
        sFilmListView.getSelectionModel().selectFirst();
        sFilmListView.setOnMousePressed(event -> {
            Film f = sFilmListView.getSelectionModel().getSelectedItem();
            showAllInfo(f);
        });

        sFilmListView.setOnKeyPressed(event -> {
            if (event.getCode().isWhitespaceKey()) {
                Film f = sFilmListView.getSelectionModel().getSelectedItem();
                showAllInfo(f);
            }
        });
        sPrimaryStage.show();

    }


    /*
    not implemented
     */

    public static void goToMediaPlayer(Film f) {
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
            goToListMenuAndShow(f.getType());
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
        Scene scene = new Scene(pane, 750, 500);
        sPrimaryStage.setTitle(f.getMedia_url().substring(f.getMedia_url().lastIndexOf("/") + 1
                , f.getMedia_url().length()).replace("%20", " "));
        sPrimaryStage.setScene(scene);
        sPrimaryStage.show();
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
                if (last_idx != -1) {
                    sb.append(tmp.substring(0, last_idx) + "\n"
                            + tmp.substring(last_idx + 1, tmp.length()));
                } else {
                    sb.append(tmp + "\n");
                }
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
        sb.append("Duration: ");
        sb.append(f.getDuration() + "\n");
        sb.append("Year: ");
        sb.append((f.getYear() == 0) ? "" : f.getYear());
        sLabelList.get(1).setText(sb.toString());
//
    }

    public static void goToTypeMenu() {
        sPrimaryStage.setScene(sTypeMenuScene);
        sPrimaryStage.show();
    }

    public static void goToStartMenu() {
        sPrimaryStage.setScene(sStartMenuScene);
        sPrimaryStage.show();
    }

    public static void goToLanguageMenu() {
        sPrimaryStage.setScene(sLanguageMenuScene);
        sPrimaryStage.show();
    }

    public static void setLanguage(Language lan) {
        System.out.println(lan.getName());
        String pro_url = lan.getProperties_url();
        // 设置properties
    }
}
