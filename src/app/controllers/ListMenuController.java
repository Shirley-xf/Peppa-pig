package app.controllers;

import app.datatype.Film;
import app.Main;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class ListMenuController {
    private Scene scene;
    @FXML private SplitPane filmListSplitPane;
    @FXML private ImageView poster;
    @FXML private Button back;
    @FXML private Button play;
    @FXML private MediaView trailer;
    @FXML private Label intro;
    @FXML private Label basicInfo;

    public void goBack() {
        Main.getPrimaryStage().setScene(Main.getsTypeMenuController().getScene());
        Main.getPrimaryStage().show();
    }

    public void showAllInfo(Film f) {

        Properties prop = new Properties();
        String propertyFile = Main.BASE_PATH + Main.FILE_SYSTEM.getSeparator()
            + "data" + Main.FILE_SYSTEM.getSeparator() + "properties" + Main.FILE_SYSTEM.getSeparator() + "default_" +
            Main.cur_language + ".properties";
        try {
            prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(propertyFile),"UTF-8")));
        } catch (IOException e) {
            System.err.println("Please refer the properties file in Main class");
            System.exit(1);
        }

         // Poster
        try {
            Image img = new Image(f.getImg_url());
            poster.setImage(img);
        } catch (Exception e) {
            System.out.println(f.getImg_url());
            System.err.println("Image " + e);
        }

        //TODO : change text
        //TODO: fxml text element translated by properties' setting.
        // Text：
        StringBuilder sb = new StringBuilder(prop.getProperty("Introduction") + ":\n");
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


        sb = new StringBuilder(prop.getProperty("Actors") + ":\n");
        List<String> actors = f.getActors();
        List<String> directors = f.getDirectors();
        for (String s : actors) sb.append(s + "\n");
        sb.append(prop.getProperty("Directors") + ":\n");
        for (String s : directors) sb.append(s + "\n");
        sb.append(prop.getProperty("Duration") + ": ");
        sb.append(f.getDuration() + "\n");
//        sb.append("Year: ");
//        sb.append((f.getYear() == 0) ? "" : f.getYear());
        if (f.getYear() != 0) {
            sb.append(prop.getProperty("Year") + ": " + f.getYear() + "\n");
        }
        if (f.getCountry() != null) {
            sb.append(prop.getProperty("Country") + ": " + f.getCountry() + "\n");
        }
        basicInfo.setText(sb.toString());

        // Trailer
        Media media = new Media(f.getMedia_url());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        trailer.setMediaPlayer(mediaPlayer);
        mediaPlayer.setRate(3);
        mediaPlayer.setStartTime(new Duration(10000));
        mediaPlayer.setStopTime(new Duration(30000));
        mediaPlayer.play();
    }


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
    public SplitPane getFilmListSplitPane() {
        return filmListSplitPane;
    }


    public Scene getScene() {
        if (scene == null) {
            scene = new Scene(this.filmListSplitPane);
        }
        return scene;
    }

}
