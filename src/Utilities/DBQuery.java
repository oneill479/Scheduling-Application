package Utilities;

import Model.*;
import javafx.collections.ObservableList;

import java.sql.*;

import static Model.Country.*;
import static Model.FirstLevelDivision.*;
import static Model.Customer.*;
import static Model.Contact.*;
import static Model.Appointment.*;

public class DBQuery {

    private static PreparedStatement statement;

    public static void setPreparedStatement(Connection connection, String sqlStatement) throws SQLException
    {
        statement = connection.prepareStatement(sqlStatement);
    }


    public static PreparedStatement getPreparedStatement()
    {
        return statement;
    }



    // QUERIES //

    public static void queryCountries() {

        // create SELECT prepared statement
        String preparedStatement = "SELECT * FROM countries"; // SELECT statement

        try {
            DBQuery.setPreparedStatement(JDBC.connection, preparedStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();

            // store results
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");

                Country country = new Country(countryId, countryName);

                addCountry(country);

            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void queryFirstLevelDivisions() {

        // create SELECT prepared statement
        String preparedStatement = "SELECT * FROM first_level_divisions"; // SELECT statement

        try {
            DBQuery.setPreparedStatement(JDBC.connection, preparedStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();

            // store results
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs.getInt("Country_ID");

                FirstLevelDivision fld = new FirstLevelDivision(divisionId, divisionName, countryId);

                addDivisions(fld);

            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void queryCustomers() {

        // create SELECT prepared statement
        String preparedStatement = "SELECT * FROM customers"; // SELECT statement

        try {
            DBQuery.setPreparedStatement(JDBC.connection, preparedStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();

            // store results
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                String divisionName = "", countryName = "";

                ObservableList<FirstLevelDivision> divisionList = getAllDivisions();
                ObservableList<Country> countryList = getAllCountries();

                // find first level division name
                for (FirstLevelDivision division : divisionList) {
                    if (divisionId == division.getId()) {
                        divisionName = division.getName();

                        // find first level division name
                        for (Country country : countryList) {
                            if (division.getCountryId() == country.getId()) {
                                countryName = country.getName();
                                break;
                            }
                        }
                        break;

                    }
                }

                Customer customer = new Customer(id, name, address, postalCode, phoneNumber, countryName, divisionName);

                addNewCustomer(customer);


            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void queryContacts() {

        // create SELECT prepared statement
        String preparedStatement = "SELECT * FROM contacts"; // SELECT statement

        try {
            DBQuery.setPreparedStatement(JDBC.connection, preparedStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();

            // store results
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");

                Contact contact = new Contact(contactId, contactName, contactEmail);

                addContact(contact);

            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void queryAppointments() {

        // create SELECT prepared statement
        String preparedStatement = "SELECT * FROM appointments"; // SELECT statement

        try {
            DBQuery.setPreparedStatement(JDBC.connection, preparedStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();

            // store results
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                String appointmentLocation = rs.getString("Location");
                String appointmentType = rs.getString("Type");
                Timestamp appointmentStart = rs.getTimestamp("Start");
                Timestamp appointmentEnd = rs.getTimestamp("End");
                int appointmentUserId = rs.getInt("User_ID");

                // get contact id's to find contact names
                int appointmentContactId = rs.getInt("Contact_ID");
                String appointmentContactName = "";

                ObservableList<Contact> contactList = getAllContacts();

                // find first level division name
                for (Contact contact : contactList) {
                    if (appointmentContactId == contact.getId()) {
                        appointmentContactName = contact.getName();
                        break;
                    }
                }

                // get customer id's to find customer names
                int appointmentCustomerId = rs.getInt("Customer_ID");
                String appointmentCustomerName = "";

                ObservableList<Customer> customerList = getAllCustomers();

                // find first level division name
                for (Customer customer : customerList) {
                    if (appointmentCustomerId == customer.getId()) {
                        appointmentCustomerName = customer.getName();
                        break;
                    }
                }

                Appointment appointment = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentLocation, appointmentContactName,
                        appointmentType, appointmentStart, appointmentEnd, appointmentCustomerId, appointmentUserId, appointmentCustomerName);

                addNewAppointment(appointment);

            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //private static Statement statement;

    // Create statement object
    /*
    public static void setStatement(Connection connection) throws SQLException
    {
        statement = connection.createStatement();
    }

    // Return statement object
    public static Statement getStatement()
    {
        return statement;
    }
     */


}
