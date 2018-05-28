package app;

import custom.CustomUtils;
import custom.Customizable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;


public class Main extends Application {

    private static Pane mMenuPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mMenuPane = FXMLLoader.load(getClass().getResource("typeMenu.fxml"));
        primaryStage.setScene(new Scene(mMenuPane, 600, 400));
        primaryStage.show();
        runCustomSettings();
    }

    public static Pane getPane() {
        return mMenuPane;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void runCustomSettings() {
        List<Customizable> custom_list = CustomUtils.getCustomList();
        for (Customizable c : custom_list) {
            c.customSetup();
        }
    }

}
