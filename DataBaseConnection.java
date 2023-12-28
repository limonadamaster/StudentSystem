package com.example.studentssystem;

import com.example.studentssystem.student.Student;
import javafx.stage.FileChooser;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class DataBaseConnection {
    public final String selectUsersUsername="SELECT * FROM Admins WHERE Username = ?";

    public List<Subject> readSubect() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Subject subject=null;
        List<Subject> subjectList =null;

        connection = this.connectToDatabase();
        String selectQuert = "SELECT * FROM Subjects";
        preparedStatement = connection.prepareStatement(selectQuert);
        ResultSet resultSet = preparedStatement.executeQuery();

        subjectList = new LinkedList<>();
        while(resultSet.next()){

            subject = new Subject(resultSet.getInt("SubjectID"),resultSet.getString("SubjectName"));

            subjectList.add(subject);
        }
        return subjectList;
    }
    public void insertStudent(Student student ,String imgPath) throws IOException, SQLException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            DataBaseConnection dataBaseConnection = new DataBaseConnection();
            //Put img

            FileInputStream imageInputStream=new FileInputStream(imgPath);

            connection = dataBaseConnection.connectToDatabase();
            //Start transaction
            connection.setAutoCommit(false);
            //Insert Student
            String insertSQL = "INSERT INTO Students (FirstName, SecondName,LastName,CollegeNumber,ImageData) VALUES (?, ?, ?, ?,?)";
            preparedStatement = connection.prepareStatement(insertSQL);

            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getSecondName());
            preparedStatement.setString(3, student.getThirdName());
            preparedStatement.setString(4, student.getCollegeNumber());
            preparedStatement.setBinaryStream(5, imageInputStream);

            // Execute the SQL statement
            preparedStatement.executeUpdate();

            //Commit transaction
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
        closeDatabaseConnection(connection,preparedStatement,null,null);

    }
    public List<Student> readStudent() throws SQLException {
        FileInputStream fileInputStream = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List studentList = null;
        Student student = null;
        try {
            connection = this.connectToDatabase();
            String selectQueryStudent = "SELECT * FROM Students";
            preparedStatement = connection.prepareStatement(selectQueryStudent);
            ResultSet resultSet = preparedStatement.executeQuery();
            Map<Subject,Grade> subjectGradeMap;
            studentList = new ArrayList<>();
            while (resultSet.next()) {
                //Set data to student
                student = new Student(resultSet.getInt("StudentID"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("SecondName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("CollegeNumber"),
                        takeBytesFromImage( resultSet.getBinaryStream("ImageData")).toByteArray()
                );
                student.setSubjectGradeMap(getGradeSubject(student));
                studentList.add(student);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            closeDatabaseConnection(connection,preparedStatement,null,null);
        }
            return studentList;
    }

    private Map<Subject,Grade> getGradeSubject(Student student) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        connection = this.connectToDatabase();
        String query="SELECT s.SubjectID,s.SubjectName,g.GradeID, g.Grade\n" +
                "FROM Grades g\n" +
                "JOIN Subjects s ON g.SubjectID = s.SubjectID\n" +
                "WHERE g.StudentID = ?;";
        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1,student.getId());

        ResultSet resultSet = preparedStatement.executeQuery();
        Map<Subject,Grade> subjectGradeMap = new HashMap<>();
        while(resultSet.next()){
            Subject subject = new Subject(resultSet.getInt("SubjectID"),resultSet.getString("SubjectName"));
            Grade grade = new Grade(resultSet.getInt("GradeID"),resultSet.getDouble("Grade"));
            subjectGradeMap.put(subject,grade);
        }
            return subjectGradeMap;
    }

    public void insertGradeToStudent(Student student,Subject subject,Grade grade) throws SQLException {
        Connection connection =null;
        PreparedStatement preparedStatement=null;

try {
    connection = this.connectToDatabase();
    connection.setAutoCommit(false);

    String sqlQuery = "INSERT INTO Grades(StudentID,SubjectID,Grade)" +
            "VALUES(?,?,?)";
    preparedStatement = connection.prepareStatement(sqlQuery);

    preparedStatement.setInt(1, student.getId());
    preparedStatement.setInt(2, subject.getId());
    preparedStatement.setDouble(3, grade.getGrade());

    preparedStatement.executeUpdate();

    connection.commit();
}catch (SQLException e ){
    e.printStackTrace();
    connection.rollback();
}finally {
    closeDatabaseConnection(connection,preparedStatement,null,null);
}
    }
    public Connection connectToDatabase() throws SQLException {
        Connection connection;
        String databaseName = "users";
        String databaseUser = "root";
        String databasePassword = "";
        String databaseUrl = "jdbc:mysql://localhost/"+databaseName;
        connection = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword);

        return connection;
    }

    public void closeDatabaseConnection(Connection connection,PreparedStatement psInsert,
                                        PreparedStatement psCheckUserExists,ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
        if (psInsert != null) {
            psInsert.close();
        }
        if (psCheckUserExists != null) {
            psCheckUserExists.close();
        }
        if (connection != null) {
            connection.close();
        }

    }

    private ByteArrayOutputStream takeBytesFromImage(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if(inputStream!=null) {
            byte[] buffer = new byte[4096];
            int byteReader;
            while ((byteReader = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, byteReader);
            }
        }
        return outputStream;
    }
}
