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
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static Model.Appointment.*;
import static Model.Contact.*;
import static Model.Customer.*;
import static Model.User.getCurrentUser;
import static Utilities.AppointmentDB.*;

public class AppointmentController implements Initializable {

    public ComboBox fieldContact;
    public ComboBox fieldCustomer;
    public TextField fieldId;
    public TextField fieldTitle;
    public TextField fieldDescription;
    public TextField fieldLocation;
    public TextField fieldType;
    public DatePicker fieldDate;
    public Spinner startHour;
    public Spinner startMinute;
    public Spinner endHour;
    public Spinner endMinute;
    public Label customerIdLabel;
    public Label contactIdLabel;
    public Button addAppointmentButton;
    public Button cancelButton;
    public RadioButton weekRadioButton;
    public RadioButton monthRadioButton;
    public RadioButton allRadioButton;

    Appointment selectedAppointment;

    private int id, contactID, customerID, userId;
    private String contactName, customerName;
    private String title, description, location, type;
    private String sHr, sMin, eHr, eMin;
    private Timestamp startTimestamp, endTimestamp;
    private boolean update = false;
    private Date updateDate;

    public TableView appointmentTable;
    public TableColumn appointmentId;
    public TableColumn appointmentTitle;
    public TableColumn appointmentDescription;
    public TableColumn appointmentLocation;
    public TableColumn appointmentContact;
    public TableColumn appointmentType;
    public TableColumn appointmentStart;
    public TableColumn appointmentEnd;
    public TableColumn appointmentCustomer;
    public TableColumn appointmentUser;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList<String> contacts = FXCollections.observableArrayList();
    private ObservableList<String> customers = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // get user name
        ObservableList<User> currentUser = getCurrentUser();
        userId = currentUser.get(0).getUserId();

        id = getRandomAppointmentId();
        fieldId.setText(String.valueOf(id));

        appointmentTable.setItems(appointments);
        appointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("startLocal"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("endLocal"));
        appointmentCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentUser.setCellValueFactory(new PropertyValueFactory<>("userId"));

        ObservableList<Appointment> appointmentList = getAllAppointments();

        for (Appointment appointment : appointmentList) {
            appointments.add(appointment);
        }

        // populate contact field
        fieldContact.setItems(contacts);
        ObservableList<Contact> contactsList = getAllContacts();

        for (Contact contact : contactsList) {
            contacts.add(contact.getName());
        }

        // populate customer field
        fieldCustomer.setItems(customers);
        ObservableList<Customer> customerList = getAllCustomers();

        for (Customer customer : customerList) {
            customers.add(customer.getName());
        }

        fieldContact.setOnAction(e -> {

            for (Contact contact : contactsList) {
                if (fieldContact.getSelectionModel().getSelectedItem() == contact.getName()) {
                    contactName = contact.getName();
                    contactIdLabel.setText("ID-" + contact.getId());
                    contactID = contact.getId();
                    break;
                }
            }

            contactIdLabel.setVisible(true);
        });

        fieldCustomer.setOnAction(e -> {

            for (Customer customer : customerList) {
                if (fieldCustomer.getSelectionModel().getSelectedItem() == customer.getName()) {
                    customerName = customer.getName();
                    customerIdLabel.setText("ID-" + customer.getId());
                    customerID = customer.getId();
                    break;
                }
            }

            customerIdLabel.setVisible(true);
        });

        // set hours
        Spinner startH = new Spinner(getHours());
        Spinner endH = new Spinner(getHours());
        startHour.setValueFactory(startH.getValueFactory());
        endHour.setValueFactory(endH.getValueFactory());

