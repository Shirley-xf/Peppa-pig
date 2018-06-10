import custom.*;
import app.*;
import utils.FilmInfoParser;

import java.io.File;

public class Client {

    public static void main(String[] args) {

        CustomUtils.setPropertiesInfo(() -> {
            switch (Main.cur_language) {
                case "fr":
                    Main.setIntroPath(Main.BASE_PATH
                            + File.separator + "data"
                            + File.separator + "introductions"
                            + File.separator + "fr");
                    break;
                case "zh":
                    Main.setIntroPath(Main.BASE_PATH
                            + File.separator + "data"
                            + File.separator + "introductions"
                            + File.separator + "zh");
                    break;
                default:
                    Main.setIntroPath("");
                    break;
            }
        });
        CustomUtils.addCustomToInit(() -> {

            new FilmInfoParser().parse("", Main.getIntroPath());
            Main.setFilmPath("");
            Main.setPropertiesPath("");
        });
        CustomUtils.addCustom(() -> {
            FilmInfoParser.setYear("Pulp Fiction", 2010);
            FilmInfoParser.setCountry("Pulp Fiction", "unknown");
        });


        Main.main(args);
    }
}
