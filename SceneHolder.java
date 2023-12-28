package com.example.studentssystem.scenes;

import com.example.studentssystem.Main;
import com.example.studentssystem.student.Student;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class SceneHolder {
public void changeScene(ActionEvent actionEvent, Stage previousStage, String fxmlFile, Student student) throws IOException, SQLException {

    FXMLLoader fxmlLoader = new FXMLLoader();
    Parent root = fxmlLoader.load(Main.class.getResource(fxmlFile).openStream());

    Stage stage = new Stage();
    stage.centerOnScreen();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setScene(new Scene(root, 800, 700));


    if(previousStage!=null){
        previousStage.close();
    }
    stage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if(keyEvent.getCode()== KeyCode.ESCAPE){
                stage.close();
            }
        }
    });
    stage.show();
}
}
