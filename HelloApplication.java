package com.example.studentssystem;

import com.example.studentssystem.scenes.SceneHolder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import java.nio.channels.ConnectionPendingException;

public class HelloApplication extends Application {
    private static Stage loginStage;

    public static void setLoginStage(Stage loginStage) {
        HelloApplication.loginStage = loginStage;
    }

    public static Stage getLoginStage() {
        return loginStage;
    }

    @Override
    public void start(Stage stage) throws IOException, NoSuchAlgorithmException, SQLException {
        openWindow(stage);
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        launch();
    }

    private void openWindow(Stage stage) throws IOException, NoSuchAlgorithmException, SQLException {
        setLoginStage(stage);
        SceneHolder sceneHolder = new SceneHolder();
        sceneHolder.changeScene(null,null,"login-form.fxml",null);
    }
}