package app.controllers;

import app.Film;
import app.Main;
import dao.DbConnection;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class ListMenuController {


    public void goBack() {
        System.out.println("goBack");
        Main.goToTypeMenu();
    }

    public static LinkedList<Film> queryFilmByType(String type) {
        try {
            LinkedList<Film> lst = new LinkedList<>();
            String sql = "select `id`, `name`, `duration`, `year`, `type`, `intro_url`, `media_url`, `img_url` from `film` where `type` = \"" + type + "\"";
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

    public static void playFilm(Film f) {
        String qry = "select media_url from film where id = " + f.getId() + ";";
        try {
            ResultSet result = DbConnection.query(qry);
            if (result.next()) {
                Main.goToMediaPlayer(result.getString(1));
            } else {
                System.err.println("no such film");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
