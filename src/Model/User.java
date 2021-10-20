package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {

    private static ObservableList<User> currentUser = FXCollections.observableArrayList();
    public static void addUser(User user) { currentUser.add(user); }

    private int userId;
    private String userName;

    public User(int userId, String userName) {
        this.userName = userName;
        this.userId = userId;
    }


    // GETTERS //

    /**
     * @return user id
     */
    public int getUserId() { return userId; }

    /**
     * @return user name
     */
    public String getUserName() { return userName; }

    /**
     * This method gets a list of all the customer data
     * @return returns an observable list of customer data
     */
    public static ObservableList<User> getCurrentUser() {
        return currentUser;
    }


    // SETTERS //

    /**
     * @param userId user name
     */
    public void setUserId(int userId) { this.userName = userName; }

    /**
     * @param userName user name
     */
    public void setUserPassword(String userName) { this.userName = userName; }



}
