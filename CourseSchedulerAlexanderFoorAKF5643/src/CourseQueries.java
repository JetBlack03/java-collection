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
public class CourseQueries {
    
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement addCourse;
    private static PreparedStatement getCourseList;
    private static PreparedStatement getCourseCodes;
    private static PreparedStatement getCourseSeats;
    private static PreparedStatement deleteCourse;
    private static ResultSet resultSet;
    private static ResultSet resultSet2;
    private static ResultSet resultSet3;


    public static ArrayList<CourseEntry> getAllCourses(String semester) {
        connection = DBConnection.getConnection();
        ArrayList<CourseEntry> course = new ArrayList<CourseEntry>();
        try
        {
            getCourseList = connection.prepareStatement("select coursecode from app.course where semester = (?) order by coursecode ");
            getCourseList.setString(1, semester);
            resultSet = getCourseList.executeQuery();
            getCourseList = connection.prepareStatement("select description from app.course  where semester = (?) order by coursecode");
            getCourseList.setString(1, semester);
            resultSet2 = getCourseList.executeQuery();
            getCourseList = connection.prepareStatement("select seats from app.course  where semester = (?) order by coursecode  ");
            getCourseList.setString(1, semester);
            resultSet3 = getCourseList.executeQuery();

            while(resultSet.next())
            {
                resultSet2.next();
                resultSet3.next();
                course.add(new CourseEntry(semester, resultSet.getString(1),resultSet2.getString(1),resultSet3.getInt(1)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return course;

    }
    
    
    public static void addCourse(CourseEntry course) {
        connection = DBConnection.getConnection();
        try
        {
            addCourse = connection.prepareStatement("insert into app.course (semester, coursecode, description, seats) values (?,?,?,?)");
            addCourse.setString(1, course.getSemester());
            addCourse.setString(2, course.getCourseCode());
            addCourse.setString(3, course.getDescription());
            addCourse.setInt(4, course.getSeats());
            addCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

    }
    
    public static void dropCourse(String semester, String courseCode){
        connection = DBConnection.getConnection();
        try
        {
            deleteCourse = connection.prepareStatement("delete from app.course where semester = (?) and coursecode = (?)");
            deleteCourse.setString(1, semester);
            deleteCourse.setString(2, courseCode);
            deleteCourse.executeUpdate();
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

    }
    
    public static ArrayList<String> getAllCourseCodes(String semester) {
        connection = DBConnection.getConnection();
        ArrayList<String> courseCode = new ArrayList<String>();
        try
        {
            getCourseCodes = connection.prepareStatement("select coursecode from app.course where semester = (?) order by coursecode ");
            getCourseCodes.setString(1, semester);
            resultSet = getCourseCodes.executeQuery();
            
            while(resultSet.next())
            {
                courseCode.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return courseCode;        
    }
    
    public static int getCourseSeats(String semester, String courseCode) {
        
        int count = 0;
        try
        {
            getCourseSeats = connection.prepareStatement("select count(studentid) from app.schedule where semester = (?) and courseCode = (?)");

            getCourseSeats.setString(1, semester);
            getCourseSeats.setString(2, courseCode);
            resultSet = getCourseSeats.executeQuery();
            getCourseSeats = connection.prepareStatement("select seats from app.course where semester = (?) and courseCode = (?)");
            getCourseSeats.setString(1, semester);
            getCourseSeats.setString(2, courseCode);
            resultSet2 = getCourseSeats.executeQuery();
            resultSet.next();
            resultSet2.next();
            count = resultSet2.getInt(1) - resultSet.getInt(1);
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return count;
    }
}
