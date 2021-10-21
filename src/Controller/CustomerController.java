package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Random;
import java.util.ResourceBundle;

import static Model.Appointment.*;
import static Model.Customer.*;
import static Model.Country.*;
import static Model.FirstLevelDivision.*;
import static Utilities.AppointmentDB.insertAppointmentDB;
import static Utilities.AppointmentDB.updateAppointmentDB;

public class CustomerController implements Initializable {

    public Button cancelButton;
    public Button addCustomerButton;
    private int id, postalCode, phoneNumber;
    private String name, address;
    private String country, firstLevelDivision;
    boolean update;

    Customer selectedCustomer;

    public TableView customerTable;
    public TableColumn customerId;
    public TableColumn customerName;
    public TableColumn customerAddress;
    public TableColumn customerPostalCode;
    public TableColumn customerPhoneNumber;
    public TableColumn customerCountry;
    public TableColumn customerFLD;

    public TextField fieldNameFirst;
    public TextField fieldNameLast;
    public TextField fieldAddressNum;
    public TextField fieldAddressStreet;
    public TextField fieldPhone;
    public TextField fieldPostalCode;
    public TextField fieldId;
    public ComboBox fieldCountry;
    public ComboBox fieldFLD;

    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<String> countries = FXCollections.observableArrayList();
    private ObservableList<String> divisions = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        id = getRandomCustomerId();
        fieldId.setText(String.valueOf(id));

        customerTable.setItems(customers);
        customerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerFLD.setCellValueFactory(new PropertyValueFactory<>("firstLevelDivision"));

        for (Customer customer : getAllCustomers()) {
            customers.add(customer);
        }

        // populate country field
        fieldCountry.setItems(countries);

        for (Country country : getAllCountries()) {
            countries.add(country.getName());
        }

        // populate first level divisions field
        fieldFLD.setItems(divisions);

