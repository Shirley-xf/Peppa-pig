package app;

import app.controllers.TypeMenuController;
import custom.CustomUtils;
import custom.Customizable;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class Main extends Application {

    private static Pane mMenuPane;
    private static Parent listMenuPar;
    private static Stage mPrimaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        mPrimaryStage = primaryStage;
        mMenuPane = FXMLLoader.load(getClass().getResource("typeMenu.fxml"));
        ObservableList children = mMenuPane.getChildren();
        initDefaultBtn(children);
        mPrimaryStage.setScene(new Scene(mMenuPane, 600, 400));
        mPrimaryStage.show();
        listMenuPar = FXMLLoader.load(getClass().getResource("listMenu.fxml"));
        runCustomSettings();
    }

    public static Pane getMenuPane() {
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

    public static void switchToListMenu() {

        Scene scene = new Scene(listMenuPar);
        mPrimaryStage.setScene(scene);
        mPrimaryStage.show();
    }

    private void initDefaultBtn(ObservableList oblst) {
        FilteredList<Button> flst = oblst.filtered(Button -> true);
        for (Button btn : flst) {
            switch (btn.getId()) {
                case "type1":
                    btn.setOnAction(e -> TypeMenuController.getFilmByType(btn.getText()));
                    break;
                default:
                    break;
            }
        }
    }
}
