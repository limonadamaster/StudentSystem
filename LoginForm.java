package com.example.studentssystem.controller;

import com.example.studentssystem.AlertManager;
import com.example.studentssystem.DataBaseConnection;
import com.example.studentssystem.Encryptor;
import com.example.studentssystem.HelloApplication;
import com.example.studentssystem.scenes.SceneHolder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

public class LoginForm implements Initializable {


    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Button btnLogIn;
    @FXML
    private Button btnSignUp;


    private static Stage loginStage;

    public static Stage getLoginStage() {
        return loginStage;
    }
    private Stage getStage(ActionEvent actionEvent){
        return  (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    }
    public static void setLoginStage(Stage loginStage) {
        LoginForm.loginStage = loginStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(logInUser(tfUsername.getText().trim(),tfPassword.getText().trim())){

                        SceneHolder sceneHolder = new SceneHolder();
                        sceneHolder.changeScene(event,getStage(event),"register-new-student.fxml",null);
                    }
                    else{
                        AlertManager.showErrorMessage("Password don't match!");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private boolean logInUser(String username, String password) throws SQLException, NoSuchAlgorithmException {

        DataBaseConnection dataBaseConnection = new DataBaseConnection();

        Connection connection=dataBaseConnection.connectToDatabase();
        PreparedStatement preparedStatement =connection.prepareStatement(dataBaseConnection.selectUsersUsername);
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if(!resultSet.isBeforeFirst()){
            AlertManager.showErrorMessage("Usern not found!");
        }

        if(resultSet.next()){
            String retrievedPassword = resultSet.getString("Password");

            Encryptor encryptor = new Encryptor();
            if(encryptor.checkPassword(password,retrievedPassword)){
                return true;
            }
        }
        return false;
    }
}
