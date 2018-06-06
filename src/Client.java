import custom.*;
import app.*;
import utils.FilmInfoParser;

public class Client {

    public static void main(String[] args) {
        CustomUtils.addCustomToPrev(() -> {
            new FilmInfoParser().parse("");
        });
        Main.main(args);
    }
}
