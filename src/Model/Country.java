package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Country {

    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    public static void addCountry(Country newCountry) {
        allCountries.add(newCountry);
    };

    private int id;
    private String name;

    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }


    // GETTERS

    /**
     * @return country id
     */
    public int getId() { return id; }

    /**
     * @return country name
     */
    public String getName() { return name; }

    /**
     * This method gets a list of all the countries
     * @return returns an observable list of countries
     */
    public static ObservableList<Country> getAllCountries() {
        return allCountries;
    }


    // SETTERS

    /**
     * @param id country id
     */
    public void setId(int id) { this.id = id; }

    /**
     * @param name country name
     */
    public void setName(String name) { this.name = name; }

}
