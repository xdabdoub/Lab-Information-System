package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.dashboard;

import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.Permission;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.User;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.FXUtils;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class UserAccountController {

    @FXML
    private TableView<User> adminTableView;

    @FXML
    private BottomNavigationButton bookmarksB;

    @FXML
    private AnchorPane bookmarksDropDown;

    @FXML
    private VBox bookmarksVBox;

    @FXML
    private BottomNavigationButton dashboardB;

    @FXML
    private BottomNavigationButton exitAndClose;

    @FXML
    private BottomNavigationButton historyB;

    @FXML
    private Label legalName;

    @FXML
    private Label logOutL;

    @FXML
    private Label myAccountL;

    @FXML
    private Label nameL;

    @FXML
    private Label nameL1;

    @FXML
    private TextField passwordTF;

    @FXML
    private BottomNavigationButton patientB;

    @FXML
    private Label permissions;

    @FXML
    private Label privacySettingsL;

    @FXML
    private BottomNavigationButton reportsB;

    @FXML
    private BottomNavigationButton samplesB;

    @FXML
    private AnchorPane settingsDropDown;

    @FXML
    private BottomNavigationButton showAllBookmarks;

    @FXML
    private Label termsL;

    @FXML
    private BottomNavigationButton testRB;

    @FXML
    private Label username;

    @FXML
    private TextField usernameTF;

    @FXML
    private Label adminOnlyL;

    @FXML
    private BottomNavigationButton addB;

    @FXML
    private BottomNavigationButton deleteB;

    @FXML
    private TextField doctorIdTF;

    @FXML
    private ComboBox<Permission> permCB;

    @FXML
    void initialize() {
        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;

        nameL.setText("Hello, " + Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser().getDoctor().getName());

        legalName.setText(user.getDoctor().getName());
        username.setText(GeneralUtils.decodeString(user.getUsername()));
        permissions.setText(user.getPermission().toString());

        if (!user.isAdmin()) {
            adminOnlyL.setVisible(false);
            usernameTF.setVisible(false);
            passwordTF.setVisible(false);

            addB.setVisible(false);
            deleteB.setVisible(false);
            adminTableView.setVisible(false);
            doctorIdTF.setVisible(false);
            permCB.setVisible(false);
            return;
        }

        adminTableView.getItems().addAll(Driver.PRIMARY_MANAGER.getUsersManager().getUsers());
        permCB.getItems().addAll(Permission.values());
        permCB.getSelectionModel().select(Permission.REGULAR);
    }

    @FXML
    void onAddUser(ActionEvent event) {
        if (allFilledCorrectly(usernameTF, passwordTF, doctorIdTF)) {
            FXUtils.alert("Some values seem to be invalid!", Alert.AlertType.ERROR).show();
            return;
        }


        long doctorId = Long.parseLong(doctorIdTF.getText());
        if (GeneralUtils.getDoctorById(doctorId) == null) {
            FXUtils.alert("There is no doctor with the entered ID!", Alert.AlertType.ERROR).show();
            return;
        }

        if (GeneralUtils.getUserByDoctorId(doctorId) != null) {
            FXUtils.alert("There is already a user with the entered doctor ID!", Alert.AlertType.ERROR).show();
            return;
        }

        User user = new User(GeneralUtils.encodeString(usernameTF.getText()),
                GeneralUtils.encodeString(passwordTF.getText()), doctorId, permCB.getSelectionModel().getSelectedItem());
        Driver.PRIMARY_MANAGER.getUsersManager().addUser(user);
        FXUtils.alert("Action was successful!", Alert.AlertType.INFORMATION).show();
    }

    @FXML
    void onBookmarks(ActionEvent event) {
        if (!bookmarksDropDown.isVisible() && settingsDropDown.isVisible()) settingsDropDown.setVisible(false);
        bookmarksDropDown.setVisible(!bookmarksDropDown.isVisible());
    }

    @FXML
    void onChangePassword(MouseEvent event) {
        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change your Password");
        dialog.setHeaderText("Change your Password");

        Scene dialogScene = dialog.getDialogPane().getScene();
        dialogScene.getStylesheets().add(getClass().getResource("/css/dialogue.css").toExternalForm());

        dialog.showAndWait().ifPresent(input -> {
            user.setPassword(GeneralUtils.encodeString(input));
            Driver.PRIMARY_MANAGER.getUsersManager().updateUser(user, user.getDoctor().getId());
        });
    }

    @FXML
    void onChangeUsername(MouseEvent event) {
        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change your Username");
        dialog.setHeaderText("Change your Username");

        Scene dialogScene = dialog.getDialogPane().getScene();
        dialogScene.getStylesheets().add(getClass().getResource("/css/dialogue.css").toExternalForm());

        dialog.showAndWait().ifPresent(input -> {
            username.setText(input);
            user.setUsername(GeneralUtils.encodeString(input));
            Driver.PRIMARY_MANAGER.getUsersManager().updateUser(user, user.getDoctor().getId());
        });
    }

    @FXML
    void onDashboard(ActionEvent event) {
        UIHandler.open("dashboard.fxml");
    }

    @FXML
    void onDeleteUser(ActionEvent event) {
        User user = adminTableView.getSelectionModel().getSelectedItem();
        if (user == null) {
            FXUtils.alert("You must have a user selected from the table to delete!", Alert.AlertType.ERROR).show();
            return;
        }

        Driver.PRIMARY_MANAGER.getUsersManager().deleteUser(user);
        FXUtils.alert("Action was successful!", Alert.AlertType.INFORMATION).show();
        adminTableView.getItems().remove(user);

        adminTableView.refresh();
    }

    @FXML
    void onEditName(MouseEvent event) {
        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change your Legal Name");
        dialog.setHeaderText("Change your Legal Name");

        Scene dialogScene = dialog.getDialogPane().getScene();
        dialogScene.getStylesheets().add(getClass().getResource("/css/dialogue.css").toExternalForm());

        dialog.showAndWait().ifPresent(input -> {
            legalName.setText(input);
            user.getDoctor().setName(input);
            Driver.PRIMARY_MANAGER.getDoctorsManager().updateDoctor(user.getDoctor(), user.getDoctor().getId());
        });
    }

    @FXML
    void onExitAndClose(ActionEvent event) {
        Optional<ButtonType> response = FXUtils.alert("Are you sure you would like to exit the application?", Alert.AlertType.WARNING).showAndWait();
        if (response.isEmpty()) return;
        if (response.get() != ButtonType.OK) return;

        System.exit(0);
    }

    @FXML
    void onHistory(ActionEvent event) {

    }

    @FXML
    void onInvoices(ActionEvent event) {
        UIHandler.open("invoices.fxml");
    }

    @FXML
    void onLogOut(MouseEvent event) {
        String filePath = System.getProperty("user.home") + "/AppData/Roaming/Medical Laboratory/cookies.txt";
        File cookiesFile = new File(filePath);

        if (cookiesFile.exists()) cookiesFile.delete();

        Driver.PRIMARY_MANAGER.getUsersManager().setActiveUser(null);
        UIHandler.open("login.fxml");
    }

    @FXML
    void onMyAccount(MouseEvent event) {

    }

    @FXML
    void onPatients(ActionEvent event) {

        UIHandler.open("patients.fxml");
    }

    @FXML
    void onPendingTestResults(ActionEvent event) {
        UIHandler.open("tests.fxml");
    }

    @FXML
    void onPrivacy(MouseEvent event) {

    }

    @FXML
    void onReports(ActionEvent event) {
        UIHandler.open("reports.fxml");
    }

    @FXML
    void onSamples(ActionEvent event) {
        UIHandler.open("samples.fxml");
    }

    @FXML
    void onSettings(ActionEvent event) {
        if (!settingsDropDown.isVisible() && bookmarksDropDown.isVisible()) bookmarksDropDown.setVisible(false);
        settingsDropDown.setVisible(!settingsDropDown.isVisible());
    }

    @FXML
    void onShowAllBookmarks(ActionEvent event) {

    }

    @FXML
    void onTerms(MouseEvent event) {
        try {
            URI website = new URI("https://www.termsfeed.com/public/uploads/2021/12/sample-terms-conditions-agreement-screenshot-2.jpg");
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(website);
            } else {
                FXUtils.alert("Your system does not allow third party apps pop-ups!", Alert.AlertType.ERROR).show();
            }
        } catch (IOException | URISyntaxException e) {
            FXUtils.alert("Your system does not allow third party apps pop-ups!", Alert.AlertType.ERROR).show();
        }
    }

    private boolean allFilledCorrectly(TextField username, TextField password, TextField doctorId) {
        if (username.getText().isEmpty() || password.getText().isEmpty()) return false;

        try {
            Long.parseLong(doctorId.getText());
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

}
