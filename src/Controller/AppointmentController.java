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

public class AppointmentController implements Initializable {

    public ComboBox fieldContact;
    public ComboBox fieldCustomer;
    public TextField fieldId;
    public TextField fieldTitle;
    public TextField fieldDescription;
    public TextField fieldLocation;
    public TextField fieldType;
    public DatePicker fieldDate;
    public Button cancelButton;
    public Spinner startHour;
    public Spinner startMinute;
    public Spinner endHour;
    public Spinner endMinute;
    public Label contactIdLabel;

    private int id, customer, user;
    private String title, description, location, type;
    private String sHr, sMin, eHr, eMin;
    private Timestamp startDateTime, endDateTime;

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

        fieldCustomer.setOnAction(e -> {

            for (Customer customer : customerList) {
                if (fieldCustomer.getSelectionModel().getSelectedItem() == customer.getName()) {
                    contactIdLabel.setText("ID-" + customer.getId());
                    break;
                }
            }

            contactIdLabel.setVisible(true);
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

        // am and pm
       // startAmPm.setItems(getAmPm());
        //startAmPm.
       // endAmPm.setItems(getAmPm());

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
        String textError = checkTextInputs(fieldContact, fieldTitle, fieldDescription, fieldLocation, fieldType);

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

            // after save go back to main screen
            toMain(actionEvent);

        }
        else {
            error.append(textError);
            error.append(timeError);
            // show error string to user
            JOptionPane.showMessageDialog(null, error.toString());
        }

    }

    public void updateAppointment(ActionEvent actionEvent) {
    }

    public void deleteAppointment(ActionEvent actionEvent) {
    }

    public void weekView(ActionEvent actionEvent) {
    }

    public void monthView(ActionEvent actionEvent) {
    }

    public void cancelUpdate(ActionEvent actionEvent) {
    }

    public static String checkTextInputs(ComboBox contact, TextField title, TextField description, TextField location, TextField type) {
        StringBuilder errorBuild = new StringBuilder();
        String error;

        // title check
        if (contact.getSelectionModel().isEmpty()) { errorBuild.append("You must select a contact!\n"); }
        // title check
        if (title.getText().isEmpty()) { errorBuild.append("Title cannot be empty!\n"); }
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

//        // set date formats and convert standard time to military time
//        SimpleDateFormat military = new SimpleDateFormat("HH:mm");
//        SimpleDateFormat standard = new SimpleDateFormat("hh:mm a");
//        Date startParsed = standard.parse(startHour + ":" + startMinute + " " + startAmPm);
//        Date endParsed = standard.parse(endHour + ":" + endMinute + " " + endAmPm);
//        String startMil = military.format(startParsed);
//        String endMil = military.format(endParsed);

        // set up strings for dates and times
        date = fieldDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
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
        ZoneId UTCZone = ZoneOffset.UTC;

        ZonedDateTime currentStart = startDateTime.atZone(currentZone);
        LocalDateTime startEST = currentStart.withZoneSameInstant(ESTZone).toLocalDateTime();
        LocalDateTime startUTC = currentStart.withZoneSameInstant(UTCZone).toLocalDateTime();

        ZonedDateTime currentEnd = endDateTime.atZone(currentZone);
        LocalDateTime endEST = currentEnd.withZoneSameInstant(ESTZone).toLocalDateTime();
        LocalDateTime endUTC = currentEnd.withZoneSameInstant(UTCZone).toLocalDateTime();

        ZonedDateTime earlyLocal = earlyDateTime.atZone(ESTZone);
        LocalTime earlyTimeLocal = earlyLocal.withZoneSameInstant(currentZone).toLocalTime();
        ZonedDateTime lateLocal = lateDateTime.atZone(ESTZone);
        LocalTime lateTimeLocal = lateLocal.withZoneSameInstant(currentZone).toLocalTime();

        // look for errors
        int compareErr = 0;
        if (startDateTime.isBefore(LocalDateTime.now())) { errorBuild.append("Appointment date and time cannot be before current date and time!\n"); compareErr++;}
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
        String overlap = "Appointment times cannot overlap current appointments!\n";

        for (Appointment appointment : appointmentList) {
            if (startEST.isBefore(appointment.getStartEST())) {
                if (endEST.isAfter(appointment.getStartEST()) && endEST.isBefore(appointment.getEndEST())) {
                    errorBuild.append(overlap);
                    break;
                }
                if (endEST.isAfter(appointment.getEndEST())) {
                    errorBuild.append(overlap);
                    break;
                }
            }
            else if (startEST.isAfter(appointment.getStartEST()) && startEST.isBefore(appointment.getEndEST())) {
                errorBuild.append(overlap);
                break;
            }

        }

        error = errorBuild.toString();
        return error;
    }

}
