import custom.*;
import app.*;
import utils.FilmInfoParser;

public class Client {

    public static void main(String[] args) {
        CustomUtils.addCustomToPrev(() -> {
            new FilmInfoParser().parse("");
        });
        CustomUtils.addCustomToPost(() -> {
            FilmInfoParser.setYear("Pulp Fiction", 2010);
            FilmInfoParser.setCountry("Pulp Fiction", "unknown");
        });
        Main.main(args);
    }
}
