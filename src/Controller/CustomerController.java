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

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import static Model.Customer.*;
import static Model.Country.*;
import static Model.FirstLevelDivision.*;

public class CustomerController implements Initializable {

    public Button cancelButton;
    private int id, postalCode, phoneNumber;
    private String name, address;
    private String country, firstLevelDivision;

    public TableView customerTable;
    public TableColumn customerId;
    public TableColumn customerName;
    public TableColumn customerAddress;
    public TableColumn customerPostalCode;
    public TableColumn customerPhoneNumber;
    public TableColumn customerCountry;
    public TableColumn customerFLD;

    public TextField fieldName;
    public TextField fieldAddress;
    public TextField fieldPostalCode;
    public TextField fieldPhoneNumber;
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

    public void addCustomer(ActionEvent actionEvent) {
    }

    public void editCustomer(ActionEvent actionEvent) {
    }

    public void deleteCustomer(ActionEvent actionEvent) {
    }

    public void cancel(ActionEvent actionEvent) {
    }
}
