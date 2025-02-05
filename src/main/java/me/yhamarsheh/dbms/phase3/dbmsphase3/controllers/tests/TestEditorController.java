package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.tests;

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
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.Permission;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.SampleType;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.TestStatus;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Bookmark;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Test;
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

public class TestEditorController {

    @FXML
    private BottomNavigationButton bookmarksB;

    @FXML
    private AnchorPane bookmarksDropDown;

    @FXML
    private VBox bookmarksVBox;

    @FXML
    private BottomNavigationButton dashboardB;

    @FXML
    private BottomNavigationButton exitAndClose1;

    @FXML
    private BottomNavigationButton historyB;

    @FXML
    private TextField idTF;

    @FXML
    private Label logOutL1;

    @FXML
    private Label myAccountL1;

    @FXML
    private Label nameL1;

    @FXML
    private BottomNavigationButton patientB;

    @FXML
    private Label privacySettingsL1;

    @FXML
    private BottomNavigationButton reportsB;

    @FXML
    private TextField sampleIdTF;

    @FXML
    private BottomNavigationButton samplesB;

    @FXML
    private AnchorPane settingsDropDown;

    @FXML
    private BottomNavigationButton showAllBookmarks;

    @FXML
    private BottomNavigationButton submitTF;

    @FXML
    private Label termsL1;

    @FXML
    private DatePicker testDateTF;

    @FXML
    private BottomNavigationButton testRB1;

    @FXML
    private ComboBox<TestStatus> testStatusCB;

    public static Test test;

    @FXML
    public void initialize() {
        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;

        nameL1.setText("Hello, " + Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser().getDoctor().getName());

        if (user.getBookmarksManager().getBookmarks().isEmpty()) {
            Label label = new Label("You don't have any bookmarks :(!");
            label.setStyle("-fx-text-fill: #0D1E2F; -fx-font-family: Poppins; -fx-font-size: 18px; -fx-font-weight: 700; -fx-font-style: normal");

            bookmarksVBox.getChildren().add(label);
        } else {

            for (Bookmark<?> bm : user.getBookmarksManager().getBookmarks()) {
                FXUtils.addBookmark(bm, bookmarksVBox, -1);
            }
        }

        testStatusCB.getItems().addAll(TestStatus.values());

        testStatusCB.getSelectionModel().select(TestStatus.PENDING);

        idTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);
        testDateTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);

        if (test == null) {
            sampleIdTF.setDisable(false);
            idTF.setText((GeneralUtils.getLastTestId() + 1) + "");
            testDateTF.setValue(LocalDate.now());
            return;
        } else {
            sampleIdTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);
        }

        idTF.setText(String.valueOf(test.getTestId()));
        sampleIdTF.setText(test.getSample().getSampleId() + "");
        testDateTF.setValue(test.getTestDate());
    }

    @FXML
    void onDashboard(ActionEvent event) {
        UIHandler.open("dashboard.fxml");
    }

    @FXML
    void onHistory(ActionEvent event) {

    }

    @FXML
    void onPatients(ActionEvent event) {
        test = null;
        UIHandler.open("patients.fxml");
    }

    @FXML
    void onPendingTestResults(ActionEvent event) {
        test = null;
        UIHandler.open("tests.fxml");
    }

    @FXML
    void onReports(ActionEvent event) {
        test = null;
        UIHandler.open("reports.fxml");
    }

    @FXML
    void onSamples(ActionEvent event) {
        test = null;
        UIHandler.open("samples.fxml");
    }

    @FXML
    void onSubmission(ActionEvent event) {
        if (!allFilledAndCorrect(idTF , sampleIdTF)) {
            FXUtils.alert("Some values seem to be invalid!", Alert.AlertType.WARNING).show();
            return;
        }


        Optional<ButtonType> result = FXUtils.alert("Are you sure you'd like to proceed?", Alert.AlertType.CONFIRMATION).showAndWait();
        if (!result.isPresent()) return;
        if (result.get() != ButtonType.OK) return;

        int id = Integer.parseInt(idTF.getText());
        int sampleId=Integer.parseInt(sampleIdTF.getText());
        LocalDate testDate = testDateTF.getValue();
        TestStatus testStatus = testStatusCB.getSelectionModel().getSelectedItem();
        LocalDate lastModified = LocalDate.now(); 

        if (test == null) { // INSERT
            if(GeneralUtils.getTestById(id)!=null){
                FXUtils.alert("The ID already Exists. You cannot add it.", Alert.AlertType.ERROR).show();

            }
            else{
                Test newTest = new Test(id, sampleId, testStatus, testDate,lastModified);
                Driver.PRIMARY_MANAGER.getTestsManager().addTest(newTest);
            }

        } else {
            // UPDATE
            long oldId = test.getTestId();

            test.setTestId(id);
            test.setSample(GeneralUtils.getSampleById(sampleId));
            test.setTestStatus(testStatusCB.getSelectionModel().getSelectedItem());
            test.setTestDate(testDate);
            test.setLastModified(LocalDate.now());
            Driver.PRIMARY_MANAGER.getTestsManager().updateTest(test, oldId);
        }

        test = null;

        FXUtils.alert("Action was successful!", Alert.AlertType.INFORMATION).show();
        idTF.clear();
        sampleIdTF.clear();
        testDateTF.setValue(LocalDate.now());
        testStatusCB.getSelectionModel().select(TestStatus.PENDING);

        UIHandler.open("tests.fxml");
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
        UIHandler.open("personal_info.fxml");
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

    @FXML
    void onInvoices(ActionEvent event) {
        test = null;
        UIHandler.open("invoices.fxml");
    }

    private boolean allFilledAndCorrect(TextField idTF, TextField sampleIdTF) {
        if (idTF.getText().isEmpty() || sampleIdTF.getText().isEmpty())
            return false;

        try {
            Integer.parseInt(idTF.getText());
            Integer.parseInt(sampleIdTF.getText());
        } catch (NumberFormatException ex) { return false; }

        return true;
    }

}



