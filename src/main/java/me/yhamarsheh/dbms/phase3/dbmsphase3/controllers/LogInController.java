package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.User;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.FXUtils;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.io.*;

public class LogInController {

    @FXML
    private PasswordField passwordTF;

    @FXML
    private CheckBox rememberMe;

    @FXML
    private Button signInB;

    @FXML
    private TextField usernameTF;

    @FXML
    private Label passwordStatus;

    @FXML
    void initialize() {
    }

    @FXML
    void onSignIn(ActionEvent event) {
        String username = usernameTF.getText();
        String password = passwordTF.getText();
        if (!allFilledAndCorrect(usernameTF, passwordTF)) return;

        User user = GeneralUtils.getUserByCredentials(username, password);
        if (user == null) {
            passwordStatus.setText("Incorrect username or password");
            passwordStatus.setVisible(true);
            return;
        }

        String filePath = System.getProperty("user.home") + "/AppData/Roaming/Medical Laboratory/cookies.txt";
        File cookiesFile = new File(filePath);
        try {
            if (rememberMe.isSelected()) {
                byte[] usernameEnc = GeneralUtils.encodeString(usernameTF.getText());
                byte[] passwordEnc = GeneralUtils.encodeString(passwordTF.getText());

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(cookiesFile))) {
                    writer.write("next_session: true");
                    writer.newLine();
                    writer.write(new String(usernameEnc) + "," + new String(passwordEnc));
                }

            } else {
                if (cookiesFile.exists()) {
                    cookiesFile.delete();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            rememberMe.setSelected(false);
            System.out.println("Couldn't save your credentials for the next sessions, please try again later!");
        }

        Driver.PRIMARY_MANAGER.getUsersManager().setActiveUser(user);
        UIHandler.open("patients.fxml");
        FXUtils.alert("Welcome back, " + user.getDoctor().getName() + "!", Alert.AlertType.INFORMATION).show();
    }

    @FXML
    void onRememberMe(ActionEvent event) {
    }

    private boolean allFilledAndCorrect(TextField usernameTF, PasswordField passwordTF) {
        if (usernameTF.getText().isEmpty() && passwordTF.getText().isEmpty()) {
            usernameTF.getStyleClass().add("text-field-error");
            passwordTF.getStyleClass().add("password-field-error");
            return false;
        } else if (usernameTF.getText().isEmpty() && !passwordTF.getText().isEmpty()) {
            usernameTF.getStyleClass().add("text-field-error");
            return false;
        } else if (!usernameTF.getText().isEmpty() && passwordTF.getText().isEmpty()) {
            passwordTF.getStyleClass().add("password-field-error");
            return false;
        }

        if (passwordTF.getText().length() < 8) {
            passwordStatus.setText("Password must consist of at least 8 characters");
            passwordStatus.setVisible(true);
            return false;
        }

        usernameTF.getStylesheets().remove("text-field-error");
        passwordTF.getStylesheets().remove("password-field-error");
        passwordStatus.setVisible(false);

        return true;
    }

}
