package app.controllers;


import app.Main;


public class TypeMenuController {


    public static void getFilmByType(String text) {
        System.out.println("getFilmByType" + " " + text);
        Main.goToListMenuAndShow(text);
    }

    public void goBack() {
        System.out.println("goBack");
    }

    public void goToMainMenu() {
        System.out.println("goToMainMenu");
    }
}
