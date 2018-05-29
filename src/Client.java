import custom.*;
import app.*;
import dao.DbConnection;

public class Client {

    public static void main(String[] args) {
        DbConnection.setDbUrl("/Users/num9527/IdeaProjects/Inflight-Entertainment-System/dbres/In-flight_Entertainer.db");
        CustomUtils.addCustom(() -> {
            CustomUtils.addMovieType("Action", "Comedy", "Horror", "Others");
            CustomUtils.changeTypeNameById("type1", "Documentary");
        });
        Main.main(args);
    }
}
