package com.example.studentssystem.controller;

import com.example.studentssystem.DataBaseConnection;
import com.example.studentssystem.Grade;
import com.example.studentssystem.Main;
import com.example.studentssystem.Subject;
import com.example.studentssystem.scenes.SceneHolder;
import com.example.studentssystem.student.Student;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class RegisterNewStudent extends SceneHolder implements Initializable {
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfSecondName;
    @FXML
    private TextField tfThirdName;
    @FXML
    private TextField tfCollegeNumber;
    @FXML
    private Button btnAddStudent;
    @FXML
    private ImageView imgviewPhotoOfStudent;
    @FXML
    private Button btnAddStudentImage;
    private String imgPath ;
    @FXML
  private  TableView<Map.Entry<Subject, Grade>> tableView;
    @FXML
   private TableColumn<Map.Entry<Subject, Grade>,String> subjectColumn;
    @FXML
  private  TableColumn<Map.Entry<Subject, Grade>,String> gradeColumn;
    @FXML
    private HBox vbTable;

    @FXML
    private Button btnGrades;
    public void openStudentDetails(Student student) throws IOException, SQLException {
        changeScene(null,null,"register-new-student.fxml",student);
    }
@Override
     public void changeScene(ActionEvent actionEvent, Stage previousStage, String fxmlFile, Student student) throws IOException, SQLException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(Main.class.getResource(fxmlFile).openStream());


    RegisterNewStudent controller = fxmlLoader.getController();
    controller.setupTableView(student);

    vbTable =(HBox) root;
    vbTable.getChildren().add(1,controller.tableView);

        Stage stage = new Stage();
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(new Scene(vbTable, 800, 700));


    // Now, the controller is initialized and the TextField should be accessible

    // Set the student's first name in the TextField
    controller.tfFirstName.setText(student.getFirstName());
    controller.tfCollegeNumber.setText(student.getCollegeNumber());
    controller.tfSecondName.setText(student.getSecondName());
    controller.tfThirdName.setText(student.getThirdName());
    controller.imgviewPhotoOfStudent.setImage(student.getImageData());

    //Disable buttons for adding student, READ-ONLY
    controller.btnAddStudentImage.setDisable(true);
    controller.btnAddStudent.setDisable(true);
    controller.btnGrades.setDisable(true);

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

    private void setupTableView(Student student) throws SQLException {
        DataBaseConnection dataBaseConnection = null;

        ObservableList<Map.Entry<Subject, Grade>> subjectsList = FXCollections.observableArrayList(student.getSubjectGradeMap().entrySet());

        subjectColumn.setCellValueFactory(param-> new SimpleStringProperty(param.getValue().getKey().getNameOfSubject()));
        gradeColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getGrade())));

        tableView.getColumns().addAll(subjectColumn, gradeColumn);
        tableView.setItems(subjectsList);
    }


    private void setTableView(Student student){
     //   tvSubjects.getItems().add(student.getSubjectGradeMap());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnAddStudentImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                String fileParh = new String(fileChooser.showOpenDialog(null).getAbsolutePath());
                imgPath = "file:///"+ fileParh;

                Image image = new Image(imgPath);
                imgviewPhotoOfStudent.setImage(image);

                imgPath=fileParh;
            }
        });
        btnGrades.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    SceneHolder sceneHolder = new SceneHolder();
                sceneHolder.changeScene(event,null,"register-student-grades.fxml",null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnAddStudent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DataBaseConnection dataBaseConnection = new DataBaseConnection();
                try {
                    if(tfFirstName.getText().isEmpty()){
                        return;
                    }
                    Student student = new Student(0,
                            tfFirstName.getText().trim(),
                            tfSecondName.getText().trim(),
                            tfThirdName.getText().trim(),
                            tfCollegeNumber.getText().trim(),null);

                    //Insert Student with his image
                    dataBaseConnection.insertStudent(student,imgPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
