package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.invoices;

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
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.InvoiceStatus;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.Permission;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.*;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.FXUtils;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

public class InvoiceEditorController {

    @FXML
    private TextField amountTF;

    @FXML
    private BottomNavigationButton bookmarksB;

    @FXML
    private AnchorPane bookmarksDropDown;

    @FXML
    private VBox bookmarksVBox;

    @FXML
    private BottomNavigationButton dashboardB;

    @FXML
    private TextField descriptionTF;

    @FXML
    private BottomNavigationButton exitAndClose1;

    @FXML
    private BottomNavigationButton historyB;

    @FXML
    private TextField idTF;

    @FXML
    private DatePicker invoiceDateTF;

    @FXML
    private ComboBox<InvoiceStatus> invoiceStatusCB;

    @FXML
    private Label logOutL1;

    @FXML
    private Label myAccountL1;

    @FXML
    private Label nameL1;

    @FXML
    private BottomNavigationButton patientB;

    @FXML
    private TextField patientIdTF;

    @FXML
    private Label privacySettingsL1;

    @FXML
    private BottomNavigationButton reportsB;

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
    private BottomNavigationButton testRB1;

    public static Invoice invoice;

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
        invoiceDateTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);
        amountTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);
        patientIdTF.setDisable(user.getPermission() != Permission.ADMINISTRATOR);

        invoiceStatusCB.getItems().addAll(InvoiceStatus.values());

        if (invoice == null) { // INSERT
            idTF.setDisable(false);
            amountTF.setDisable(false);
            patientIdTF.setDisable(false);
            idTF.setText((GeneralUtils.getLastTestId() + 1) + "");
            invoiceDateTF.setValue(LocalDate.now());
            amountTF.setText("0");
            invoiceStatusCB.getSelectionModel().select(InvoiceStatus.DUE);
            return;
        }

        // UPDATE
        idTF.setText(String.valueOf(invoice.getInvoiceId()));
        invoiceDateTF.setValue(invoice.getInvoiceDate());
        amountTF.setText(String.valueOf(invoice.getAmount()));
        patientIdTF.setText(String.valueOf(invoice.getPatient().getId()));
        invoiceStatusCB.getSelectionModel().select(invoice.getInvoiceStatus());
        descriptionTF.setText(invoice.getDescription());
    }


    @FXML
    void onBookmarks(ActionEvent event) {
        if (!bookmarksDropDown.isVisible() && settingsDropDown.isVisible()) settingsDropDown.setVisible(false);
        bookmarksDropDown.setVisible(!bookmarksDropDown.isVisible());
    }

    @FXML
    void onDashboard(ActionEvent event) {
        UIHandler.open("dashboard.fxml");
    }

    @FXML
    void onExitAndClose(ActionEvent event) {
        Optional<ButtonType> response = FXUtils.alert("Are you sure you would like to exit the application?", Alert.AlertType.WARNING).showAndWait();
        if (response.isEmpty()) return;
        if (response.get() != ButtonType.OK) return;

        System.exit(0);
    }

    @FXML
    void onHistory(ActionEvent event) {

    }

    @FXML
    void onInvoices(ActionEvent event) {
        invoice = null;
        UIHandler.open("invoices.fxml");
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
    void onPatients(ActionEvent event) {
        invoice = null;
        UIHandler.open("patients.fxml");
    }

    @FXML
    void onPendingTestResults(ActionEvent event) {
        invoice = null;
        UIHandler.open("tests.fxml");
    }

    @FXML
    void onPrivacy(MouseEvent event) {

    }

    @FXML
    void onReports(ActionEvent event) {
        invoice = null;
        UIHandler.open("reports.fxml");
    }

    @FXML
    void onSamples(ActionEvent event) {
        invoice = null;
        UIHandler.open("samples.fxml");
    }

    @FXML
    void onSettings(ActionEvent event) {
        if (!settingsDropDown.isVisible() && bookmarksDropDown.isVisible()) bookmarksDropDown.setVisible(false);
        settingsDropDown.setVisible(!settingsDropDown.isVisible());

    }

    @FXML
    void onShowAllBookmarks(ActionEvent event) {

    }

    @FXML
    void onSubmission(ActionEvent event) {
        if (!allFilledAndCorrect(idTF, amountTF, patientIdTF, descriptionTF)) {
            FXUtils.alert("Some values seem to be invalid!", Alert.AlertType.WARNING).show();
            return;
        }


        Optional<ButtonType> result = FXUtils.alert("Are you sure you'd like to proceed?", Alert.AlertType.CONFIRMATION).showAndWait();
        if (!result.isPresent()) return;
        if (result.get() != ButtonType.OK) return;


        int id = Integer.parseInt(idTF.getText());
        LocalDate invoiceDate = invoiceDateTF.getValue();
        double amount = Double.parseDouble(amountTF.getText());
        int patientId = Integer.parseInt(patientIdTF.getText());
        String description = descriptionTF.getText();
        LocalDate lastModified = LocalDate.now();
        InvoiceStatus status = invoiceStatusCB.getSelectionModel().getSelectedItem();

        if (invoice == null) { // INSERT
            if(GeneralUtils.getInvoiceById(id)!=null){
                FXUtils.alert("The ID already Exists. You cannot add it.", Alert.AlertType.ERROR).show();

            }
            else{
                Invoice newInvoice = new Invoice(id, invoiceDate, amount, patientId, status, description, lastModified);
                Driver.PRIMARY_MANAGER.getInvoicesManager().addInvoice(newInvoice);
            }

        } else {
            // UPDATE
            long oldId = invoice.getInvoiceId();
            invoice.setInvoiceId(id);
            invoice.setInvoiceDate(invoiceDate);
            invoice.setAmount(amount);
            invoice.setPatient(GeneralUtils.getPatientById(patientId));
            invoice.setDescription(description);
            invoice.setInvoiceStatus(status);
            invoice.setLastModified(lastModified);
            Driver.PRIMARY_MANAGER.getInvoicesManager().updateInvoice(invoice, oldId);
        }

        invoice = null;

        FXUtils.alert("Action was successful!", Alert.AlertType.INFORMATION).show();
        idTF.clear();
        amountTF.clear();
        patientIdTF.clear();
        descriptionTF.clear();
        invoiceDateTF.setValue(LocalDate.now());
        invoiceStatusCB.getSelectionModel().select(InvoiceStatus.DUE);

        UIHandler.open("invoices.fxml");
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

    private boolean allFilledAndCorrect(TextField idTF, TextField amountTF, TextField patientIdTF, TextField descriptionTF) {
        if (idTF.getText().isEmpty() || amountTF.getText().isEmpty() || patientIdTF.getText().isEmpty() || descriptionTF.getText().isEmpty())
            return false;
        try {
            Integer.parseInt(idTF.getText());
            Integer.parseInt(patientIdTF.getText());
            Double.parseDouble(amountTF.getText());
        } catch (NumberFormatException ex) { return false; }

        return true;
    }

}