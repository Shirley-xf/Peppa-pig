package app;

import app.controllers.LanguageMenuController;
import app.controllers.ListMenuController;
import app.controllers.StartMenuController;
import app.controllers.TypeMenuController;
import app.datatype.Film;
import app.datatype.Language;
import custom.*;
import dao.DbConnection;
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
import utils.FilmInfoParser;
import utils.PropertiesInfoParser;


import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.*;


public class Main extends Application {


    private static Stage sPrimaryStage;
    private static TypeMenuController sTypeMenuController = null;
    private static ListMenuController sListMenuController = null;
    private static StartMenuController sStartMenuController = null;
    private static LanguageMenuController sLanguageMenuController = null;


    private List<Language> mLanguageLinkedList;

    private static String sFilmPath = "";
    private static String prefix = "default_";


    private static String sPropPath = "";
    private static String sIntroPath = "";

    public static String cur_language;
    public static final String BASE_PATH = Paths.get(".").toAbsolutePath().normalize().toString();
    public static final FileSystem FS = FileSystems.getDefault();
    public static final String DEFAULT_INTRO_PATH = "data" + FS.getSeparator() + "introductions";
    public static PropertyResourceBundle property = null;
    public static Map<String, FXMLLoader> FXMLLoaders;
    public static String[] FXMLs = {"startMenu.fxml", "languageMenu.fxml",
            "typeMenu.fxml", "listMenu.fxml"};


    private static boolean runned_custom_init = false;

    public static void setFilmPath(String sFilmPath) {
        Main.sFilmPath = sFilmPath;
    }

    public static void setPropertiesPath(String sPropPath) {
        Main.sPropPath = sPropPath;
    }

    public void start(Stage primaryStage) throws IOException {
        getPropertiesInfo();
        FXMLLoaders = new HashMap<>(4);
        if (!runned_custom_init) {
            runCustomSettingsToInit();
            runned_custom_init = true;
        }
        for (int i = 0; i < FXMLs.length; i++) {
            FXMLLoaders.put(FXMLs[i], new FXMLLoader(Main.class.getResource(FXMLs[i])));
        }

        try {
            mLanguageLinkedList = PropertiesInfoParser.getPropertiesList(sPropPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sPrimaryStage = primaryStage;


        property = PropertiesInfoParser.getProperty(prefix + cur_language, sPropPath);


        for (Map.Entry<String, FXMLLoader> et : FXMLLoaders.entrySet()) {
            et.getValue().setResources(property);
            et.getValue().load();
        }

        sStartMenuController = FXMLLoaders.get("startMenu.fxml").getController();
        sPrimaryStage.setScene(sStartMenuController.getScene());
        sPrimaryStage.show();

        sLanguageMenuController = FXMLLoaders.get("languageMenu.fxml").getController();
        ListView<Language> lan_lst_view = (ListView) sLanguageMenuController.getLanguagePane().getChildren().get(0);
        mLanguageLinkedList.forEach(lan_lst_view.getItems()::add);

        sTypeMenuController = FXMLLoaders.get("typeMenu.fxml").getController();

        sListMenuController = FXMLLoaders.get("listMenu.fxml").getController();

        FilteredList list_menu_anc_panes = sListMenuController.getFilmListSplitPane()
                .getItems()
                .filtered(e -> e instanceof AnchorPane);


        AnchorPane left = (AnchorPane) list_menu_anc_panes.get(0);
        ListView<Film> filmListView = (ListView) left.getChildren().get(0);
        AnchorPane right = (AnchorPane) list_menu_anc_panes.get(1);

        ObservableList chrd = right.getChildren();
        FilteredList<Button> op_btns = chrd.filtered(e -> e instanceof Button);

        sTypeMenuController.setUpButtons();
        for (Button btn : op_btns) {
            if (btn.getId().equals("play")) btn.setOnAction(
                    e -> goToMediaPlayer(filmListView
                            .getSelectionModel()
                            .getSelectedItem()));
        }


        runCustomSettings();
    }

    public static void main(String[] args) {
        cur_language = "en";
        launch(args);
    }

    private void runCustomSettingsToInit() {
        List<Customizable> custom_list = CustomUtils.getInitCustomList();
        Iterator<Customizable> iter = custom_list.iterator();
        while (iter.hasNext()) {
            iter.next().customSetup();
        }
    }

    private void runCustomSettings() {
        List<Customizable> custom_list = CustomUtils.getDurableCustomList();
        Iterator<Customizable> iter = custom_list.iterator();
        while (iter.hasNext()) {
            iter.next().customSetup();
        }
    }

    private void getPropertiesInfo() {
        Customizable prop_custom = CustomUtils.getPropertiesCustom();
        prop_custom.customSetup();
        String intro_path = (sIntroPath.length() < 1) ? DEFAULT_INTRO_PATH : sIntroPath;
        if (runned_custom_init) {
            try {
                for (File f : new File(intro_path).listFiles(f -> f.getName().contains(".txt"))) {
                    String sql = "update film set intro_url = \"" + intro_path + FS.getSeparator() + f.getName()
                            + "\" where name = \"" + f.getName().replace(".txt", "") + "\";";
                            DbConnection.exeUpdate(sql);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void goToMediaPlayer(Film f) {
        Media media = new Media(f.getMedia_url());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        Button playButton = new Button(">");
        playButton.setStyle("-fx-text-fill: #2280e8; -fx-background-color: #eeffcc");
        Button backButton = new Button(property.getString("Back"));
        backButton.setStyle("-fx-text-fill: #2280e8; -fx-background-color: #eeffcc");
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

        rewindButton.setStyle("-fx-text-fill: #2280e8; -fx-background-color: #eeffcc");
        forwardButton.setStyle("-fx-text-fill: #2280e8; -fx-background-color: #eeffcc");
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
        slVolume.setStyle("-fx-background-color: #bbccee; -fx-text-fill: #002255");
        mediaPlayer.volumeProperty().bind(slVolume.valueProperty().divide(100));
        HBox volume_box = new HBox();
        HBox button_box = new HBox(10);
        button_box.setAlignment(Pos.CENTER);
        button_box.getChildren().addAll(playButton, rewindButton, forwardButton, backButton);
        volume_box.getChildren().addAll(new Label(property.getString("Volume")), slVolume);
        BorderPane pane = new BorderPane();
        pane.setCenter(mediaView);
        pane.setBottom(button_box);
        pane.setTop(volume_box);
        pane.setStyle("-fx-background-color: #ccffee");

        Scene scene = new Scene(pane, 600, 400);
        sPrimaryStage.setTitle(f.getMedia_url().substring(f.getMedia_url().lastIndexOf("/") + 1
                , f.getMedia_url().length()).replace("%20", " "));
        sPrimaryStage.setScene(scene);
        sPrimaryStage.show();
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


    public static String getIntroPath() {
        return sIntroPath;
    }

    public static void setIntroPath(String sIntroPath) {
        Main.sIntroPath = sIntroPath;
    }


    public static String getPropPath() {
        return sPropPath;
    }
}
