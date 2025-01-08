package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.reports;

import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
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
import me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.sample.SampleViewerController;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.EntityType;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub.BookmarksManager;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Bookmark;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Report;
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

public class ReportViewerController {

    @FXML
    private BottomNavigationButton bookmarkB;

    @FXML
    private BottomNavigationButton bookmarksB;

    @FXML
    private AnchorPane bookmarksDropDown;

    @FXML
    private VBox bookmarksVBox;


    @FXML
    private TextField reportDate;

    @FXML
    private BottomNavigationButton dashboardB;

    @FXML
    private Label originSample;

    @FXML
    private BottomNavigationButton editInfoB;

    @FXML
    private BottomNavigationButton exitAndClose;

    @FXML
    private BottomNavigationButton historyB;

    @FXML
    private TextField idTF;

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
    private TextField testIdTF;

    @FXML
    private Label result;

    @FXML
    private Label patientNameL;

    @FXML
    private Label originPatient;

    @FXML
    private Label privacySettingsL;

    @FXML
    private VBox recentActivity;

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
    private BottomNavigationButton testB;

    @FXML
    private BottomNavigationButton testRB;

    @FXML
    private Label originTest;

    public static Report report;

    @FXML
    public void initialize() {
        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;

        nameL.setText("Hello, " + Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser().getDoctor().getName());

        BookmarksManager bookmarksManager = user.getBookmarksManager();
        if (bookmarksManager.bookmarkExists(report)) bookmarkB.setGraphic(new ImageView(new Image("https://i.ibb.co/tqmQ9Kg/bookmark-white-filled.png")));


        if (bookmarksManager.getBookmarks().isEmpty()) {
            Label label = new Label("You don't have any bookmarks :(!");
            label.setStyle("-fx-text-fill: #0D1E2F; -fx-font-family: Poppins; -fx-font-size: 18px; -fx-font-weight: 700; -fx-font-style: normal");

            bookmarksVBox.getChildren().add(label);
        } else {

            for (Bookmark<?> bm : bookmarksManager.getBookmarks()) {
                FXUtils.addBookmark(bm, bookmarksVBox, -1);
            }
        }

        if (report == null) return;

        patientNameL.setText("Report No. " + report.getReportId() + " Information");
        idTF.setText(String.valueOf(report.getReportId()));
        testIdTF.setText(report.getTest().getTestId() + "");
        result.setText(report.getResult());
        reportDate.setText(report.getDate().toString());

        originTest.setText(report.getTest().getTestId() + "");
        originSample.setText(report.getTest().getSample().getSampleId() + "");
        originPatient.setText(report.getTest().getSample().getPatient().getId() + "");
    }

    @FXML
    void onBookmark(ActionEvent event) {
        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;

        BookmarksManager bookmarksManager = user.getBookmarksManager();
        if (!bookmarksManager.bookmarkExists(report)) {
            bookmarkB.setGraphic(new ImageView(new Image("https://i.ibb.co/tqmQ9Kg/bookmark-white-filled.png")));

            Bookmark<?> bookmark = new Bookmark<Report>(GeneralUtils.getLastBookmarkId(user) + 1, user.getDoctor().getId(), report.getReportId(),
                    EntityType.REPORT, LocalDate.now());
            bookmarksManager.addBookMark(bookmark);

            FXUtils.addBookmark(bookmark, bookmarksVBox, 0);

            FXUtils.alert("Report #" + report.getReportId() + " has been added to your bookmark list!", Alert.AlertType.INFORMATION);
            return;
        }

        Bookmark<?> bookmark = bookmarksManager.getBookmarkByObject(report);

        bookmarkB.setGraphic(new ImageView(new Image("https://i.ibb.co/fF9hdzQ/bookmark-white-outlined.png")));
        bookmarksManager.deleteBookmark(bookmark);
        FXUtils.alert("Report #" + report.getReportId() + " has been removed from your bookmark list!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onPrint(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        if (printerJob != null) {
            boolean success = printerJob.showPrintDialog(UIHandler.getStage());
            if (success) {
                boolean printed = printerJob.printPage(null);
                if (printed) printerJob.endJob();
            }
        }
    }

    @FXML
    void onShowMore(ActionEvent event) {

    }

    @FXML
    void onOriginSample(ActionEvent event) {
        SampleViewerController.sample = report.getTest().getSample();
        UIHandler.open("sample_viewer.fxml");
    }

    @FXML
    void onDashboard(ActionEvent event) {
        report = null;
        UIHandler.open("dashboard.fxml");
    }

    @FXML
    void onEditInfo(ActionEvent event) {
        ReportEditorController.report = report;
        UIHandler.open("insert_report.fxml");
    }

    @FXML
    void onHistory(ActionEvent event) {
        report = null;

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
}