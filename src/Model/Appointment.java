package Model;

/**
 * Class Appointment.java
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Caleb O'Neill
 */

public class Appointment {
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    public static void addNewAppointment(Appointment newAppointment) {
        allAppointments.add(newAppointment);
    };

    private int id, customerId, userId;
    private String title, description, location, contact, type, customerName;
    private Timestamp start, end;
    private LocalDateTime startLocal, endLocal;

    // constructor
    public Appointment(int id, String title, String description, String location, String contact, String type,
                       Timestamp start, Timestamp end, int customerId, int userId, String customerName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.start = start;
        this.end = end;
        this.customerName = customerName;

        // set local times for display in table
        startLocal = start.toLocalDateTime();
        endLocal = end.toLocalDateTime();

    }


    // GETTERS

    /**
     * @return appointment id
     */
    public int getId() { return id; }

    /**
     * @return appointment title
     */
    public String getTitle() { return title; }

    /**
     * @return appointment description
     */
    public String getDescription() { return description; }

    /**
     * @return appointment location
     */
    public String getLocation() { return location; }

    /**
     * @return appointment contact
     */
    public String getContact() { return contact; }

    /**
     * @return appointment type
     */
    public String getType() { return type; }

    /**
     * @return appointment start time and date
     */
    public Timestamp getStart() { return start; }

    /**
     * @return appointment end time and date
     */
    public Timestamp getEnd() { return end; }

    /**
     * @return appointment customer id
     */
    public int getCustomerId() { return customerId; }

    /**
     * @return appointment end time
     */
    public int getUserId() { return userId; }

    /**
     * @return customer name
     */
    public String getCustomerName() { return customerName; }

    /**
     * @return appointment customer id
     */
    public LocalDateTime getStartLocal() { return startLocal; }

    /**
     * @return appointment end time
     */
    public LocalDateTime getEndLocal() { return endLocal; }

    /**
     * @return startEST get eastern time conversion
     */
    public LocalDateTime getStartEST() {
        ZoneId currentTimeZone = ZoneId.systemDefault();
        ZoneId ESTZone = ZoneId.of("America/New_York");

        ZonedDateTime startDT = startLocal.atZone(currentTimeZone);
        LocalDateTime startEST = startDT.withZoneSameInstant(ESTZone).toLocalDateTime();

        return startEST;
    }

    /**
     * @return endEST get eastern time conversion
     */
    public LocalDateTime getEndEST() {
        ZoneId currentTimeZone = ZoneId.systemDefault();
        ZoneId ESTZone = ZoneId.of("America/New_York");

        ZonedDateTime endDT = endLocal.atZone(currentTimeZone);
        LocalDateTime endEST = endDT.withZoneSameInstant(ESTZone).toLocalDateTime();

        return endEST;
    }

    /**
     * This method gets a list of all the appointment data
     * @return returns an observable list of appointment data
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }


    // SETTERS

    /**
     * @param id appointment id
     */
    public void setId(int id) { this.id = id; }

    /**
     * @param title appointment title
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * @param description appointment description
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * @param location appointment location
     */
    public void setLocation(String location) { this.location = location; }

    /**
     * @param contact appointment contact
     */
    public void setContact(String contact) { this.contact = contact; }

    /**
     * @param type appointment type
     */
    public void setType(String type) { this.type = type; }

    /**
     * @param start appointment start date and time
     */
    public void setStartDate(Timestamp start) { this.start = start; }

    /**
     * @param end appointment end date and time
     */
    public void setStartTime(Timestamp end) { this.end = end; }

    /**
     * @param customerId appointment customer id
     */
    public void setCustomerId(int customerId) {  this.customerId = customerId; }

    /**
     * @param customerName customer name
     */
    public void setCustomerId(String customerName) {  this.customerName = customerName; }

    /**
     * @param userId appointment user id
     */
    public void setEndTime(int userId) { this.userId = userId; }


    // Special functions

    public static ObservableList<String> getHours() {
        List<String> hours = Arrays.asList("06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "00", "01", "02", "03", "04", "05", "06");
        ObservableList<String> hrs = FXCollections.observableList(hours);
        return hrs;
    }

    public static ObservableList<String> getMinutes() {
        List<String> minutes = Arrays.asList("00", "15", "30", "45", "00");
        ObservableList<String> mins = FXCollections.observableList(minutes);
        return mins;
    }

    public static ObservableList<String> getAmPm() {
        List<String> ampm = Arrays.asList("AM", "PM");
        ObservableList<String> choices = FXCollections.observableList(ampm);
        return choices;
    }

    /**
     * This method deletes an appointment
     * @param selectedAppointment the appointment that will be deleted
     * @return returns true if the appointment was successfully deleted, otherwise returns false
     */
    public static boolean deleteAppointment(Appointment selectedAppointment) {
        try {
            int selection = allAppointments.indexOf(selectedAppointment);
            allAppointments.remove(selection);
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * This method updates an appointment
     * @param index the index of the appointment
     * @param updatedAppointment the appointment that will be updated
     */
    public static void updateAppointment(int index, Appointment updatedAppointment) {
        allAppointments.set(index, updatedAppointment);
    }


}
