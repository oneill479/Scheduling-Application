package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FirstLevelDivision {

    private static ObservableList<FirstLevelDivision> allDivisions = FXCollections.observableArrayList();
    public static void addDivisions(FirstLevelDivision newDivision) {
        allDivisions.add(newDivision);
    };

    private int id, countryId;
    private String name;

    public FirstLevelDivision(int id, String name, int countryId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
    }


    // GETTERS

    /**
     * @return division id
     */
    public int getId() { return id; }

    /**
     * @return division name
     */
    public String getName() { return name; }

    /**
     * @return country id
     */
    public int getCountryId() { return countryId; }

    /**
     * This method gets a list of all the divisions
     * @return returns an observable list of divisions
     */
    public static ObservableList<FirstLevelDivision> getAllDivisions() {
        return allDivisions;
    }


    // SETTERS

    /**
     * @param id division id
     */
    public void setId(int id) { this.id = id; }

    /**
     * @param name division name
     */
    public void setName(String name) { this.name = name; }

    /**
     * @param countryId country id
     */
    public void setCountryId(int countryId) { this.countryId = countryId; }

}
