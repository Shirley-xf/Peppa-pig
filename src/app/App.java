package app;

import app.controllers.LanguageMenuController;
import app.controllers.ListMenuController;
import app.controllers.StartMenuController;
import app.controllers.TypeMenuController;
import app.datatype.Film;
import app.datatype.Language;
import custom.*;
import dao.DbConnection;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.util.Duration;
import utils.PropertiesInfoParser;


import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.*;


/**
 * App class is the main class that runs the application.
 * Normally it names Main and has a static main method.
 * However, we do not what it to run directly. So we use boot to replace main,
 * and the main method is in a client class.
 */
public class App extends Application {


    private static Stage sPrimaryStage;
    private static TypeMenuController sTypeMenuController = null;
    private static ListMenuController sListMenuController = null;
    private static StartMenuController sStartMenuController = null;
    private static LanguageMenuController sLanguageMenuController = null;

    private List<Language> mLanguageLinkedList;


    public static Label subtitleLabel;
    private static String sFilmPath = "";
    private static String prefix = "default_";
    public static HashMap<Integer, String> subTitleNow;


    private static String sPropPath = "";


    private static String sIntroPath = "";
    private String mSubtitle;
    /**
     * The constant currentLanguage refers to the current language chosen.
     */
    public static String currentLanguage;
    /**
     * The constant BASE_PATH is the base path of the project.
     */
    public static final String BASE_PATH = Paths.get(".").toAbsolutePath().normalize().toString();
    /**
     * The constant FS is the file system of the machine that is using.
     */
    public static final FileSystem FS = FileSystems.getDefault();
    /**
     * The constant DEFAULT_INTRO_PATH is the default path of the introduction.
     */
    private static final String DEFAULT_INTRO_PATH = "data" + FS.getSeparator() + "introductions";
    /**
     * The constant property refers to the property resource bundle of the current language that chosen.
     */
    public static PropertyResourceBundle property = null;

    /**
     * hashmap fxmlloaders, string -> fxmlloader, and the key the name of fxml.
     */
    private static Map<String, FXMLLoader> FXMLLoaders;

    /**
     * Default fxmls.
     */
    public static String[] FXMLs = {"startMenu.fxml", "languageMenu.fxml",
            "typeMenu.fxml", "listMenu.fxml"};


    private static boolean runned_custom_init = false;
    private static String sSubTitlePath = BASE_PATH + FS.getSeparator() + "data" + FS.getSeparator() + "subtitles";

    /**
     * Setter of the path of the film.
     *
     * @param sFilmPath the s film path
     */
    public static void setFilmPath(String sFilmPath) {
        App.sFilmPath = sFilmPath;
    }

    /**
     * Setter of the path to the resource bundles.
     *
     * @param prop_path the path to resource bundles
     */
    public static void setPropertiesPath(String prop_path) {
        App.sPropPath = prop_path;
    }

    public static void setIconPath(String icon_path) {
        App.sIconPath = sIconPath;
    }

    public static String getIconPath() {
        return sIconPath;
    }

