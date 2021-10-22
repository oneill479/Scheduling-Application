package Utilities;

/**
 * Class AppointmentDB.java
 */

/**
 *
 * @author Caleb O'Neill
 */

import Model.User;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static Model.User.*;

public class AppointmentDB {

    /**
     * Inserts an appointment into database
     * @param id
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param contactId
     */
    public static void insertAppointmentDB(int id, String title, String description, String location, String type, Timestamp start, Timestamp end,
                                           int customerId, int contactId) {

        // create INSERT prepared statement
        String preparedStatement = "INSERT INTO appointments(Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By," +
                                   " Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // INSERT statement

        // get current timestamp in utc time
        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp currentTimeStamp = Timestamp.valueOf(currentTime);

        // get user name
        ObservableList<User> currentUser = getCurrentUser();
        String userName = currentUser.get(0).getUserName();
        int userId = currentUser.get(0).getUserId();

        try {
            DBQuery.setPreparedStatement(JDBC.connection, preparedStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            ps.setInt(1, id);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, location);
            ps.setString(5, type);
            ps.setTimestamp(6, start);
            ps.setTimestamp(7, end);
            ps.setTimestamp(8, currentTimeStamp);
            ps.setString(9, userName);
            ps.setTimestamp(10, currentTimeStamp);
            ps.setString(11, userName);
            ps.setInt(12, customerId);
            ps.setInt(13, userId);
            ps.setInt(14, contactId);

            ps.execute();

            if (ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount() + " row(s) affected!");
            }
            else {
                System.out.println("No row(s) affected!");
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates an apointment in database
     * @param id
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param contactId
     */
    public static void updateAppointmentDB(int id, String title, String description, String location, String type, Timestamp start, Timestamp end,
                                           int customerId, int contactId) {

        // create UPDATE prepared statement
        String preparedStatement = "UPDATE appointments " +
                "SET Title = ?, " +
                "Description = ?, " +
                "Location = ?, " +
                "Type = ?, " +
                "Start = ?, " +
                "End = ?, " +
                "Last_Update = ?, " +
                "Last_Updated_By = ?, " +
                "Customer_ID = ?, " +
                "User_ID = ?, " +
                "Contact_ID = ? " +
                "WHERE Appointment_ID = ?"; // UPDATE statement

        // get current timestamp in utc time
        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp currentTimeStamp = Timestamp.valueOf(currentTime);

        // get user name
        ObservableList<User> currentUser = getCurrentUser();
        String userName = currentUser.get(0).getUserName();
        int userId = currentUser.get(0).getUserId();

        try {
            DBQuery.setPreparedStatement(JDBC.connection, preparedStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, start);
            ps.setTimestamp(6, end);
            ps.setTimestamp(7, currentTimeStamp);
            ps.setString(8, userName);
            ps.setInt(9, customerId);
            ps.setInt(10, userId);
            ps.setInt(11, contactId);
            ps.setInt(12, id);

            ps.execute();

            if (ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount() + " row(s) affected!");
            }
            else {
                System.out.println("No row(s) affected!");
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes an appointment in database
     * @param id
     */
    public static void deleteAppointmentDB(int id) {

        // create DELETE prepared statement
        String preparedStatement = "DELETE FROM appointments WHERE Appointment_ID = ?"; // DELETE statement

        try {
            DBQuery.setPreparedStatement(JDBC.connection, preparedStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            ps.setInt(1, id);

            ps.execute();

            if (ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount() + " row(s) affected!");
            }
            else {
                System.out.println("No row(s) affected!");
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
