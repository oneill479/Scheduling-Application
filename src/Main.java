/**
 * Class Main.java
 */

/**
 *
 * @author Caleb O'Neill
 */

import Utilities.JDBC;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

import static Utilities.DBQuery.*;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        JDBC.openConnection();

        // load in data from database
        queryCountries();
        queryFirstLevelDivisions();
        queryCustomers();
        queryContacts();
        queryAppointments();


        Parent root = FXMLLoader.load(getClass().getResource("View/LoginScreen.fxml"));
        primaryStage.setTitle("Scheduling Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {

        launch(args);

        String insertStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdateBy) VALUES(?,?,?,?)";
        String updateStatement = "UPDATE country SET country = ?, createdBy = ?, WHERE country = ?";

        //DBQuery.setPreparedStatement(JDBC.connection, insertStatement); // Create Prepared Statement

        //PreparedStatement ps = DBQuery.getPreparedStatement();

        String countryName;
        String createDate = "2020-03-28 00:00:00";
        String createdBy = "admin";
        String lastUpdateBy = "admin";

        //ps.setString(0, createDate);

       // ps.execute();
       // if (ps.getUpdateCount() > 0) {

       // }

//        DBQuery.setStatement(JDBC.connection);
//        // get statement reference
//        Statement statement = DBQuery.getStatement();


        // SQL insert statement
        //String insertStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdateBy) VALUES('US'), '2021-10-05 00:00:00', 'admin', 'admin' ";


        // SQL update statement
        //String updateStatement = "UPDATE country SET country = 'Japan' WHERE country = 'Canada'";

        // SQL delete statement
        //String deleteStatement = "DELETE FROM country WHERE country = 'Japan'";

        // execute SQL statement
        //statement.execute(insertStatement);

        //String selectStatement = "SELECT * FROM countries"; // SELECT statement

        /*
        if (selectStatement.contains("'")) {
            selectStatement = selectStatement.replace("'", "\\'");
        }
        */


//        try {
//
//            statement.execute(selectStatement);
//            ResultSet rs = statement.getResultSet(); // Get ResultSet
//
//            if(statement.getUpdateCount() > 0)
//                System.out.println(statement.getUpdateCount() + "row(s) affected!");
//            else
//                System.out.println("No change!");
//
//            // Forward Scroll ResultSet
//            while (rs.next()) {
//                int countryId = rs.getInt("Country_ID");
//                String countryName = rs.getString("country");
//                LocalDate date = rs.getDate("createDate").toLocalDate();
//                LocalTime time = rs.getTime("createDate").toLocalTime();
//                String createdBy = rs.getString("createdBy");
//                LocalDateTime lastUpdate = rs.getTimestamp("lastUpdate").toLocalDateTime();
//
//                // Display record
//                String p = " | ";
//                System.out.println(countryId + p + countryName + p + date + " " + time + p + createdBy + p + lastUpdate);
//            }
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//        }





        /*
        // confirm rows affected
        if (statement.getUpdateCount() > 0)
            System.out.println(statement.getUpdateCount() +  "row(s) affected!");
        else
            System.out.println("No change!");


        ResourceBundle rb = ResourceBundle.getBundle("Resources/user", Locale.getDefault());

        if (Locale.getDefault().equals(new Locale("fr").getLanguage())) {
            System.out.println(rb.getString("hello") + " " + rb.getString("world") );

        }
        
         */

        JDBC.closeConnection();

    }
}
