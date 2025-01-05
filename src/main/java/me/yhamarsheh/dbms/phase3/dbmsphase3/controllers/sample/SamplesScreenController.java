package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.sample;

import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.patient.PatientViewerController;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Bookmark;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Patient;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Sample;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.User;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.FXUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

public class SamplesScreenController {

    @FXML
    private TableColumn<Sample, Long> collectedByCol;

    @FXML
    private BottomNavigationButton dashboardB;

    @FXML
    private BottomNavigationButton deleteB;

    @FXML
    private Label discSampleType;

    @FXML
    private Label discreteGL;

    @FXML
    private TableColumn<Sample, LocalDate> collectionDateCol;

    @FXML
    private TableColumn<Sample, String> sampleTypeCol;

    @FXML
    private BottomNavigationButton historyB;

    @FXML
    private TableColumn<Sample, Integer> idCol;

    @FXML
    private BottomNavigationButton insertB;

    @FXML
    private TableColumn<Sample, Long> patientIdCol;

    @FXML
    private BottomNavigationButton patientB;

    @FXML
    private BottomNavigationButton reportsB;

    @FXML
    private BottomNavigationButton samplesB;

    @FXML
    private TextField searchTF;

    @FXML
    private TableView<Sample> tableView;

    @FXML
    private BottomNavigationButton testRB;

    @FXML
    private Label totalSamples;

    @FXML
    private BottomNavigationButton updateB;

    @FXML
    private BottomNavigationButton bookmarksB;

    @FXML
    private AnchorPane bookmarksDropDown;

    @FXML
    private BottomNavigationButton showAllBookmarks;

    @FXML
    private AnchorPane settingsDropDown;

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
    private VBox bookmarksVBox;

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSampleId()).asObject());
        patientIdCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getPatient().getId()).asObject());
        sampleTypeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSampleType().toString()));
        collectionDateCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCollectionDate()));
        collectedByCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getCollectedBy().getId()).asObject());

        tableView.getItems().addAll(Driver.PRIMARY_MANAGER.getSamplesManager().getSamples());

        totalSamples.setText(Driver.PRIMARY_MANAGER.getSamplesManager().getTotalSamples() + "");
        discSampleType.setText(Driver.PRIMARY_MANAGER.getSamplesManager().getDiscreteSampleType());
        discreteGL.setText(Driver.PRIMARY_MANAGER.getSamplesManager().getDiscretePatientsGender());

        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;

        if (user.getBookmarksManager().getBookmarks().isEmpty()) {
            Label label = new Label("You don't have any bookmarks :(!");
            label.setStyle("-fx-text-fill: #0D1E2F; -fx-font-family: Poppins; -fx-font-size: 18px; -fx-font-weight: 700; -fx-font-style: normal");

            bookmarksVBox.getChildren().add(label);
            return;
        }

        for (Bookmark<?> bm : Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser().getBookmarksManager().getBookmarks()) {
            FXUtils.addBookmark(bm, bookmarksVBox, -1);
        }
    }

    @FXML
    void onDashboard(ActionEvent event) {

    }

    @FXML
    void onDeleteSample(ActionEvent event) {
        Sample sample = tableView.getSelectionModel().getSelectedItem();
        if (sample == null) {
            FXUtils.alert("Selection is Empty! You must select a sample to delete.", Alert.AlertType.ERROR).show();
            return;
        }

        Alert alert = FXUtils.alert("Are you sure you'd like to PERMANENTLY delete this sample?", Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty()) return;
        if (result.get() != ButtonType.OK) return;

        Driver.PRIMARY_MANAGER.getSamplesManager().deleteSample(sample);
        tableView.getItems().remove(sample);
        tableView.refresh();

        FXUtils.alert("Successfully deleted this patient!", Alert.AlertType.INFORMATION).show();
    }

    @FXML
    void onHistory(ActionEvent event) {

    }

    @FXML
    void onInsertSample(ActionEvent event) {
        UIHandler.open("insert_sample.fxml");
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
    void onReports(ActionEvent event) {
        UIHandler.open("reports.fxml");
    }

    @FXML
    void onSamples(ActionEvent event) {
    }

    @FXML
    void onSearch(KeyEvent event) {
        ObservableList<Sample> filteredPatients = FXCollections.observableArrayList();

        if (searchTF.getText().isEmpty()) {
            filteredPatients.addAll(Driver.PRIMARY_MANAGER.getSamplesManager().getSamples());
        } else {
            getSampleByPartOfIdOrSampleType(filteredPatients, searchTF.getText());
        }

        tableView.setItems(filteredPatients);
    }

    @FXML
    void onUpdateSample(ActionEvent event) {
        Sample sample = tableView.getSelectionModel().getSelectedItem();
        if (sample == null) {
            FXUtils.alert("Selection is Empty! You must select a sample to update.", Alert.AlertType.ERROR).show();
            return;
        }

        SampleEditorController.sample = sample;
        UIHandler.open("insert_sample.fxml");
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

    @FXML
    void onViewSample(ActionEvent event) {
        Sample sample = tableView.getSelectionModel().getSelectedItem();
        if (sample == null) {
            FXUtils.alert("Selection is Empty! You must select a sample to view.", Alert.AlertType.ERROR).show();
            return;
        }

        SampleViewerController.sample = sample;
        UIHandler.open("sample_viewer.fxml");
    }

    private void getSampleByPartOfIdOrSampleType(ObservableList<Sample> filteredPatients, String input) {
        for (Sample sample : Driver.PRIMARY_MANAGER.getSamplesManager().getSamples()) {
            if (String.valueOf(sample.getSampleId()).startsWith(input) || sample.getSampleType().toString().toLowerCase().startsWith(input.toLowerCase())) {
                filteredPatients.add(sample);
            }
        }
    }

}
