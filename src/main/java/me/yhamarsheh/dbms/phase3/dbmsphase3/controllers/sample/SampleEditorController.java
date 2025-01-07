package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.sample;

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
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub.BookmarksManager;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Bookmark;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Sample;
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

public class SampleEditorController {

    @FXML
    private TextField collectedByTF;

    @FXML
    private DatePicker collectionDateTF;

    @FXML
    private BottomNavigationButton dashboardB;

    @FXML
    private BottomNavigationButton historyB;

    @FXML
    private TextField idTF;

    @FXML
    private BottomNavigationButton patientB;

    @FXML
    public TextField patientIdTF;

    @FXML
    private BottomNavigationButton reportsB;

    @FXML
    private ComboBox<SampleType> sampleTypeCB;

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

    public static Sample sample;

    @FXML
    public void initialize() {
        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;

        BookmarksManager bookmarksManager = user.getBookmarksManager();
        if (bookmarksManager.getBookmarks().isEmpty()) {
            Label label = new Label("You don't have any bookmarks :(!");
            label.setStyle("-fx-text-fill: #0D1E2F; -fx-font-family: Poppins; -fx-font-size: 18px; -fx-font-weight: 700; -fx-font-style: normal");

            bookmarksVBox.getChildren().add(label);
        } else {
            for (Bookmark<?> bm : bookmarksManager.getBookmarks()) {
                FXUtils.addBookmark(bm, bookmarksVBox, -1);
            }
        }

        sampleTypeCB.getItems().addAll(SampleType.values());

        sampleTypeCB.getSelectionModel().select(SampleType.BLOOD);

        idTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);
        collectionDateTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);
        collectedByTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);

        if (sample == null) {
            patientIdTF.setDisable(false);
            idTF.setText((GeneralUtils.getLastSampleId() + 1) + "");
            collectionDateTF.setValue(LocalDate.now());
            collectedByTF.setText(user.getDoctor().getId() + "");
            return;
        } else {
            patientIdTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);
        }

        idTF.setText(String.valueOf(sample.getSampleId()));
        patientIdTF.setText(sample.getPatient().getId() + "");
        collectionDateTF.setValue(sample.getCollectionDate());
        collectedByTF.setText(user.getDoctor().getId() + "");
    }

    @FXML
    void onDashboard(ActionEvent event) {

    }

    @FXML
    void onHistory(ActionEvent event) {

    }

    @FXML
    void onPatients(ActionEvent event) {
        sample = null;
        UIHandler.open("patients.fxml");
    }

    @FXML
    void onPendingTestResults(ActionEvent event) {
        sample = null;
        UIHandler.open("tests.fxml");
    }


    @FXML
    void onReports(ActionEvent event) {
        sample = null;
        UIHandler.open("reports.fxml");
    }

    @FXML
    void onSamples(ActionEvent event) {
        sample = null;
        UIHandler.open("samples.fxml");
    }

    @FXML
    void onSubmission(ActionEvent event) {
        if (!allFilledAndCorrect(patientIdTF)) {
            FXUtils.alert("Some values seem to be invalid!", Alert.AlertType.WARNING).show();
            return;
        }

        Optional<ButtonType> result = FXUtils.alert("Are you sure you'd like to proceed?", Alert.AlertType.CONFIRMATION).showAndWait();
        if (!result.isPresent()) return;
        if (result.get() != ButtonType.OK) return;

        int id = Integer.parseInt(idTF.getText());
        long patientId = Long.parseLong(patientIdTF.getText());
        SampleType sampleType = sampleTypeCB.getSelectionModel().getSelectedItem();
        LocalDate collectionDate = collectionDateTF.getValue();
        long collectedBy = Long.parseLong(collectedByTF.getText());

        if (sample == null) { // INSERT
            if(GeneralUtils.getSampleById(id)!=null){
                FXUtils.alert("The ID already Exists. You cannot add it.", Alert.AlertType.ERROR).show();

            }
            else{
                Sample newSample = new Sample(id, patientId, collectedBy, sampleType.toString(), collectionDate, LocalDate.now());
                Driver.PRIMARY_MANAGER.getSamplesManager().addSample(newSample);
            }

        } else {
            // UPDATE
            long oldId = sample.getSampleId();

            sample.setSampleId(id);
            sample.setPatient(GeneralUtils.getPatientById(patientId));
            sample.setSampleType(sampleTypeCB.getSelectionModel().getSelectedItem());
            sample.setCollectionDate(collectionDate);
            sample.setLastModified(LocalDate.now());
            Driver.PRIMARY_MANAGER.getSamplesManager().updateSample(sample, oldId);
        }

        sample = null;

        FXUtils.alert("Action was successful!", Alert.AlertType.INFORMATION).show();
        idTF.clear();
        patientIdTF.clear();
        collectedByTF.clear();
        collectionDateTF.setValue(LocalDate.now());
        sampleTypeCB.getSelectionModel().select(SampleType.BLOOD);

        UIHandler.open("samples.fxml");
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

    private boolean allFilledAndCorrect(TextField patientIdTF) {
        if (patientIdTF.getText().isEmpty())
            return false;

        try {
            Long.parseLong(patientIdTF.getText());
        } catch (NumberFormatException ex) { return false; }

        return true;
    }
}