        // set minutes
        Spinner startM = new Spinner(getMinutes());
        Spinner endM = new Spinner(getMinutes());
        startMinute.setValueFactory(startM.getValueFactory());
        endMinute.setValueFactory(endM.getValueFactory());

    }

    /**
     * This method generates a random customer id
     * @return Returns a generated customer id
     */
    public static int getRandomAppointmentId () {
        Random rand = new Random();
        int upperBound = 25 + getAllAppointments().size();
        int randomId = rand.nextInt(upperBound);

        for (Appointment appointment : getAllAppointments()) {
            if (randomId == appointment.getId() || randomId == 0) {
                return getRandomAppointmentId();
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

    public void addAppointment(ActionEvent actionEvent) throws IOException, ParseException {
        StringBuilder error = new StringBuilder();
        String textError = checkTextInputs(fieldContact, fieldTitle, fieldCustomer, fieldDescription, fieldLocation, fieldType);

        sHr = startHour.getValue().toString();
        sMin = startMinute.getValue().toString();
        eHr = endHour.getValue().toString();
        eMin = endMinute.getValue().toString();

        String timeError = checkTimeInputs(sHr, sMin, eHr, eMin);

        if (textError.isEmpty() && timeError.isEmpty()) {

            title = fieldTitle.getText();
            description = fieldDescription.getText();
            location = fieldLocation.getText();
            type = fieldType.getText();

            if (update) {
                Appointment updateAppointment = new Appointment(Integer.parseInt(fieldId.getText()), title, description, location, contactName, type, startTimestamp, endTimestamp, customerID, userId, customerName);
                updateAppointment(getAllAppointments().indexOf(selectedAppointment), updateAppointment);
            }
            else {
                Appointment newAppointment = new Appointment(id, title, description, location, contactName, type, startTimestamp, endTimestamp, customerID, userId, customerName);
                addNewAppointment(newAppointment);
            }

            if (update) updateAppointmentDB(Integer.parseInt(fieldId.getText()), title, description, location, type, startTimestamp, endTimestamp, customerID, contactID);
            else insertAppointmentDB(id, title, description, location, type, startTimestamp, endTimestamp, customerID, contactID);

            // show success message
            if (update) {
                JOptionPane.showMessageDialog(null, "Appointment successfully updated!", "Appointments", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null, "Appointment successfully added!", "Appointments", JOptionPane.INFORMATION_MESSAGE);
            }
            // refresh the page
            refreshPage(actionEvent);


        }
        else {
            error.append(textError);
            error.append(timeError);
            // show error string to user
            JOptionPane.showMessageDialog(null, error.toString(), "Appointments", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void updateSelectedAppointment(ActionEvent actionEvent) {

        selectedAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        if(selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Update Appointment");
            alert.setContentText("You must select an appointment!");
            alert.showAndWait();
            selectedAppointment = null;
            return;
        };

        update = true;
        cancelButton.setDisable(false);
        addAppointmentButton.setText("Update Appointment");

        selectedAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();

        fieldContact.getSelectionModel().select(selectedAppointment.getContact());
        fieldId.setText(String.valueOf(selectedAppointment.getId()));
        fieldTitle.setText(selectedAppointment.getTitle());
        fieldCustomer.getSelectionModel().select(selectedAppointment.getCustomerName());
        fieldDescription.setText(selectedAppointment.getDescription());
        fieldLocation.setText(selectedAppointment.getLocation());
        fieldType.setText(selectedAppointment.getType());
        fieldDate.setValue(selectedAppointment.getStart().toLocalDateTime().toLocalDate());

        String sHour = selectedAppointment.getStartLocal().toLocalTime().toString().substring(0, 2);
        String sMinute = selectedAppointment.getStartLocal().toLocalTime().toString().substring(3, 5);
        String eHour = selectedAppointment.getEndLocal().toLocalTime().toString().substring(0, 2);
        String eMinute = selectedAppointment.getEndLocal().toLocalTime().toString().substring(3, 5);

        startHour.getValueFactory().setValue(sHour);
        startMinute.getValueFactory().setValue(sMinute);
        endHour.getValueFactory().setValue(eHour);
        endMinute.getValueFactory().setValue(eMinute);

    }

    public void deleteAppointment(ActionEvent actionEvent) {

        selectedAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        if(selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete Appointment");
            alert.setContentText("You must select an appointment!");
            alert.showAndWait();
            return;
        };

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel this appointment?");
        Optional<ButtonType> result = alert.showAndWait();

        String aptId = String.valueOf(selectedAppointment.getId());
        String aptType = selectedAppointment.getType();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteAppointmentDB(selectedAppointment.getId());
            Model.Appointment.deleteAppointment(selectedAppointment);
            appointments.remove(selectedAppointment);
            appointmentTable.setItems(getAllAppointments());

            JOptionPane.showMessageDialog(null, "Appointment successfully cancelled!\n" +
                    "Appointment ID - " + aptId + "\nAppointment Type - " + aptType, "Customers", JOptionPane.INFORMATION_MESSAGE);
        }



        // set selected appointment back to null
        selectedAppointment = null;
    }

    public void allView(ActionEvent actionEvent) {

    }

    public void weekView(ActionEvent actionEvent) {
        //appointmentStart.setCellValueFactory(cellData -> cellData.getValue());
    }

    public void monthView(ActionEvent actionEvent) {
    }

    public void cancelUpdate(ActionEvent actionEvent) throws IOException {
        refreshPage(actionEvent);
    }

    public void refreshPage (ActionEvent actionEvent) throws IOException {
        // reset appointment screen
        Parent root = FXMLLoader.load(getClass().getResource("../View/AppointmentScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public static String checkTextInputs(ComboBox contact, TextField title, ComboBox customer, TextField description, TextField location, TextField type) {
        StringBuilder errorBuild = new StringBuilder();
        String error;

        // contact check
        if (contact.getSelectionModel().isEmpty()) { errorBuild.append("You must select a contact!\n"); }
        // title check
        if (title.getText().isEmpty()) { errorBuild.append("Title cannot be empty!\n"); }
        // customer name check
        if (customer.getSelectionModel().isEmpty()) { errorBuild.append("You must select a contact!\n"); }
        // description check
        if (description.getText().isEmpty()) errorBuild.append("Description cannot be empty!\n");
        // location check
        if (location.getText().isEmpty()) errorBuild.append("Location cannot be empty!\n");
        // type check
        if (type.getText().isEmpty()) errorBuild.append("Type cannot be empty!\n");

        error = errorBuild.toString();
        return error;

    }

    public String checkTimeInputs(String startHour, String startMinute, String endHour,
                                         String endMinute) throws ParseException {

        StringBuilder errorBuild = new StringBuilder();
        StringBuilder start = new StringBuilder();
        StringBuilder end = new StringBuilder();
        String date, startTime, endTime, error;
        String aptEarly, aptLate;

        // set up strings for dates and times
        try {
            date = fieldDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        catch (Exception e) {
            errorBuild.append("Please select a date from calendar!\n");
            error = errorBuild.toString();
            return error;
        }

        startTime = " " + startHour + ":" + startMinute + ":00";
        endTime = " " + endHour + ":" + endMinute + ":00";

        aptEarly = date + " 08:00:00";
        aptLate = date + " 20:00:00";

        // append dates and times to string builders
        start.append(date);
        start.append(startTime);
        end.append(date);
        end.append(endTime);

        // create local date time objects
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);
        LocalDateTime earlyDateTime = LocalDateTime.parse(aptEarly, formatter);
        LocalDateTime lateDateTime = LocalDateTime.parse(aptLate, formatter);

        // get time zones
        ZoneId currentZone = ZoneId.systemDefault();
        ZoneId ESTZone = ZoneId.of("America/New_York");

        ZonedDateTime currentStart = startDateTime.atZone(currentZone);
        LocalDateTime startEST = currentStart.withZoneSameInstant(ESTZone).toLocalDateTime();

        ZonedDateTime currentEnd = endDateTime.atZone(currentZone);
        LocalDateTime endEST = currentEnd.withZoneSameInstant(ESTZone).toLocalDateTime();

        ZonedDateTime earlyLocal = earlyDateTime.atZone(ESTZone);
        LocalTime earlyTimeLocal = earlyLocal.withZoneSameInstant(currentZone).toLocalTime();
        ZonedDateTime lateLocal = lateDateTime.atZone(ESTZone);
        LocalTime lateTimeLocal = lateLocal.withZoneSameInstant(currentZone).toLocalTime();

        // look for errors
        int compareErr = 0;
        // get day of week
        String dayOfWeek = startDateTime.getDayOfWeek().toString();

        if (dayOfWeek.equalsIgnoreCase("SATURDAY") || dayOfWeek.equalsIgnoreCase("SUNDAY")) {
            { errorBuild.append("Appointment day cannot be on a weekend!\n"); compareErr++;}
        }
        else if (startDateTime.isBefore(LocalDateTime.now())) { errorBuild.append("Appointment date and time cannot be before current date and time!\n"); compareErr++;}
        else if (startDateTime.isEqual(endDateTime)) { errorBuild.append("Appointment end time must be after start time!\n"); compareErr++;}
        else if (startDateTime.isAfter(endDateTime)) { errorBuild.append("Appointment end time cannot be before start time!\n"); compareErr++;}

        if (compareErr == 0) {
            if (startEST.isBefore(earlyDateTime)) {
                errorBuild.append("Appointment times cannot be before 08:00 EST!\n(" + earlyTimeLocal + " Local Time)\n\n");
            }
            if (startEST.isAfter(lateDateTime)) {
                errorBuild.append("Appointment times cannot be after 20:00 EST!\n(" + lateTimeLocal + " Local Time)\n\n");
            }
            else if (endEST.isAfter(lateDateTime)) {
                errorBuild.append("Appointment times cannot be after 20:00 EST!\n(" + lateTimeLocal + " Local Time)\n\n");
            }

        }

        ObservableList<Appointment> appointmentList = getAllAppointments();
        String overlap = "Appointment times cannot overlap with the same customer!\n";

        for (Appointment appointment : appointmentList) {

            // if you are updating an appointment skip this one
            if (selectedAppointment != null) {
                if (appointment.getId() == selectedAppointment.getId()) continue;
            }

            // check to see if customers appointments overlap
            if (customerID == appointment.getCustomerId()) {

                // check if start time is the same
                if (startEST.isEqual(appointment.getStartEST())) {
                    errorBuild.append(overlap);
                    break;
                }
                // check if end time is the same
                if (endEST.isEqual(appointment.getEndEST())) {
                    errorBuild.append(overlap);
                    break;
                }
                if (startEST.isBefore(appointment.getStartEST())) {
                    // check if time is before and ends during another appointment
                    if (endEST.isAfter(appointment.getStartEST()) && endEST.isBefore(appointment.getEndEST())) {
                        errorBuild.append(overlap);
                        break;
                    }
                    //check if time starts before and ends after another appointment
                    if (endEST.isAfter(appointment.getEndEST())) {
                        errorBuild.append(overlap);
                        break;
                    }
                }
                if (startEST.isAfter(appointment.getStartEST()) && startEST.isBefore(appointment.getEndEST())) {
                    errorBuild.append(overlap);
                    System.out.println(4);
                    break;
                }

            }

        }

        startTimestamp = Timestamp.valueOf(startDateTime);
        endTimestamp = Timestamp.valueOf(endDateTime);

        error = errorBuild.toString();
        return error;
    }

}
