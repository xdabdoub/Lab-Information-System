package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.patient;

import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Bookmark;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Patient;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.User;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.FXUtils;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

public class PatientEditorController {

    @FXML
    private TextField addressTF;

    @FXML
    private BottomNavigationButton dashboardB;

    @FXML
    private DatePicker dobTF;

    @FXML
    private ComboBox<String> genderCB;

    @FXML
    private BottomNavigationButton historyB;

    @FXML
    private TextField idTF;

    @FXML
    private TextField nameTF;

    @FXML
    private BottomNavigationButton patientB;

    @FXML
    private TextField phoneTF;

    @FXML
    private BottomNavigationButton reportsB;

    @FXML
    private BottomNavigationButton samplesB;

    @FXML
    private BottomNavigationButton submitTF;

    @FXML
    private BottomNavigationButton testRB;

    @FXML
    private Label termsL;

    @FXML
    private Label privacySettingsL;

    @FXML
    private Label nameL;

    @FXML
    private Label myAccountL;

    @FXML
    private Label logOutL;

    @FXML
    private BottomNavigationButton exitAndClose;

    @FXML
    private BottomNavigationButton bookmarksB;

    @FXML
    private AnchorPane bookmarksDropDown;

    @FXML
    private BottomNavigationButton showAllBookmarks;

    @FXML
    private AnchorPane settingsDropDown;

    @FXML
    private VBox bookmarksVBox;

    public static Patient patient;

    @FXML
    public void initialize() {
        genderCB.getItems().addAll("Male", "Female");
        genderCB.getSelectionModel().select("Male");

        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;

        if (user.getBookmarksManager().getBookmarks().isEmpty()) {
            Label label = new Label("You don't have any bookmarks :(!");
            label.setStyle("-fx-text-fill: #0D1E2F; -fx-font-family: Poppins; -fx-font-size: 18px; -fx-font-weight: 700; -fx-font-style: normal");

            bookmarksVBox.getChildren().add(label);
        } else {
            for (Bookmark<?> bm : user.getBookmarksManager().getBookmarks()) {
                FXUtils.addBookmark(bm, bookmarksVBox, -1);
            }
        }

        if (patient == null) return;

        idTF.setText(String.valueOf(patient.getId()));
        nameTF.setText(patient.getName());
        addressTF.setText(patient.getAddress());
        phoneTF.setText(patient.getPhone());
        dobTF.setValue(patient.getDateOfBirth());
        genderCB.getSelectionModel().select(patient.getGender());
    }

    @FXML
    void onDashboard(ActionEvent event) {

    }

    @FXML
    void onHistory(ActionEvent event) {

    }

    @FXML
    void onPatients(ActionEvent event) {
        patient = null;
        UIHandler.open("patients.fxml");
    }

    @FXML
    void onPendingTestResults(ActionEvent event) {
        patient = null;
        UIHandler.open("tests.fxml");
    }

    @FXML
    void onReports(ActionEvent event) {
        patient = null;
        UIHandler.open("reports.fxml");
    }

    @FXML
    void onSamples(ActionEvent event) {
        patient = null;
        UIHandler.open("samples.fxml");
    }

    @FXML
    void onSubmission(ActionEvent event) {
        if (!allFilledAndCorrect(idTF, nameTF, phoneTF, addressTF)) {
            FXUtils.alert("Some values seem to be invalid!", Alert.AlertType.WARNING).show();
            return;
        }

        Optional<ButtonType> result = FXUtils.alert("Are you sure you'd like to proceed?", Alert.AlertType.CONFIRMATION).showAndWait();
        if (!result.isPresent()) return;
        if (result.get() != ButtonType.OK) return;

        long id = Long.parseLong(idTF.getText());
        String name = nameTF.getText();
        String gender = genderCB.getValue();
        LocalDate dateOfBirth = dobTF.getValue();
        String phone = phoneTF.getText();
        String address = addressTF.getText();

        if (patient == null) { // INSERT
            if (GeneralUtils.getPatientById(id) != null) {
                FXUtils.alert("The Id Exisit You cant Add It", Alert.AlertType.ERROR).show();
            } else {

                Patient newPatient = new Patient(id, name, gender, dateOfBirth, phone, address);
                Driver.PRIMARY_MANAGER.getPatientsManager().addPatient(newPatient);
            }
        } else {
            // UPDATE
            long oldId = patient.getId();

            patient.setId(id);
            patient.setName(name);
            patient.setGender(gender);
            patient.setAddress(address);
            patient.setPhone(phone);
            patient.setDateOfBirth(dateOfBirth);

            Driver.PRIMARY_MANAGER.getPatientsManager().updatePatient(patient, oldId);
        }

        patient = null;

        FXUtils.alert("Action was successful!", Alert.AlertType.INFORMATION).show();
        idTF.clear();
        nameTF.clear();
        addressTF.clear();
        phoneTF.clear();
        dobTF.setValue(LocalDate.now());
        genderCB.getSelectionModel().select("Male");

        UIHandler.open("patients.fxml");
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

    @FXML
    void onPrivacy(MouseEvent event) {

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
    void onExitAndClose(ActionEvent event) {
        Optional<ButtonType> response = FXUtils.alert("Are you sure you would like to exit the application?", Alert.AlertType.WARNING).showAndWait();
        if (response.isEmpty()) return;
        if (response.get() != ButtonType.OK) return;

        System.exit(0);
    }

    @FXML
    void onSettings(ActionEvent event) {
        if (!settingsDropDown.isVisible() && bookmarksDropDown.isVisible()) bookmarksDropDown.setVisible(false);
        settingsDropDown.setVisible(!settingsDropDown.isVisible());
    }

    @FXML
    void onBookmarks(ActionEvent event) {
        if (!bookmarksDropDown.isVisible() && settingsDropDown.isVisible()) settingsDropDown.setVisible(false);
        bookmarksDropDown.setVisible(!bookmarksDropDown.isVisible());
    }

    @FXML
    void onShowAllBookmarks(ActionEvent event) {

    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    private boolean allFilledAndCorrect(TextField idTF, TextField nameTF, TextField phoneTF,
                                        TextField addressTF) {
        if (idTF.getText().isEmpty() || nameTF.getText().isEmpty() || phoneTF.getText().isEmpty() || addressTF.getText().isEmpty())
            return false;

        try {
            Long.parseLong(idTF.getText());
        } catch (NumberFormatException ex) { return false; }

        return true;
    }
}