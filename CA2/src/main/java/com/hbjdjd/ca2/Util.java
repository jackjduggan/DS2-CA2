package com.hbjdjd.ca2;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;

public class Util {

    public static boolean withinBoundsForMatrix(int x, int y, int width, int height) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public static void messageAlert(String title, String message)
    {
        Alert a = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        ShowAlert(title, a);
    }

    private static void ShowAlert(String title, Alert a)
    {
        Stage s = (Stage) a.getDialogPane().getScene().getWindow();
        s.setTitle(title);
        //Why do I have to use AWT? JavaFX is better in every way except this...
        ((Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.asterisk")).run();
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(App.stage);
        s.show();
        s.requestFocus();
    }
}
