package app.controllers;

import app.Main;
import app.datatype.Language;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;


/**
 * The type Language menu controller. It controls languageMenu.fxml
 * <p>
 *     This Controller offers methods for changing languages.
 * </p>
 *
 */
public class LanguageMenuController {
    /**
     * Gets language pane, which contains an anchor pane and with its child a single list view.
     *
     * @return the language pane
     */
    public AnchorPane getLanguagePane() {
        return languagePane;
    }

    /**
     * Gets scene.
     *
     * @return the scene
     */
    public Scene getScene() {
        if (scene == null) {
            scene = new Scene(this.languagePane);
        }
        return scene;
    }

    private Scene scene;
    @FXML
    private AnchorPane languagePane;

    /**
     * Change language.
     * <p>
     *     According to the selected item, the language is thus specified.
     * </p>
     *
     *
     * @param e the event, which can specify which element is selected and activated.
     */
    public void changeLanguage(Event e) {
        if (e.getEventType().getName().equals("KEY_PRESSED")) {
            KeyEvent key_event = (KeyEvent) e;
            if (!key_event.getCode().isWhitespaceKey()) {
                return;
            }
        }
        ListView<Language> lan_lst_view = (ListView) languagePane.getChildren().get(0);
        Language lan = lan_lst_view.getSelectionModel().getSelectedItem();
        setLanguage(lan);
        Main.getPrimaryStage().setScene(Main.getsStartMenuController().getScene());
    }

    /**
     * Sets language.
     *
     * @param lan the language that is going to be set.
     */
    private void setLanguage(Language lan) {
        Main.cur_language = lan.toString().split("_")[1];
        try {
            Main.getPrimaryStage().close();
            new Main().start(Main.getPrimaryStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("set language " + lan);
    }
}
