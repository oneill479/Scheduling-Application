package Model;

/**
 * Class Customer.java
 */

import Utilities.DBQuery;
import Utilities.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Caleb O'Neill
 */

public class Customer {

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    public static void addNewCustomer(Customer newCustomer) {
        allCustomers.add(newCustomer);
    };

    private int id;
    private String name, address, postalCode, phoneNumber;
    private String country, firstLevelDivision;

    // constructor
    public Customer(int id, String name, String address, String postalCode, String phoneNumber, String country, String firstLevelDivision) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.firstLevelDivision = firstLevelDivision;
    }


    // GETTERS

    /**
     * @return customer id
     */
    public int getId() { return id; }

    /**
     * @return customer name
     */
    public String getName() { return name; }

    /**
     * @return customer address
     */
    public String getAddress() { return address; }

    /**
     * @return customer postal code
     */
    public String getPostalCode() { return postalCode; }

    /**
     * @return customer phoneNumber
     */
    public String getPhoneNumber() { return phoneNumber; }

    /**
     * @return customer country
     */
    public String getCountry() { return country; }

    /**
     * @return customer first level division
     */
    public String getFirstLevelDivision() { return firstLevelDivision; }

    /**
     * This method gets a list of all the customer data
     * @return returns an observable list of customer data
     */
    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }


    // SETTERS

    /**
     * @param id customer id
     */
    public void setId(int id) { this.id = id; }

    /**
     * @param name customer name
     */
    public void setName(String name) { this.name = name; }

    /**
     * @param address customer address
     */
    public void setAddress(String address) { this.address = address; }

    /**
     * @param postalCode customer postal code
     */
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    /**
     * @param phoneNumber customer phone number
     */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /**
     * @param country customer country
     */
    public void setCountry(String country) { this.country = country; }

    /**
     * @param firstLevelDivision customer first level division
     */
    public void setFirstLevelDivision(String firstLevelDivision) { this.firstLevelDivision = firstLevelDivision; }


    // SPECIAL FUNCTIONS //

    /**
     * This method updates a customer
     * @param index the index of the customer
     * @param updatedCustomer the customer that will be updated
     */
    public static void updateCustomer(int index, Customer updatedCustomer) {
        allCustomers.set(index, updatedCustomer);
    }

    /**
     * This method deletes a customer
     * @param selectedCustomer the customer that will be deleted
     * @return returns true if the customer was successfully deleted, otherwise returns false
     */
    public static boolean deleteCustomer(Customer selectedCustomer) {
        try {
            int selection = allCustomers.indexOf(selectedCustomer);
            allCustomers.remove(selection);
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

}
