import custom.*;
import app.*;

public class Client {

    public static void main(String[] args) {
        CustomUtils.addCustom(() -> {
            CustomUtils.addMovieType("Action", "Comedy", "Horror", "Others");
            CustomUtils.changeType("Type1", "Documentary");
        });
        Main.main(args);
    }
}
