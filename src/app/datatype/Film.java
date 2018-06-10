package app.datatype;

import app.Main;
import javafx.scene.Node;

import java.util.List;
import java.util.MissingResourceException;

public class Film {
    private int id;
    private String name;
    private String duration;
    private int year;
    private String country;
    private String type;
    private String intro_url;
    private String media_url;
    private String img_url;
    private List<String> actors;
    private List<String> directors;



    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntro_url() { return intro_url; }

    public void setIntro_url(String intro_url) { this.intro_url = intro_url; }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) { this.media_url = media_url; }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {

        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        String s;
        try {
            s = Main.property.getString(name);
        } catch (MissingResourceException e) {
            s = name;
        }
        return s;
    }
}
