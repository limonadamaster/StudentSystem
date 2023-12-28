package com.example.studentssystem.controller;
import com.example.studentssystem.DataBaseConnection;
import com.example.studentssystem.Grade;
import com.example.studentssystem.Subject;
import com.example.studentssystem.student.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class RegisterStudentGrades  {

    @FXML
    private TextField tfSearchBar;
    @FXML
    private AnchorPane apStudentGrades;
    @FXML
    private HBox hbStudentGrades;
    @FXML
    private TableView<Student> tbStudents;
    @FXML
    private TableColumn columnCollegeNumber;
    @FXML
    private TableColumn columnFirstName;
    @FXML
    private TableColumn columnSecondName;
    @FXML
    private TableColumn columnLastName;
    @FXML
    private ComboBox<Subject> cbSelectSubject;
    @FXML
    private TextField tfGrade;
    @FXML
    private Button btnSetGrade;


    final ObservableList<Student> studentsList= FXCollections.observableArrayList();
    public void initialize() throws SQLException {
        //set all Students form DB
        setStudent();
        //Map the tbStudents with Student fields
        columnCollegeNumber.setCellValueFactory(new PropertyValueFactory<>("collegeNumber"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));;
        columnSecondName.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("thirdName"));

        tbStudents.setItems(studentsList);
        //search the specific Student by his College number
        searchFilter();
        //select Student with double-click
        initializeDoubleClickTbView();

        setSubjectComboBox();

        btnSetGrade.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    setGradeToStudent();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

 private void setStudent() throws SQLException {
     DataBaseConnection dataBaseConnection = new DataBaseConnection();
     for(Student student:dataBaseConnection.readStudent()){
         studentsList.add(student);
     }

 }
    private void searchFilter() {
        // Create a FilteredList that initially includes all items from studentsList
        FilteredList<Student> filterData = new FilteredList<>(studentsList, e -> true);

        // Add a listener to the textProperty of tfSearchBar (your search input field)
        // This is a method that allows you to attach a listener to a property, in this case is implemented to search changes
        tfSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            // Set the filter predicate for the FilteredList based on the search input
            filterData.setPredicate(student -> {
                // If the search input is null or empty, show all items
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convert both the search input and the college number to lowercase for case-insensitive search
                String toLowerCaseFilter = newValue.toLowerCase();
                String studentCollegeNumber = student.getCollegeNumber().toLowerCase();

                // Check if the college number contains the search input
                if (studentCollegeNumber.contains(toLowerCaseFilter)) {
                    return true; // Show the item in the filtered list
                }

                // If no match is found, exclude the item from the filtered list
                return false;
            });
        });

        // Create a SortedList and bind its comparator to the TableView's comparator
        SortedList<Student> students = new SortedList<>(filterData);
        students.comparatorProperty().bind(tbStudents.comparatorProperty());

        // Set the TableView's items to the sorted and filtered list
        tbStudents.setItems(students);
    }

    private void initializeDoubleClickTbView(){
        tbStudents.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)&&event.getClickCount()==2){
                try {
                    handleTableViewDoubleClick();
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void handleTableViewDoubleClick() throws IOException, SQLException {
        Student selectdStudent = tbStudents.getSelectionModel().getSelectedItem();
        if(selectdStudent==null){
            return;
        }else{
            RegisterNewStudent registerNewStudent = new RegisterNewStudent();
            registerNewStudent.openStudentDetails(selectdStudent);
        }
    }

    private void setSubjectComboBox() throws SQLException {
            DataBaseConnection  dataBaseConnection = new DataBaseConnection();
            for(Subject subject:dataBaseConnection.readSubect()){
                cbSelectSubject.getItems().add(subject);
            }
    }

    private void setGradeToStudent() throws SQLException {
        Student selectdStudent = new Student(tbStudents.getSelectionModel().getSelectedItem());
        Subject selectedSubject= cbSelectSubject.getSelectionModel().getSelectedItem();
        Grade grade = new Grade(Double.parseDouble(tfGrade.getText()));

        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        dataBaseConnection.insertGradeToStudent(selectdStudent,selectedSubject,grade);

    }
}
