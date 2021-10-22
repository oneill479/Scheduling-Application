package Controller;

/**
 * Class ReportController.java
 */

/**
 *
 * @author Caleb O'Neill
 */

import Model.Appointment;
import Model.Contact;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static Model.Appointment.getAllAppointments;
import static Model.Contact.getAllContacts;
import static Model.Customer.getAllCustomers;

public class ReportController implements Initializable {
    public TableView contactTable;
    public TableColumn aptIdColumn;
    public TableColumn titleColumn;
    public TableColumn typeColumn;
    public TableColumn descriptionColumn;
    public TableColumn startColumn;
    public TableColumn endColumn;
    public TableColumn customerIdColumn;

    public ChoiceBox typeBox;
    public ChoiceBox monthBox;
    public ChoiceBox contactBox;
    public TextField typeField;
    public TextField monthField;
    public TextField totalCustomersField;
    public TextField totalAppointmentsField;
    public PieChart countryPieChart;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList<String> contacts = FXCollections.observableArrayList();
    private ObservableList<String> types = FXCollections.observableArrayList();
    private ObservableList<String> months = FXCollections.observableArrayList();


    /**
     * Initializes reports screen with all of the data
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        contactTable.setItems(appointments);
        aptIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startLocal"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endLocal"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        ObservableList<Appointment> appointmentList = getAllAppointments();
        ObservableList<Customer> customerList = getAllCustomers();

        // populate contact box
        contactBox.setItems(contacts);
        for (Contact contact : getAllContacts()) {
            contacts.add(contact.getName());
        }

        // populate type box
        typeBox.setItems(types);
        for (Appointment appointment : appointmentList) {
            types.add(appointment.getType());
        }

        // populate month box
        monthBox.setItems(months);
        months.addAll("January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December");

        // contact selection
        contactBox.setOnAction(e -> {
            // clear table
            appointments.clear();

            for (Appointment appointment : appointmentList) {
                if (contactBox.getSelectionModel().getSelectedItem().toString() == appointment.getContact()) {
                    appointments.add(appointment);
                }
            }

        });

        // type selection
        typeBox.setOnAction(e -> {
            // clear type result box
            typeField.clear();
            int typeCount = 0;

            for (Appointment appointment : appointmentList) {
                if (typeBox.getSelectionModel().getSelectedItem().toString() == appointment.getType()) {
                    typeCount++;
                }
            }

            typeField.setText(String.valueOf(typeCount));

        });

        // month selection
        monthBox.setOnAction(e -> {
            // clear type result box
            monthField.clear();
            int monthCount = 0;

            for (Appointment appointment : appointmentList) {
                if (appointment.getStartLocal().getMonth().toString().equalsIgnoreCase(monthBox.getSelectionModel().getSelectedItem().toString())) {
                    monthCount++;
                }
            }

            monthField.setText(String.valueOf(monthCount));

        });

        // total customers
        totalCustomersField.setText(String.valueOf(customerList.size()));
        // total appointments
        totalAppointmentsField.setText(String.valueOf(appointmentList.size()));

        int usa = 0, uk = 0, canada = 0;

        for (Customer customer : customerList) {
            String country = customer.getCountry();
            System.out.println(country);
            if (country.equalsIgnoreCase("U.S")) {usa++;}
            else if (country.equalsIgnoreCase("UK")) {uk++;}
            else if (country.equalsIgnoreCase("Canada")) {canada++;}
        }

        // fill pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("USA-"+usa, usa),
                new PieChart.Data("UK-"+uk, uk),
                new PieChart.Data("CANADA-"+canada, canada)
        );

        countryPieChart.setTitle("Customers By Country");
        countryPieChart.setData(pieChartData);
        countryPieChart.setStyle("-fx-font-size: 8px; -fx-font-weight: bold;");
        countryPieChart.setClockwise(true);
        countryPieChart.setLabelLineLength(5);
        countryPieChart.setLabelsVisible(true);
        countryPieChart.setStartAngle(180);

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
}
