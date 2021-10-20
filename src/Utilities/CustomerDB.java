package Utilities;

import Model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static Model.Country.addCountry;

public class CustomerDB {

    public static void insertCustomer(int id, String name, String address, String postal, String phone, int division) {

        // create INSERT prepared statement
        String preparedStatement = "INSERT INTO customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID)" +
                " VALUES(?,?,?,?,?,?,?,?)"; // INSERT statement

        try {
            DBQuery.setPreparedStatement(JDBC.connection, preparedStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, postal);
            ps.setString(5, phone);
            ps.setInt(6, division);

            ps.execute();

            if (ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount());
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
