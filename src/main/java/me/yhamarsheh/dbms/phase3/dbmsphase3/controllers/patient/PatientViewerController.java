package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.patient;

import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.sample.SampleEditorController;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.EntityType;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub.BookmarksManager;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Bookmark;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Patient;
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

public class PatientViewerController {

    @FXML
    private TextField addressTF;

    @FXML
    private BottomNavigationButton bookmarkB;

    @FXML
    private BottomNavigationButton callB;

    @FXML
    private BottomNavigationButton dashboardB;

    @FXML
    private TextField dateOfBirthTF;

    @FXML
    private Label dueAmountL;

    @FXML
    private Label dueInvoicesL;

    @FXML
    private BottomNavigationButton editInfoB;

    @FXML
    private TextField genderTF;

    @FXML
    private BottomNavigationButton historyB;

    @FXML
    private TextField idTF;

    @FXML
    private TextField nameTF;

    @FXML
    private BottomNavigationButton patientB;

    @FXML
    private Label patientNameL;

    @FXML
    private TextField phoneTF;

    @FXML
    private BottomNavigationButton reportsB;

    @FXML
    private BottomNavigationButton sampleB;

    @FXML
    private BottomNavigationButton samplesB;

    @FXML
    private BottomNavigationButton testRB;

    @FXML
    private Label totalInvoicesL;

    @FXML
    private Label totalSamplesL;

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
        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;

        nameL.setText("Hello, " + Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser().getDoctor().getName());

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

        if (patient == null) return;

        patientNameL.setText(patient.getName() + "'s Information");
        idTF.setText(String.valueOf(patient.getId()));
        nameTF.setText(patient.getName());
        addressTF.setText(patient.getAddress());
        phoneTF.setText(patient.getPhone());
        dateOfBirthTF.setText(patient.getDateOfBirth().toString() + " | " + GeneralUtils.dateDifference(patient.getDateOfBirth(), LocalDate.now()) + " years old");
        genderTF.setText(patient.getGender());

        totalSamplesL.setText(Driver.PRIMARY_MANAGER.getPatientsManager().getPatientTotalSamples(patient) + "");
        totalInvoicesL.setText(Driver.PRIMARY_MANAGER.getPatientsManager().getPatientTotalInvoices(patient) + "");
        dueInvoicesL.setText(Driver.PRIMARY_MANAGER.getPatientsManager().getPatientTotalDueInvoices(patient) + "");
        dueAmountL.setText(Driver.PRIMARY_MANAGER.getPatientsManager().getPatientDueAmount(patient) + " NIS");

        if (bookmarksManager.bookmarkExists(patient)) bookmarkB.setGraphic(new ImageView(new Image("https://i.ibb.co/tqmQ9Kg/bookmark-white-filled.png")));
    }

    @FXML
    void onDashboard(ActionEvent event) {
        patient = null;
        UIHandler.open("dashboard.fxml");
    }

    @FXML
    void onHistory(ActionEvent event) {
        patient = null;
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
    void onCall(ActionEvent event) {
        if (!Desktop.isDesktopSupported()) {
            FXUtils.alert("Your operating system does not support outgoing phone calls.", Alert.AlertType.INFORMATION).show();
            return;
        }

        try {
            Desktop.getDesktop().browse(new URI("tel:+972" + patient.getPhone()));
        } catch (Exception e) {
            FXUtils.alert("Your operating system does not support outgoing phone calls.", Alert.AlertType.INFORMATION).show();
        }
    }

    @FXML
    void onBookmark(ActionEvent event) {
        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;

        BookmarksManager bookmarksManager = user.getBookmarksManager();
        if (!bookmarksManager.bookmarkExists(patient)) {
            bookmarkB.setGraphic(new ImageView(new Image("https://i.ibb.co/tqmQ9Kg/bookmark-white-filled.png")));

            Bookmark<?> bookmark = new Bookmark<Sample>(GeneralUtils.getLastBookmarkId(user) + 1, user.getDoctor().getId(), patient.getId(),
                    EntityType.PATIENT, LocalDate.now());

            bookmarksManager.addBookMark(bookmark);

            FXUtils.addBookmark(bookmark, bookmarksVBox, 0);
            FXUtils.alert("Patient #" + patient.getId() + " has been added to your bookmark list!", Alert.AlertType.INFORMATION);
            return;
        }

        bookmarkB.setGraphic(new ImageView(new Image("https://i.ibb.co/fF9hdzQ/bookmark-white-outlined.png")));
        Bookmark<?> bookmark = bookmarksManager.getBookmarkByObject(patient);

        bookmarksManager.deleteBookmark(bookmark);
        FXUtils.alert("Patient #" + patient.getId() + " has been removed from your bookmark list!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onEditInfo(ActionEvent event) {
        PatientEditorController.patient = patient;
        UIHandler.open("insert_patient.fxml");
    }

    @FXML
    void onSample(ActionEvent event) {
        UIHandler.open("insert_sample.fxml");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("insert_sample.fxml"));
            Parent root = loader.load();
            SampleEditorController controller = loader.getController();

            controller.patientIdTF.setText(String.valueOf(patient.getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        patient = null;
        UIHandler.open("invoices.fxml");
    }

}
