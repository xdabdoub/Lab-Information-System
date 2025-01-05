package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.reports;

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
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Bookmark;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Report;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.User;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.FXUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class ReportsScreenController {

    @FXML
    private BottomNavigationButton bookmarksB;

    @FXML
    private AnchorPane bookmarksDropDown;

    @FXML
    private VBox bookmarksVBox;

    @FXML
    private BottomNavigationButton dashboardB;

    @FXML
    private Label discreteTL;

    @FXML
    private BottomNavigationButton exitAndClose;

    @FXML
    private BottomNavigationButton historyB;

    @FXML
    private TableColumn<Report, Integer> idCol;

    @FXML
    private Label lastWRL;

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
    private Label privacySettingsL;

    @FXML
    private TableColumn<Report, LocalDate> reportDate;

    @FXML
    private BottomNavigationButton reportsB;

    @FXML
    private TableColumn<Report, String> resultCol;

    @FXML
    private BottomNavigationButton samplesB;

    @FXML
    private TextField searchTF;

    @FXML
    private AnchorPane settingsDropDown;

    @FXML
    private BottomNavigationButton showAllBookmarks;

    @FXML
    private TableView<Report> tableView;

    @FXML
    private Label termsL;

    @FXML
    private TableColumn<Report, Integer> testIdCol;

    @FXML
    private BottomNavigationButton testRB;

    @FXML
    private Label totalRL;

    @FXML
    void initialize() {
        idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getReportId()).asObject());
        testIdCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTest().getTestId()).asObject());
        reportDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDate()));
        resultCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getResult()));

        tableView.getItems().addAll(Driver.PRIMARY_MANAGER.getReportsManager().getReports());

        totalRL.setText(Driver.PRIMARY_MANAGER.getReportsManager().getTotalReports() + "");
        discreteTL.setText(Driver.PRIMARY_MANAGER.getReportsManager().getDiscreteTestId() + "");

        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusDays(7);

        Date startDate = Date.valueOf(oneWeekAgo);
        Date endDate = Date.valueOf(today);

        lastWRL.setText(Driver.PRIMARY_MANAGER.getReportsManager().getReportCountWithinDateRange(startDate, endDate) + "");

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
    void onDeletePatient(ActionEvent event) {

    }

    @FXML
    void onHistory(ActionEvent event) {

    }

    @FXML
    void onInsertReport(ActionEvent event) {

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

    }

    @FXML
    void onSamples(ActionEvent event) {
        UIHandler.open("samples.fxml");
    }

    @FXML
    void onSearch(KeyEvent event) {

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
    void onUpdateReport(ActionEvent event) {

    }

    @FXML
    void onViewReport(ActionEvent event) {

    }

}
