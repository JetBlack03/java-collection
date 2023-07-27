
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author nucle
 */
public class StudentQueries {
    
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement addStudent;
    private static PreparedStatement getSemesterList;
    private static PreparedStatement dropStudent;
    private static ResultSet resultSet;
    private static ResultSet resultSet2;
    private static ResultSet resultSet3;

    
    public static void addStudent(StudentEntry student) {
        connection = DBConnection.getConnection();
        try
        {
            addStudent = connection.prepareStatement("insert into app.student (studentid, firstname, lastname) values (?,?,?)");
            addStudent.setString(1, student.getStudentID());
            addStudent.setString(2, student.getFirstName());
            addStudent.setString(3, student.getLastName());
            addStudent.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

    }
    
    public static ArrayList<StudentEntry>   getAllStudents() {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> student = new ArrayList<StudentEntry>();
        try
        {
            getSemesterList = connection.prepareStatement("select studentid from app.student order by studentid");
            resultSet = getSemesterList.executeQuery();
            getSemesterList = connection.prepareStatement("select firstname from app.student order by studentid");
            resultSet2 = getSemesterList.executeQuery();
            getSemesterList = connection.prepareStatement("select lastname from app.student order by studentid");
            resultSet3 = getSemesterList.executeQuery();

            while(resultSet.next())
            {
                resultSet2.next();
                resultSet3.next();
                student.add(new StudentEntry(resultSet.getString(1),resultSet2.getString(1),resultSet3.getString(1)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return student;
        
    }
    public static StudentEntry getStudent(String studentid) {
        connection = DBConnection.getConnection();
        try
        {
            getSemesterList = connection.prepareStatement("select firstname from app.student where studentid = (?)");
            getSemesterList.setString(1, studentid);
            resultSet = getSemesterList.executeQuery();
            getSemesterList = connection.prepareStatement("select lastname from app.student where studentid = (?)");
            getSemesterList.setString(1, studentid);
            resultSet2 = getSemesterList.executeQuery();

            resultSet.next();
            resultSet2.next();
             
            return new StudentEntry(studentid,resultSet.getString(1),resultSet2.getString(1));
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return null;
        
    }
    
    public static void dropStudent(String studentid) {
        connection = DBConnection.getConnection();
        try
        {
            dropStudent = connection.prepareStatement("delete from app.student where studentid = (?)");
            dropStudent.setString(1, studentid);
            dropStudent.executeUpdate();
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        

    }
}
