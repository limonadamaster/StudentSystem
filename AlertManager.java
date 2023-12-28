package com.example.studentssystem;

import javafx.scene.control.Alert;

public class AlertManager {
    public static void showErrorMessage(String specificErrorText)   {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(specificErrorText);
        alert.show();
    }
}
