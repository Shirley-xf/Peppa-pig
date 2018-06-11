import custom.*;
import app.*;
import dao.DbConnection;
import utils.FilmInfoParser;

import java.io.File;


/**
 * Main class to run the application
 */
public class Main {

    /**
     *
     * Here is the field to interact with CustomUtils
     * and sends custom settings to the App.
     *
     * Also the url of data base should be set up.
     *
     * @param args
     */
    public static void main(String[] args) {

        DbConnection.setDbUrl("");
        CustomUtils.addCustomAtPrev(() -> {
            switch (App.currentLanguage) {
                case "fr":
                    App.setIntroPath(App.BASE_PATH
                            + File.separator + "data"
                            + File.separator + "introductions"
                            + File.separator + "fr");
                    break;
                case "zh":
                    App.setIntroPath(App.BASE_PATH
                            + File.separator + "data"
                            + File.separator + "introductions"
                            + File.separator + "zh");
                    break;
                default:
                    App.setIntroPath("");
                    break;
            }
        });

        CustomUtils.addCustomToInit(() -> {

            new FilmInfoParser().parse("", App.getIntroPath());
            App.setFilmPath("");
            App.setPropertiesPath("");
        });

        CustomUtils.addCustomAtPost(() -> {
            FilmInfoParser.setYear("Pulp Fiction", 2010);
            FilmInfoParser.setCountry("Pulp Fiction", "unknown");
        });


        App.boot(args);
    }
}
