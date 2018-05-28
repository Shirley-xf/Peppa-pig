package app;

import customer.CustomerMain;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class TypeMenu extends Application {

    private static Pane mPane;
    @Override
    public void start(Stage primaryStage) throws Exception {
        mPane = FXMLLoader.load(getClass().getResource("typeMenu.fxml"));
        primaryStage.setScene(new Scene(mPane, 600, 400));
        primaryStage.show();
        runCustomerSettings();
    }

    public static Pane getPane() {
        return mPane;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void runCustomerSettings() throws Exception {
        CustomerMain.customSetup();
    }

}
