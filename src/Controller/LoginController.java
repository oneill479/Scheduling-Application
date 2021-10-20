package Controller;

import Model.User;
import Utilities.DBQuery;
import Utilities.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

import static Model.User.addUser;

public class LoginController implements Initializable {

    public Label locationId;
    public PasswordField userPassword;
    public TextField userName;
    public Button loginButton;
    public Label userLabel;
    public Label passwordLabel;
    public Button exitButton;
    public Label titleLabel;

    // Error strings
    private static String userEmpty, passwordEmpty, userIncorrect, passwordIncorrect;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // get resource bundle
        ResourceBundle rb = ResourceBundle.getBundle("Resources/user", Locale.getDefault());

        ZoneId zone = ZoneId.systemDefault();
        locationId.setText(zone.toString());

        // if french language user change text to french
        if (Locale.getDefault().getLanguage().equals("fr")) {
            titleLabel.setText(rb.getString("title"));
            userLabel.setText(rb.getString("userid"));
            passwordLabel.setText(rb.getString("password"));
            loginButton.setText(rb.getString("login"));
            exitButton.setText(rb.getString("exit"));
        }

        userEmpty = rb.getString("userEmpty");
        passwordEmpty = rb.getString("passwordEmpty");
        userIncorrect = rb.getString("userIncorrect");
        passwordIncorrect = rb.getString("passwordIncorrect");

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

    public void login(ActionEvent actionEvent) throws IOException, SQLException {
        String errorStr = checkUser(userName, userPassword);

        if (errorStr.isEmpty()) {

            // after save go back to main screen
            toMain(actionEvent);

        }
        else {
            // show error string to user
            JOptionPane.showMessageDialog(null, errorStr);
        }

    }

    public static String checkUser(TextField userName, PasswordField userPassword) throws SQLException {
        StringBuilder errorBuild = new StringBuilder();
        int emptyError = 0;
        int foundUser = 0;
        String error;

        // username check
        if (userName.getText().isEmpty()) {
            errorBuild.append(userEmpty + "\n");
            emptyError++;
        }
        if (userPassword.getText().isEmpty()) {
            errorBuild.append(passwordEmpty + "\n");
            emptyError++;
        }

        // create SELECT prepared statement
        String preparedStatement = "SELECT * FROM users"; // SELECT statement

        // query the database
        try {
            DBQuery.setPreparedStatement(JDBC.connection, preparedStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();

            // store results
            ResultSet rs = ps.getResultSet();

            // loop through results to find user
            while (rs.next()) {

                // assign user
                int user_id = rs.getInt("User_ID");
                String user_name = rs.getString("User_Name");

                // if the user is found check the password
                if (userName.getText().equals(user_name)) {
                    // a user was found
                    foundUser++;
                    // assign password to string variable
                    String password = rs.getString("Password");

                    if (userPassword.getText().equals(password)) {
                        // add the current user
                        User user = new User(user_id, userName.getText());
                        addUser(user);
                    }
                    else {
                        errorBuild.append(passwordIncorrect + "\n");
                    }
                }

            }

            if ( (foundUser == 0) && (emptyError == 0) ) {
                errorBuild.append(userIncorrect + "\n");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        error = errorBuild.toString();
        return error;
    }

    /**
     * Exits program
     */
    public void exit() { System.exit(0); }


}
