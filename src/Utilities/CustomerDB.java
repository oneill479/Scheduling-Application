package Utilities;

/**
 * Class CustomerDB.java
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

import static Model.User.getCurrentUser;

public class CustomerDB {

    /**
     * Inserts a customer into database
     * @param id
     * @param name
     * @param address
     * @param postal
     * @param phone
     * @param division
     */
    public static void insertCustomer(int id, String name, String address, String postal, String phone, int division) {

        // create INSERT prepared statement
        String preparedStatement = "INSERT INTO customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                " VALUES(?,?,?,?,?,?,?,?,?,?)"; // INSERT statement

        // get current timestamp in utc time
        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp currentTimeStamp = Timestamp.valueOf(currentTime);

        // get user name
        ObservableList<User> currentUser = getCurrentUser();
        String userName = currentUser.get(0).getUserName();

        try {
            DBQuery.setPreparedStatement(JDBC.connection, preparedStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, postal);
            ps.setString(5, phone);
            ps.setTimestamp(6, currentTimeStamp);
            ps.setString(7, userName);
            ps.setTimestamp(8, currentTimeStamp);
            ps.setString(9, userName);
            ps.setInt(10, division);

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
     * Updates a customer in database
     * @param id
     * @param name
     * @param address
     * @param postal
     * @param phone
     * @param division
     */
    public static void updateCustomerDB(int id, String name, String address, String postal, String phone, int division) {

        // create UPDATE prepared statement
        String preparedStatement = "UPDATE appointments " +
                "SET Customer_Name = ?, " +
                "Address = ?, " +
                "Postal_Code = ?, " +
                "Phone = ?, " +
                "Last_Update = ?, " +
                "Last_Updated_By = ?, " +
                "Division_ID = ?, " +
                "WHERE Customer_ID = ?"; // UPDATE statement

        // get current timestamp in utc time
        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp currentTimeStamp = Timestamp.valueOf(currentTime);

        // get user name
        ObservableList<User> currentUser = getCurrentUser();
        String userName = currentUser.get(0).getUserName();

        try {
            DBQuery.setPreparedStatement(JDBC.connection, preparedStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postal);
            ps.setString(4, phone);
            ps.setTimestamp(5, currentTimeStamp);
            ps.setString(6, userName);
            ps.setInt(7, division);
            ps.setInt(8, id);

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

    public static void deleteCustomerDB(int id) {

        // create DELETE prepared statement
        String preparedStatement = "DELETE FROM customers WHERE Customer_ID = ?"; // DELETE statement

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
