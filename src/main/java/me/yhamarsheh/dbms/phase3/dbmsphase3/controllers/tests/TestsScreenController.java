package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.tests;

import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.sample.SampleEditorController;
import me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.sample.SampleViewerController;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.TestStatus;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Bookmark;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Sample;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Test;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.User;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.FXUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class TestsScreenController {

    @FXML
    private BottomNavigationButton bookmarksB;

    @FXML
    private AnchorPane bookmarksDropDown;

    @FXML
    private VBox bookmarksVBox;

    @FXML
    private BottomNavigationButton dashboardB;

    @FXML
    private BottomNavigationButton deleteB;

    @FXML
    private Label discreteTS;

    @FXML
    private BottomNavigationButton exitAndClose;

    @FXML
    private BottomNavigationButton exitAndClose1;

    @FXML
    private BottomNavigationButton historyB;

    @FXML
    private TableColumn<Test, Integer> idCol;

    @FXML
    private BottomNavigationButton insertB;

    @FXML
    private Label logOutL;

    @FXML
    private Label logOutL1;

    @FXML
    private Label myAccountL;

    @FXML
    private Label myAccountL1;

    @FXML
    private Label nameL;

    @FXML
    private Label nameL1;

    @FXML
    private BottomNavigationButton patientB;

    @FXML
    private Label privacySettingsL;

    @FXML
    private Label privacySettingsL1;

    @FXML
    private BottomNavigationButton reportsB;

    @FXML
    private TableColumn<Test, Integer> sampleIdCol;

    @FXML
    private BottomNavigationButton samplesB;

    @FXML
    private TextField searchTF;

    @FXML
    private AnchorPane settingsDropDown;

    @FXML
    private AnchorPane settingsDropDown1;

    @FXML
    private BottomNavigationButton showAllBookmarks;

    @FXML
    private TableView<Test> tableView;

    @FXML
    private Label termsL;

    @FXML
    private Label termsL1;

    @FXML
    private TableColumn<Test, LocalDate> testDateCol;

    @FXML
    private BottomNavigationButton testRB;

    @FXML
    private TableColumn<Test, String> testStatusCol;

    @FXML
    private Label totalTests;

    @FXML
    private BottomNavigationButton updateB;

    @FXML
    private BottomNavigationButton viewB;

    @FXML
    private Label weekTests;

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTestId()).asObject());
        sampleIdCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSample().getSampleId()).asObject());
        testStatusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTestStatus().toString()));
        testDateCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTestDate()));

        tableView.getItems().addAll(Driver.PRIMARY_MANAGER.getTestsManager().getTests());

        totalTests.setText(Driver.PRIMARY_MANAGER.getTestsManager().getTotalTests() + "");

        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusDays(7);

        Date startDate = Date.valueOf(oneWeekAgo);
        Date endDate = Date.valueOf(today);

        weekTests.setText(Driver.PRIMARY_MANAGER.getTestsManager().getTestsWithinDateRange(startDate, endDate) + "");
        discreteTS.setText(Driver.PRIMARY_MANAGER.getTestsManager().getDiscreteTestStatus());

        nameL.setText("Hello, " + Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser().getDoctor().getName());

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
    void onDeleteTest(ActionEvent event) {
        Test test = tableView.getSelectionModel().getSelectedItem();
        if (test == null) {
            FXUtils.alert("Selection is Empty! You must select a test to delete.", Alert.AlertType.ERROR).show();
            return;
        }

        Alert alert = FXUtils.alert("Are you sure you'd like to PERMANENTLY delete this test?", Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty()) return;
        if (result.get() != ButtonType.OK) return;

        Driver.PRIMARY_MANAGER.getTestsManager().deleteTest(test);
        tableView.getItems().remove(test);
        tableView.refresh();

        FXUtils.alert("Successfully deleted this patient!", Alert.AlertType.INFORMATION).show();
    }

    @FXML
    void onHistory(ActionEvent event) {

    }

    @FXML
    void onInsertTest(ActionEvent event) {
        UIHandler.open("insert_test.fxml");
    }

    @FXML
    void onPatients(ActionEvent event) {
        UIHandler.open("patients.fxml");
    }

    @FXML
    void onPendingTestResults(ActionEvent event) {

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
    void onSearch(KeyEvent event) {

    }

    @FXML
    void onUpdateTest(ActionEvent event) {
        Test test = tableView.getSelectionModel().getSelectedItem();
        if (test == null) {
            FXUtils.alert("Selection is Empty! You must select a test to update.", Alert.AlertType.ERROR).show();
            return;
        }

        TestEditorController.test = test;
        UIHandler.open("insert_test.fxml");
    }

    @FXML
    void onViewTest(ActionEvent event) {
        Test test = tableView.getSelectionModel().getSelectedItem();
        if (test == null) {
            FXUtils.alert("Selection is Empty! You must select a sample to view.", Alert.AlertType.ERROR).show();
            return;
        }

//        TestViewerController.test = test;
        UIHandler.open("test_viewer.fxml");
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

}
