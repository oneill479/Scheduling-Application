package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contact {

    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    public static void addContact(Contact newContact) {
        allContacts.add(newContact);
    };

    private int id;
    private String name, email;

    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    // GETTERS

    /**
     * @return contact id
     */
    public int getId() { return id; }

    /**
     * @return contact name
     */
    public String getName() { return name; }

    /**
     * @return contact email
     */
    public String getEmail() { return email; }

    /**
     * This method gets a list of all the contacts
     * @return returns an observable list of contacts
     */
    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }


    // SETTERS

    /**
     * @param id contact name
     */
    public void setId(int id) { this.id = id; }

    /**
     * @param name contact name
     */
    public void setName(String name) { this.id = id; }

    /**
     * @param email contact email
     */
    public void setEmail(String email) { this.email = email; }

}
