package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.tests;

import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.EntityType;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.TestStatus;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub.BookmarksManager;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Bookmark;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Sample;
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

public class TestViewerController {

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
    private TextField idTF;

    @FXML
    private Label latestReportL;

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
    private Label patientNameL;

    @FXML
    private Label privacySettingsL;

    @FXML
    private VBox recentActivity;

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
    private Label termsL;

    @FXML
    private TextField testDate;

    @FXML
    private BottomNavigationButton testRB1;

    @FXML
    private TextField testStatusTF;

    @FXML
    private Label totalReports;

    @FXML
    private BottomNavigationButton bookmarkB;

    public static Test test;

    @FXML
    void initialize() {
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

        if (test == null) return;

        patientNameL.setText("Test No. " + test.getTestId() + " Information");
        idTF.setText(String.valueOf(test.getTestId()));
        sampleIdTF.setText(test.getSample().getSampleId() + "");
        testStatusTF.setText(test.getTestStatus().toString());
        testDate.setText(test.getTestDate().toString());

        totalReports.setText(Driver.PRIMARY_MANAGER.getTestsManager().getTestTotalReports(test) + "");
        latestReportL.setText(Driver.PRIMARY_MANAGER.getTestsManager().getTestTotalReports(test) + "");
        if (bookmarksManager.bookmarkExists(test)) bookmarkB.setGraphic(new ImageView(new Image("https://i.ibb.co/tqmQ9Kg/bookmark-white-filled.png")));

    }

    @FXML
    void onBookmark(ActionEvent event) {
        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;

        BookmarksManager bookmarksManager = user.getBookmarksManager();
        if (!bookmarksManager.bookmarkExists(test)) {
            bookmarkB.setGraphic(new ImageView(new javafx.scene.image.Image("https://i.ibb.co/tqmQ9Kg/bookmark-white-filled.png")));

            Bookmark<?> bookmark = new Bookmark<Sample>(GeneralUtils.getLastBookmarkId(user) + 1, user.getDoctor().getId(), test.getTestId(),
                    EntityType.TEST, LocalDate.now());
            bookmarksManager.addBookMark(bookmark);

            FXUtils.addBookmark(bookmark, bookmarksVBox, 0);

            FXUtils.alert("Test #" + test.getTestId() + " has been added to your bookmark list!", Alert.AlertType.INFORMATION);
            return;
        }

        Bookmark<?> bookmark = bookmarksManager.getBookmarkByObject(test);
        bookmarkB.setGraphic(new ImageView(new Image("https://i.ibb.co/fF9hdzQ/bookmark-white-outlined.png")));
        bookmarksManager.deleteBookmark(bookmark);

        FXUtils.alert("Test #" + test.getTestId() + " has been removed from your bookmark list!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onDashboard(ActionEvent event) {

    }

    @FXML
    void onEditInfo(ActionEvent event) {

    }

    @FXML
    void onHistory(ActionEvent event) {

    }

    @FXML
    void onOriginSample(ActionEvent event) {

    }

    @FXML
    void onPatients(ActionEvent event) {

    }

    @FXML
    void onPendingTestResults(ActionEvent event) {

    }

    @FXML
    void onReport(ActionEvent event) {

    }

    @FXML
    void onReports(ActionEvent event) {
        UIHandler.open("reports.fxml");
    }

    @FXML
    void onSamples(ActionEvent event) {

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