    private static String sIconPath = BASE_PATH + FS.getSeparator() + "data" + FS.getSeparator() + "icons";
    /**
     * this is the method that overrides the start in Class Application, mainly start a stage
     *
     * @param primaryStage
     * @throws IOException
     */
    public void start(Stage primaryStage) throws IOException {
        getPropertiesInfo();
        FXMLLoaders = new HashMap<>(4);
        if (!runned_custom_init) {
            runCustomSettingsToInit();
            runned_custom_init = true;
        }
        for (int i = 0; i < FXMLs.length; i++) {
            FXMLLoaders.put(FXMLs[i], new FXMLLoader(App.class.getResource(FXMLs[i])));
        }

        mLanguageLinkedList = PropertiesInfoParser.getLanguagesList(sPropPath);
        sPrimaryStage = primaryStage;


        property = PropertiesInfoParser.getProperty(prefix + currentLanguage, sPropPath);


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

    /**
     * Boot. Similar to the Main method in normal App. We name it boot because we don't what it to run directly
     *
     * @param args the args
     */
    public static void boot(String[] args) {
        currentLanguage = "en";
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
        Customizable prop_custom = CustomUtils.getCustomAtPrev();
        if (prop_custom != null) {
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
    }

    /**
     * Thanks Stephane for giving us a model such method :)
     *
     * @param film
     */
    private void goToMediaPlayer(Film film) {
        Media media = new Media(film.getMedia_url());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(550);
        mediaView.setFitHeight(340);
        Runnable rnb = null;
        Button playButton = new Button(">");
//        playButton.setStyle("-fx-text-fill: #2280e8; -fx-background-color: #eeffcc");
        Button backButton = new Button(property.getString("Back"));
//        backButton.setStyle("-fx-text-fill: #2280e8; -fx-background-color: #eeffcc");
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
            sTypeMenuController.goToListMenuAndShow(film.getType());
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


        subtitleLabel = new Label();
        String path = (currentLanguage.equals("en")) ? sSubTitlePath : sSubTitlePath + FS.getSeparator() + currentLanguage;
        File[] fl = new File(path).listFiles();
        File file_to_read = null;
        for (File lfile : fl) {
            if (lfile.getName().contains(".txt") && lfile.getName().contains(film.getName())) {
                file_to_read = lfile;
            }
        }

        subTitleNow = null;
        try (FileInputStream fi = new FileInputStream(file_to_read)) {
            mSubtitle = "";
            StringBuilder sb = new StringBuilder();
            BufferedInputStream in = new BufferedInputStream(fi);
            byte[] buffer = new byte[200];
            int r;
            while ((r = in.read(buffer)) != -1) {
                sb.append(new String((Arrays.copyOfRange(buffer, 0, r))));
            }
            mSubtitle = sb.toString();
            subTitleNow = new HashMap<>(mSubtitle.length() / 20);
            for (String line : mSubtitle.split("\n")) {
                int sep_index = line.indexOf(" ");
                double this_time = Double.parseDouble(line.substring(0, sep_index));
                subTitleNow.put((int) this_time, line.substring(sep_index + 1, line.length()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        subtitleLabel.setFont(new Font("Times", 25));
        subtitleLabel.setId("subtitle");
        subtitleLabel.setTextFill(Color.DARKBLUE);

        mediaPlayer.volumeProperty().bind(slVolume.valueProperty().divide(100));
        HBox volume_box = new HBox();
        HBox subtitle_box = new HBox(20);
        subtitle_box.getChildren().add(subtitleLabel);
        VBox button_box = new VBox(10);
        button_box.setAlignment(Pos.CENTER);
        button_box.getChildren().addAll(playButton, rewindButton, forwardButton, backButton);
        volume_box.getChildren().addAll(new Label(property.getString("Volume")), slVolume);
        BorderPane pane = new BorderPane();
        pane.setCenter(mediaView);
        pane.setBottom(subtitle_box);
        pane.setTop(volume_box);
        pane.setRight(button_box);

        pane.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.PLUS) || e.getCode().equals(KeyCode.EQUALS)) {
                slVolume.setValue((slVolume.getValue() + 10) < 100 ? slVolume.getValue() + 10 : 100);
            } else if (e.getCode().equals(KeyCode.MINUS)) {
                slVolume.setValue((slVolume.getValue() - 10) > 0 ? slVolume.getValue() - 10 : 0);
            }
        });

        Scene scene = new Scene(pane, 600, 400);
        scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        sPrimaryStage.setTitle(film.getMedia_url().substring(film.getMedia_url().lastIndexOf("/") + 1
                , film.getMedia_url().length()).replace("%20", " "));
        sPrimaryStage.setScene(scene);
        sPrimaryStage.show();

        if (subTitleNow != null) {
            Timeline subtitle_change = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    Integer sec = (int) mediaPlayer.getCurrentTime().toSeconds();
                    if (subTitleNow.containsKey(sec)) {
                        subtitleLabel.setText(subTitleNow.get(sec));
                    }
                }
            }));
            subtitle_change.setCycleCount(Timeline.INDEFINITE);
            subtitle_change.play();
        }

//
//
//        if (rnb != null) rnb.run();
    }

    /**
     * Gets primary stage.
     *
     * @return the primary stage
     */
    public static Stage getPrimaryStage() {
        return sPrimaryStage;
    }

    /**
     * Gets list menu controller.
     *
     * @return the list menu controller
     */
    public static ListMenuController getsListMenuController() {
        return sListMenuController;
    }

    /**
     * Gets start menu controller.
     *
     * @return the start menu controller
     */
    public static StartMenuController getsStartMenuController() {
        return sStartMenuController;
    }


    /**
     * Gets type menu controller.
     *
     * @return the type menu controller
     */
    public static TypeMenuController getsTypeMenuController() {
        return sTypeMenuController;
    }


    /**
     * Gets language menu controller.
     *
     * @return the language menu controller
     */
    public static LanguageMenuController getsLanguageMenuController() {
        return sLanguageMenuController;
    }


    /**
     * Getter of the path to the introduction.
     *
     * @return the intro path
     */
    public static String getIntroPath() {
        return sIntroPath;
    }

    /**
     * Setter of the path to the introduction.
     *
     * @param intro_path
     */
    public static void setIntroPath(String intro_path) {
        App.sIntroPath = intro_path;
    }


    /**
     * Getter of the current path to the bundles.
     */
    public static String getPropPath() {
        return sPropPath;
    }

    /**
     * Setter of subtitle path
     *
     * @param subtitle_path
     */
    public static void setsSubTitlePath(String subtitle_path) {
        App.sSubTitlePath = subtitle_path;
    }

}
