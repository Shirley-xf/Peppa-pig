package app.controllers;

import app.App;
import app.datatype.Film;
import dao.DbConnection;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.*;
import java.sql.ResultSet;
import java.util.*;

/**
 * List menu controller controls the listMenu.fxml
 */
public class ListMenuController {
    private Scene scene;
    @FXML
    private SplitPane filmListSplitPane;
    @FXML
    private ImageView poster;
    @FXML
    private Button back;
    @FXML
    private Button play;
    @FXML
    private MediaView trailer;
    @FXML
    private Label intro;
    @FXML
    private Label basicInfo;

    /**
     * Go back to type menu
     */
    public void goBack() {
        App.getPrimaryStage().setScene(App.getsTypeMenuController().getScene());
        App.getPrimaryStage().show();
    }

    /**
     * Show all information of the film. Including trailers, posters, introductions and basic information.
     *
     * @param f the film for displaying information
     */
    public void showAllInfo(Film f) {

        PropertyResourceBundle prop = App.property;

        // Poster
        try {
            Image img = new Image(f.getImg_url());
            poster.setImage(img);
        } catch (Exception e) {
            System.out.println(f.getImg_url());
            System.err.println("Image " + e);
        }


        // Text：
        StringBuilder sb = new StringBuilder(prop.getString("Introduction") + ":\n");
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

        intro.setText(sb.toString());


        sb = new StringBuilder(prop.getString("Actors") + ":\n");
        List<String> actors = f.getActors();
        List<String> directors = f.getDirectors();
        for (String s : actors) {
            String atr = getPropString(s);
            sb.append(atr + "\n");
        }
        sb.append(prop.getString("Directors") + ":\n");
        for (String s : directors) {
            String dtr = getPropString(s);
            sb.append(dtr + "\n");
        }

        sb.append(prop.getString("Duration") + ": ");
        sb.append(f.getDuration() + "\n");
        if (f.getYear() != 0) {
            sb.append(prop.getString("Year") + ": " + f.getYear() + "\n");
        }
        if (f.getCountry() != null) {
            sb.append(prop.getString("Country") + ": " + f.getCountry() + "\n");
        }
        basicInfo.setText(sb.toString());

        // Trailer
        Media media = new Media(f.getMedia_url());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        trailer.setMediaPlayer(mediaPlayer);
        mediaPlayer.setRate(3);
        mediaPlayer.setStartTime(new Duration(10000));
        mediaPlayer.setStopTime(new Duration(30000));
        mediaPlayer.setMute(true);
        mediaPlayer.play();
    }


    /**
     * Sets up film by their type.
     * <p>
     * This method can fetch the films under the film path. and add films to the a list
     * Then, it will be fetched by a type manager (TypeMenuController). And the type manager
     * can get all the film under its specified type.
     * </p>
     * @param type the type
     * @return the films list
     */
    public LinkedList<Film> setUpFilmByType(String type) {
        try {
            LinkedList<Film> film_list = new LinkedList<>();
            String sql = "select `id`, `name`, `duration`, `year`, `type`, `intro_url`, `media_url`, `img_url`, `country` from `film` where `type` = \"" + type + "\"";
            ResultSet films_result = DbConnection.query(sql);
            try {
                while (films_result.next()) {
                    Film f = new Film();
                    f.setId(films_result.getInt(1));
                    f.setName(films_result.getString(2));
                    f.setDuration(films_result.getString(3) + "min");
                    f.setYear(films_result.getInt(4));
                    f.setType(films_result.getString(5));
                    f.setIntro_url(films_result.getString(6));
                    f.setMedia_url(films_result.getString(7));
                    f.setImg_url(films_result.getString(8));
                    f.setCountry(films_result.getString(9));
                    addDirectorsAndActors(f);
                    film_list.add(f);
                }
                return film_list;
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

    /**
     * This method add directors and actors to film
     * @param f film
     */
    private void addDirectorsAndActors(Film f) {
        try {
            String actor_qry = "select `actor` from `film_actor` where `id` = " + f.getId() + ";";
            String director_qry = "select `director` from `film_director` where `id` = " + f.getId() + ";";
            ResultSet actor_set = DbConnection.query(actor_qry);
            ResultSet director_set = DbConnection.query(director_qry);
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

    /**
     * Gets the whole pane (split pane).
     *
     * @return the split pane
     */
    public SplitPane getFilmListSplitPane() {
        return filmListSplitPane;
    }


    /**
     * Gets scene.
     *
     * @return the scene
     */
    public Scene getScene() {
        if (scene == null) {
            scene = new Scene(this.filmListSplitPane);
        }
        return scene;
    }

    /**
     * Get the string according to App.property
     * If the string is not as a key, it returns itself
     * @param s
     * @return
     */
    private static String getPropString(String s) {
        String str;
        try {
            str = App.property.getString(s);
        } catch (MissingResourceException e) {
            str = s;
        }
        return str;
    }
}
