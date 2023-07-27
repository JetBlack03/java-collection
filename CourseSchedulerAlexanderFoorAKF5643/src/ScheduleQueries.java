
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.Calendar;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author nucle
 */
public class ScheduleQueries {

    private static Connection connection;
    private static PreparedStatement addSchedule;
    private static PreparedStatement getScheduleList;
    private static PreparedStatement getCourseSeats;
    private static PreparedStatement deleteCourse;
    private static PreparedStatement updateEntry;
    private static ResultSet resultSet;
    private static ResultSet resultSet2;
    private static ResultSet resultSet3;

    public static void addScheduleEntry(ScheduleEntry entry){
        connection = DBConnection.getConnection();
        try
        {
            addSchedule = connection.prepareStatement("insert into app.schedule (semester, studentid, coursecode, status, timestamp) values (?,?,?,?,?)");
            addSchedule.setString(1, entry.getSemester());
            addSchedule.setString(2, entry.getStudentID());
            addSchedule.setString(3, entry.getCourseCode());
            addSchedule.setString(4, entry.getStatus());
            addSchedule.setTimestamp(5,  entry.getTimestamp());
            
            addSchedule.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

    }
    
    public static ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String studentID) {
   //     PreparedStatement addSemester = connection.prepareStatement("select count(studentID) from app.schedule where studentID = ? and courseCode = ?");
        
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> schedule = new ArrayList<ScheduleEntry>();
        try
        {
            getScheduleList = connection.prepareStatement("select coursecode from app.schedule where semester = (?) and studentid = (?) order by coursecode");
            getScheduleList.setString(1, semester);
            getScheduleList.setString(2, studentID);
            resultSet = getScheduleList.executeQuery();
            getScheduleList = connection.prepareStatement("select status from app.schedule where semester = (?) and studentid = (?) order by coursecode");
            getScheduleList.setString(1, semester);
            getScheduleList.setString(2, studentID);
            resultSet2 = getScheduleList.executeQuery();
            getScheduleList = connection.prepareStatement("select timestamp from app.schedule where semester = (?) and studentid = (?) order by coursecode");
            getScheduleList.setString(1, semester);
            getScheduleList.setString(2, studentID);
            resultSet3 = getScheduleList.executeQuery();

            while(resultSet.next())
            {
                resultSet2.next();
                resultSet3.next();
                schedule.add(new ScheduleEntry(semester, studentID, resultSet.getString(1),resultSet2.getString(1),resultSet3.getTimestamp(1)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return schedule;

    }
    
    public static int getScheduledStudentCount(String currentSemester, String courseCode) {
        int count = 0;
        try
        {
            getCourseSeats = connection.prepareStatement("select count(studentid) from app.schedule where semester = (?) and courseCode = (?)");

            getCourseSeats.setString(1, currentSemester);
            getCourseSeats.setString(2, courseCode);
            resultSet = getCourseSeats.executeQuery();
            resultSet.next();
            count =  resultSet.getInt(1);
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace(); 
        }
        return count;
    }
    
    
    public static ArrayList<ScheduleEntry> getScheduledStudentsByCourse(String semester, String courseCode){
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> schedule = new ArrayList<ScheduleEntry>();
        try
        {
            getScheduleList = connection.prepareStatement("select studentid from app.schedule where semester = (?) and coursecode = (?) and status = 'S' order by studentid");
            getScheduleList.setString(1, semester);
            getScheduleList.setString(2, courseCode);
            resultSet = getScheduleList.executeQuery();
            getScheduleList = connection.prepareStatement("select timestamp from app.schedule where semester = (?) and coursecode = (?) and status = 'S' order by studentid");
            getScheduleList.setString(1, semester);
            getScheduleList.setString(2, courseCode);
            resultSet2 = getScheduleList.executeQuery();

            while(resultSet.next())
            {
                resultSet2.next();
                schedule.add(new ScheduleEntry(semester, resultSet.getString(1), courseCode, "S", resultSet2.getTimestamp(1)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return schedule;

    }
    public static ArrayList<ScheduleEntry> getWaitlistedStudentsByCourse(String semester, String courseCode){
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> schedule = new ArrayList<ScheduleEntry>();
        try
        {
            getScheduleList = connection.prepareStatement("select studentid from app.schedule where semester = (?) and coursecode = (?) and status = 'W' order by timestamp asc");
            getScheduleList.setString(1, semester);
            getScheduleList.setString(2, courseCode);
            resultSet = getScheduleList.executeQuery();
            getScheduleList = connection.prepareStatement("select timestamp from app.schedule where semester = (?) and coursecode = (?) and status = 'W' order by timestamp asc");
            getScheduleList.setString(1, semester);
            getScheduleList.setString(2, courseCode);
            resultSet2 = getScheduleList.executeQuery();

            while(resultSet.next())
            {
                resultSet2.next();
                schedule.add(new ScheduleEntry(semester, resultSet.getString(1), courseCode, "W", resultSet2.getTimestamp(1)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return schedule;
    }

    public static void dropStudentScheduleByCourse(String semester, String studentID, String courseCode){
        connection = DBConnection.getConnection();
        try
        {
            deleteCourse = connection.prepareStatement("delete from app.schedule where semester = (?) and studentid = (?) and coursecode = (?)");
            deleteCourse.setString(1, semester);
            deleteCourse.setString(2, studentID);
            deleteCourse.setString(3, courseCode);
            deleteCourse.executeUpdate();
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

    }
    
    public static void dropScheduleByCourse(String semester, String courseCode){
        connection = DBConnection.getConnection();
        try
        {
            deleteCourse = connection.prepareStatement("delete from app.schedule where semester = (?) and coursecode = (?)");
            deleteCourse.setString(1, semester);
            deleteCourse.setString(2, courseCode);
            deleteCourse.executeUpdate();
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

    }
    
    public static void updateScheduleEntry(String semester, ScheduleEntry entry){
        connection = DBConnection.getConnection();
        try
        {
            updateEntry = connection.prepareStatement("update app.schedule set status = 'S' where semester = (?) and coursecode = (?) and studentid = (?)");
            updateEntry.setString(1, semester);
            updateEntry.setString(2, entry.getCourseCode());
            updateEntry.setString(3, entry.getStudentID());
            updateEntry.executeUpdate();
            
        }
        catch(SQLException sqlException) 
        {
            sqlException.printStackTrace();
        }

    }
}
