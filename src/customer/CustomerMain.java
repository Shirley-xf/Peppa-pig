package customer;
import app.*;

public class CustomerMain {

    public static void customSetup() throws Exception {
        CustomerUtils.addMovieType("Action", "Comedy", "Horror", "Others");
        CustomerUtils.changeTypeNameById("type1", "Documentary");
    }

}
