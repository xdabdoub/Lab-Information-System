package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.dashboard;

import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class DashboardScreenController {

    @FXML
    private BottomNavigationButton bookmarksB;

    @FXML
    private AnchorPane bookmarksDropDown;

    @FXML
    private VBox bookmarksVBox;

    @FXML
    private VBox chartVBox;

    @FXML
    private BottomNavigationButton dashboardB;

    @FXML
    private BottomNavigationButton exitAndClose;

    @FXML
    private ComboBox<?> filterCB;

    @FXML
    private BottomNavigationButton historyB;

    @FXML
    private Label logOutL;

    @FXML
    private Label myAccountL;

    @FXML
    private Label nameL;

    @FXML
    private Label nameL1;

    @FXML
    private BottomNavigationButton patientB;

    @FXML
    private Label patientsPerc;

    @FXML
    private Label privacySettingsL;

    @FXML
    private BottomNavigationButton reportsB;

    @FXML
    private BottomNavigationButton samplesB;

    @FXML
    private Label samplesPerc;

    @FXML
    private AnchorPane settingsDropDown;

    @FXML
    private BottomNavigationButton showAllBookmarks;

    @FXML
    private Label termsL;

    @FXML
    private BottomNavigationButton testRB;

    @FXML
    private Label totalPatients;

    @FXML
    private Label totalSamples;

    @FXML
    void initialize() {
    }

    @FXML
    void onBookmarks(ActionEvent event) {

    }

    @FXML
    void onDashboard(ActionEvent event) {

    }

    @FXML
    void onExitAndClose(ActionEvent event) {

    }

    @FXML
    void onHistory(ActionEvent event) {

    }

    @FXML
    void onLogOut(MouseEvent event) {

    }

    @FXML
    void onMyAccount(MouseEvent event) {

    }

    @FXML
    void onPatients(ActionEvent event) {

    }

    @FXML
    void onPendingTestResults(ActionEvent event) {

    }

    @FXML
    void onPrivacy(MouseEvent event) {

    }

    @FXML
    void onReports(ActionEvent event) {

    }

    @FXML
    void onSamples(ActionEvent event) {

    }

    @FXML
    void onSettings(ActionEvent event) {

    }

    @FXML
    void onShowAllBookmarks(ActionEvent event) {

    }

    @FXML
    void onTerms(MouseEvent event) {

    }
}
