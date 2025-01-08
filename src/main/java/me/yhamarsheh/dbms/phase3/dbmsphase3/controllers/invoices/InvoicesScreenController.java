package me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.invoices;

import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import javafx.beans.property.*;
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
import me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.patient.PatientEditorController;
import me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.patient.PatientViewerController;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.InvoiceStatus;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Bookmark;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Invoice;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Patient;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.User;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.FXUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

public class InvoicesScreenController {

    @FXML
    private TableColumn<Invoice, Double> amountCol;

    @FXML
    private BottomNavigationButton bookmarksB;

    @FXML
    private AnchorPane bookmarksDropDown;

    @FXML
    private VBox bookmarksVBox;

    @FXML
    private BottomNavigationButton dashboardB;

    @FXML
    private TableColumn<Invoice, LocalDate> dateCol;

    @FXML
    private BottomNavigationButton deleteB;

    @FXML
    private TableColumn<Invoice, String> descriptionCol;

    @FXML
    private Label dueAmountL;

    @FXML
    private Label dueInvoicesL;

    @FXML
    private BottomNavigationButton exitAndClose;

    @FXML
    private BottomNavigationButton historyB;

    @FXML
    private TableColumn<Invoice, Long> idCol;

    @FXML
    private BottomNavigationButton insertB;

    @FXML
    private TableColumn<Invoice, String> invoiceSCol;

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
    private TableColumn<Invoice, Long> patientIdCol;

    @FXML
    private Label privacySettingsL;

    @FXML
    private BottomNavigationButton reportsB;

    @FXML
    private BottomNavigationButton samplesB;

    @FXML
    private TextField searchTF;

    @FXML
    private AnchorPane settingsDropDown;

    @FXML
    private BottomNavigationButton showAllBookmarks;

    @FXML
    private TableView<Invoice> tableView;

    @FXML
    private Label termsL;

    @FXML
    private BottomNavigationButton testRB;

    @FXML
    private Label totalInvoicesL;

    @FXML
    private BottomNavigationButton updateB;

    @FXML
    private BottomNavigationButton viewB;

    @FXML
    void initialize() {
        idCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getInvoiceId()).asObject());
        patientIdCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getPatient().getId()).asObject());
        invoiceSCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInvoiceStatus().toString()));
        dateCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getInvoiceDate()));
        descriptionCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        amountCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getAmount()).asObject());

        tableView.getItems().addAll(Driver.PRIMARY_MANAGER.getInvoicesManager().getInvoices());

        dueAmountL.setText(String.valueOf(Driver.PRIMARY_MANAGER.getInvoicesManager().getDueAmount()));
        dueInvoicesL.setText(String.valueOf(Driver.PRIMARY_MANAGER.getInvoicesManager().countInvoicesByStatus(InvoiceStatus.DUE)));
        totalInvoicesL.setText(String.valueOf(Driver.PRIMARY_MANAGER.getInvoicesManager().getTotalInvoices()));

        nameL.setText("Hello, " + Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser().getDoctor().getName());

        User user = Driver.PRIMARY_MANAGER.getUsersManager().getActiveUser();
        if (user == null) return;

        if (user.getBookmarksManager().getBookmarks().isEmpty()) {
            Label label = new Label("You don't have any bookmarks :(!");
            label.setStyle("-fx-text-fill: #0D1E2F; -fx-font-family: Poppins; -fx-font-size: 18px; -fx-font-weight: 700; -fx-font-style: normal");

            bookmarksVBox.getChildren().add(label);
            return;
        }

        for (Bookmark<?> bm : user.getBookmarksManager().getBookmarks()) {
            FXUtils.addBookmark(bm, bookmarksVBox, -1);
        }
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
    void onDeleteInvoice(ActionEvent event) {
        Invoice invoice = tableView.getSelectionModel().getSelectedItem();
        if (invoice == null) {
            FXUtils.alert("Selection is Empty! You must select a Invoice to delete.", Alert.AlertType.ERROR).show();
            return;
        }

        Alert alert = FXUtils.alert("Are you sure you'd like to PERMANENTLY delete this invoice?", Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty()) return;
        if (result.get() != ButtonType.OK) return;

        Driver.PRIMARY_MANAGER.getInvoicesManager().deleteInvoice(invoice);;
        tableView.getItems().remove(invoice);
        tableView.refresh();

        FXUtils.alert("Successfully deleted this Invoice!", Alert.AlertType.INFORMATION).show();
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
    void onInsertInvoice(ActionEvent event) {
        UIHandler.open("insert_invoice.fxml");
    }

    @FXML
    void onInvoices(ActionEvent event) {
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
        UIHandler.open("patients.fxml");
    }

    @FXML
    void onPendingTestResults(ActionEvent event) {
        UIHandler.open("tests.fxml");

    }

    @FXML
    void onPrivacy(MouseEvent event) {

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
        ObservableList<Invoice> filteredInvoices = FXCollections.observableArrayList();

        if (searchTF.getText().isEmpty()) {
            filteredInvoices.addAll(Driver.PRIMARY_MANAGER.getInvoicesManager().getInvoices());
        } else {
            getInvoiceByPartOfId(filteredInvoices, searchTF.getText());
        }

        tableView.setItems(filteredInvoices);
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
    void onUpdateInvoice(ActionEvent event) {
        Invoice invoice = tableView.getSelectionModel().getSelectedItem();
        if (invoice == null) {
            FXUtils.alert("Selection is Empty! You must select a Invoice to delete.", Alert.AlertType.ERROR).show();
            return;
        }

        InvoiceEditorController.invoice=invoice;
        UIHandler.open("insert_invoice.fxml");
    }

    @FXML
    void onViewInvoice(ActionEvent event) {
        Invoice invoice = tableView.getSelectionModel().getSelectedItem();
        if (invoice == null) {
            FXUtils.alert("Selection is Empty! You must select a Invoice to view.", Alert.AlertType.ERROR).show();
            return;
        }

        InvoiceViewerController.invoice = invoice;
        UIHandler.open("invoice_viewer.fxml");
    }

    private void getInvoiceByPartOfId(ObservableList<Invoice> filteredInvoices, String partOfId) {
        for (Invoice invoice : Driver.PRIMARY_MANAGER.getInvoicesManager().getInvoices()) {
            if (String.valueOf(invoice.getInvoiceId()).startsWith(partOfId) ||
                    invoice.getInvoiceStatus().toString().toLowerCase().startsWith(partOfId.toLowerCase())) {
                filteredInvoices.add(invoice);
            }
        }
    }


}