        fieldCountry.setOnAction(e -> {
            // reconfigure button
            fieldFLD.setDisable(false);
            fieldFLD.setPromptText("");
            // clear divisions
            divisions.clear();

            String country = (String) fieldCountry.getSelectionModel().getSelectedItem();
            int countryId = 0;

            // get the country id
            for (Country selectedCountry : getAllCountries()) {
                if (country == selectedCountry.getName()) {
                    countryId = selectedCountry.getId();
                    System.out.println(countryId);
                }
            }

            // if country id matches add first level division
            for (FirstLevelDivision division : getAllDivisions()) {
                if (division.getCountryId() == countryId)
                    divisions.add(division.getName());
            }
        });

    }

    /**
     * This method generates a random customer id
     * @return Returns a generated customer id
     */
    public static int getRandomCustomerId () {
        Random rand = new Random();
        int upperBound = 25 + getAllCustomers().size();
        int randomId = rand.nextInt(upperBound);

        for (Customer customer : getAllCustomers()) {
            if (randomId == customer.getId() || randomId == 0) {
                return getRandomCustomerId();
            }
        }
        return randomId;
    }

    public void addCustomer(ActionEvent actionEvent) throws IOException, ParseException {
        StringBuilder error = new StringBuilder();
        String textError = checkInputs(fieldAddressNum, fieldAddressStreet, fieldPhone, fieldNameFirst, fieldNameLast,
                fieldPostalCode, fieldCountry, fieldFLD);

        if (textError.isEmpty()) {



//            if (update) {
//                Appointment updateAppointment = new Appointment(Integer.parseInt(fieldId.getText()), title, description, location, contactName, type, startTimestamp, endTimestamp, customerID, userId, customerName);
//                updateAppointment(getAllAppointments().indexOf(selectedAppointment), updateAppointment);
//            }
//            else {
//                Appointment newAppointment = new Appointment(id, title, description, location, contactName, type, startTimestamp, endTimestamp, customerID, userId, customerName);
//                addNewAppointment(newAppointment);
//            }
//
//            if (update) updateAppointmentDB(Integer.parseInt(fieldId.getText()), title, description, location, type, startTimestamp, endTimestamp, customerID, contactID);
//            else insertAppointmentDB(id, title, description, location, type, startTimestamp, endTimestamp, customerID, contactID);
//
//            // show success message
//            if (update) {
//                JOptionPane.showMessageDialog(null, "Appointment successfully updated!", "Appointments", JOptionPane.INFORMATION_MESSAGE);
//            }
//            else {
//                JOptionPane.showMessageDialog(null, "Appointment successfully added!", "Appointments", JOptionPane.INFORMATION_MESSAGE);
//            }
//            // refresh the page
//            refreshPage(actionEvent);


        }
        else {
            error.append(textError);
            // show error string to user
            JOptionPane.showMessageDialog(null, error.toString(), "Appointments", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void updateSelectedCustomer(ActionEvent actionEvent) {
        update = true;
        cancelButton.setDisable(false);
        addCustomerButton.setText("Update Customer");

        selectedCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();

        String [] address = selectedCustomer.getAddress().split(" ", 0);
        String [] name = selectedCustomer.getName().split(" ", 0);

        System.out.println(address[0]);
        System.out.println(address[1]);
        System.out.println(name[0]);
        System.out.println(name[1]);

        String addressNum = address[0];
        String addressName = address [1];
        String phoneNumber = String.valueOf(selectedCustomer.getPhoneNumber());
        String firstName = name[0];
        String lastName = name[1];
        String postalCode = String.valueOf(selectedCustomer.getPostalCode());

        fieldAddressNum.setText(addressNum);
        fieldAddressStreet.setText(addressName);
        fieldPhone.setText(phoneNumber);
        fieldNameFirst.setText(firstName);
        fieldNameLast.setText(lastName);
        fieldPostalCode.setText(postalCode);
        fieldCountry.getSelectionModel().select(selectedCustomer.getCountry());
        fieldFLD.getSelectionModel().select(selectedCustomer.getFirstLevelDivision());

    }

    public void cancelUpdate(ActionEvent actionEvent) throws IOException {
        refreshPage(actionEvent);
    }

    /**
     * This method takes the user to the main menu
     * @param actionEvent Save or cancel button being pressed
     * @throws IOException Checks to see if the main screen will load correctly
     */
    public void toMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainMenuScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public void deleteSelectedCustomer(ActionEvent actionEvent) {
    }

    public void refreshPage (ActionEvent actionEvent) throws IOException {
        // reset customer screen
        Parent root = FXMLLoader.load(getClass().getResource("../View/CustomerScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    /**
     * This method checks to see if the string input has only letters or spaces
     * @param text String to be tested for characters
     * @return returns true if there are only characters and spaces, otherwise returns false
     */
    public static boolean isLetters(String text) {
        String trimmed = text.replaceAll("\\s+","");
        char[] textArray = trimmed.toCharArray();

        for (char index : textArray) {
            if(!Character.isLetter(index)) return false;
        }
        return true;
    }

    /**
     * This method checks to see if the string input has only digits
     * @param text String to be tested for digits
     * @return returns true if there are only digits, otherwise returns false
     */
    public static boolean isInteger(String text) {
        int num;

        try {
            num = Integer.parseInt(text);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }

    }

    /**
     * This method checks to see if the string input is a valid postal code
     * @param text String to be tested
     * @return returns true if there are only digits and letters, otherwise returns false
     */
    public static boolean isPostalCode(String text) {

        try {
            char[] textArray = text.toCharArray();
            for (char index : textArray) {
                if(!Character.isLetterOrDigit(index)) return false;
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }

    }

    public static String checkInputs(TextField addressNum, TextField addressStreet, TextField phoneNumber, TextField nameFirst, TextField nameLast, TextField postalCode, ComboBox country, ComboBox division) {
        StringBuilder errorBuild = new StringBuilder();
        String error;

        // address number check
        if (addressNum.getText().isEmpty()) { errorBuild.append("Address number cannot be empty!\n"); }
        else if(isInteger(addressNum.getText())) { errorBuild.append("Address number must be a number!\n"); }
        // address number check
        if (addressStreet.getText().isEmpty()) { errorBuild.append("Address name cannot be empty!\n"); }
        else if (isLetters(addressStreet.getText())) { errorBuild.append("Address must be only letters!\n"); }
        // phone number check
        if (phoneNumber.getText().isEmpty()) { errorBuild.append("Phone Number cannot be empty!\n"); }
        if (isInteger(phoneNumber.getText().replaceAll("[\\s\\-()]", ""))) { errorBuild.append("Phone Number must only be numbers and dashes!\n"); }
        // customer first name check
        if (nameFirst.getText().isEmpty()) errorBuild.append("Name cannot be empty!\n");
        else if (isLetters(nameFirst.getText())) { errorBuild.append("First Name must be only letters!\n"); }
        // customer last name check
        if (nameLast.getText().isEmpty()) errorBuild.append("Name cannot be empty!\n");
        else if (isLetters(nameLast.getText())) { errorBuild.append("Last Name must be only letters!\n"); }
        // location check
        if (postalCode.getText().isEmpty()) errorBuild.append("Postal Code cannot be empty!\n");
        else if (isPostalCode(postalCode.getText())) { errorBuild.append("Postal Code can only be letters and numbers!\n"); }

        // country check
        if (country.getSelectionModel().isEmpty()) { errorBuild.append("You must select a Country!\n"); }
        // first level division check
        else if (division.getSelectionModel().isEmpty()) { errorBuild.append("You must select a First-level Division!\n"); }

        error = errorBuild.toString();
        return error;

    }
}
