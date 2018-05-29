import custom.*;
import app.*;
public class Client {

    public static void main(String[] args) {
        CustomUtils.addCustom(() -> {
            CustomUtils.addMovieType("Action", "Comedy", "Horror", "Others");
            CustomUtils.changeTypeNameById("type1", "Documentary");
        });
        Main.main(args);
    }
}
