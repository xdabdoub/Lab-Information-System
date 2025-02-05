package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.reports;

import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.Permission;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.TestStatus;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Bookmark;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Report;
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

public class ReportEditorController {

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
    private TextField testIdTF;

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
    private DatePicker reportDateTF;

    @FXML
    private BottomNavigationButton testRB1;

    @FXML
    private TextArea resultTA;
/*
    private TextArea resultTA;
    private TextField testIdTF;
    private DatePicker reportDateTF;
    private TextField idTF;
*/
    public static Report report;


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

        idTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);
        reportDateTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);

        if (report == null) {
            idTF.setDisable(false);
            idTF.setText((GeneralUtils.getLastTestId() + 1) + "");
            reportDateTF.setValue(LocalDate.now());
            return;
        } else {
            idTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);
        }

        idTF.setText(String.valueOf(report.getReportId()));
        reportDateTF.setValue(report.getDate());
    }

    @FXML
    void onDashboard(ActionEvent event) {

    }

    @FXML
    void onHistory(ActionEvent event) {

    }

    @FXML
    void onPatients(ActionEvent event) {
        report = null;
        UIHandler.open("patients.fxml");
    }

    @FXML
    void onPendingTestResults(ActionEvent event) {
        report = null;
        UIHandler.open("tests.fxml");
    }

    @FXML
    void onReports(ActionEvent event) {
        report = null;
        UIHandler.open("reports.fxml");
    }

    @FXML
    void onSamples(ActionEvent event) {
        report = null;
        UIHandler.open("samples.fxml");
    }

    @FXML
    void onSubmission(ActionEvent event) {
        if (!allFilledAndCorrect(idTF,testIdTF, resultTA)) {
            FXUtils.alert("Some values seem to be invalid!", Alert.AlertType.WARNING).show();
            return;
        }


        Optional<ButtonType> result = FXUtils.alert("Are you sure you'd like to proceed?", Alert.AlertType.CONFIRMATION).showAndWait();
        if (!result.isPresent()) return;
        if (result.get() != ButtonType.OK) return;


        int id = Integer.parseInt(idTF.getText());
        LocalDate reportDate = reportDateTF.getValue();
        String resultText = resultTA.getText();
        LocalDate lastModified = LocalDate.now();
        Test testObj = GeneralUtils.getTestById(Integer.parseInt(testIdTF.getText()));


        if (report == null) { // INSERT
            if(GeneralUtils.getReportById(id)!=null){
                FXUtils.alert("The ID already Exists. You cannot add it.", Alert.AlertType.ERROR).show();

            }
            else{
                Report newReport = new Report(id, Integer.parseInt(testIdTF.getText()), reportDate, resultText,lastModified);
                Driver.PRIMARY_MANAGER.getReportsManager().addReport(newReport);
            }

        } else {
            // UPDATE
            long oldId = report.getReportId();
            report.setReportId(id);
            report.setTest(testObj);
            report.setDate(reportDate);
            report.setResult(resultText);
            report.setLastModified(lastModified);
            Driver.PRIMARY_MANAGER.getReportsManager().updateReports(report, oldId);
        }

        report = null;

        FXUtils.alert("Action was successful!", Alert.AlertType.INFORMATION).show();
        idTF.clear();
        testIdTF.clear();
        reportDateTF.setValue(LocalDate.now());
        resultTA.clear();
        UIHandler.open("reports.fxml");
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
        report = null;
        UIHandler.open("invoices.fxml");
    }

    private boolean allFilledAndCorrect(TextField idTF, TextField testIdTF, TextArea resultTA) {
        if (idTF.getText().isEmpty() || testIdTF.getText().isEmpty() || resultTA.getText().isEmpty())
            return false;

        try {
            Integer.parseInt(idTF.getText());
            Integer.parseInt(testIdTF.getText());
        } catch (NumberFormatException ex) { return false; }

        return true;
    }

}