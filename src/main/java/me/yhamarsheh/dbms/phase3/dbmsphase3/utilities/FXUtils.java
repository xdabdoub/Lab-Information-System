package me.yhamarsheh.dbms.phase3.dbmsphase3.utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.patient.PatientViewerController;
import me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.sample.SampleViewerController;
import me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.tests.TestViewerController;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.*;

import java.io.IOException;

public class FXUtils {

    public static Alert alert(String context, Alert.AlertType type) {
        final String css = Driver.class.getResource("/css/alerts.css").toExternalForm();

        Alert alert = new Alert(type);
        alert.setContentText(context);

        alert.getDialogPane().getStylesheets().add(css);

        return alert;
    }

    public static void addBookmark(Bookmark<?> bookmark, VBox vBox, int index) {
        try {
            FXMLLoader loader = new FXMLLoader(Driver.class.getResource("BookmarkItem.fxml"));
            HBox bookmarkItem = loader.load();

            Label entityId = (Label) bookmarkItem.lookup("#entityId");
            Label date = (Label) bookmarkItem.lookup("#bookmarkDate");
            Button button = (Button) bookmarkItem.lookup("#viewB");
            ImageView image = (ImageView) bookmarkItem.lookup("#image");
            Rectangle background = (Rectangle) bookmarkItem.lookup("#background");

            switch (bookmark.getEntityType()) {
                case PATIENT -> {
                    Patient bPatient = (Patient) bookmark.getEntity();
                    entityId.setText(bPatient.getName());
                    date.setText(bookmark.getBookmarkDate().toString());
                    image.setImage(new Image("C:\\Users\\yhama\\Documents\\DBMS-Phase3\\src\\main\\resources\\images\\patients_blue.png"));
                    background.setStyle("-fx-stroke-width: 0; -fx-fill: #D2EBF9");
                    button.setOnAction(e -> {
                        PatientViewerController.patient = bPatient;
                        UIHandler.open("patient_viewer.fxml");
                    });
                }
                case SAMPLE -> {
                    Sample bSample = (Sample) bookmark.getEntity();
                    entityId.setText("#" + bSample.getSampleId());
                    date.setText(bookmark.getBookmarkDate().toString());
                    background.setStyle("-fx-stroke-width: 0; -fx-fill: #D2F9DB");
                    image.setImage(new Image("C:\\Users\\yhama\\Documents\\DBMS-Phase3\\src\\main\\resources\\images\\samples_blue.png"));
                    button.setOnAction(e -> {
                        SampleViewerController.sample = bSample;
                        UIHandler.open("sample_viewer.fxml");
                    });
                }
                case TEST -> {
                    Test bTest = (Test) bookmark.getEntity();
                    entityId.setText("#" + bTest.getTestId());
                    date.setText(bookmark.getBookmarkDate().toString());
                    background.setStyle("-fx-stroke-width: 0; -fx-fill: #F9D5D2");
                    image.setImage(new Image("C:\\Users\\yhama\\Documents\\DBMS-Phase3\\src\\main\\resources\\images\\tests_blue.png"));
                    button.setOnAction(e -> {
                        TestViewerController.test = bTest;
                        UIHandler.open("test_viewer.fxml");
                    });
                }
                case INVOICE -> {
                    Invoice bInvoice = (Invoice) bookmark.getEntity();
                    entityId.setText("#" + bInvoice.getInvoiceId());
                    date.setText(bookmark.getBookmarkDate().toString());
                    background.setStyle("-fx-stroke-width: 0; -fx-fill: #F9EED2");
/*                    button.setOnAction(e -> {
                        PatientViewerController.patient = bPatient;
                        UIHandler.open("patient_viewer.fxml");
                    });*/
                }
                case REPORT -> {
                    Report bReport = (Report) bookmark.getEntity();
                    entityId.setText("#" + bReport.getReportId());
                    date.setText(bookmark.getBookmarkDate().toString());
                    background.setStyle("-fx-stroke-width: 0; -fx-fill: #F9EED2");
                    image.setImage(new Image("C:\\Users\\yhama\\Documents\\DBMS-Phase3\\src\\main\\resources\\images\\reports_blue.png"));
                }
            }

            if (index == -1) vBox.getChildren().add(bookmarkItem);
            else vBox.getChildren().add(index, bookmarkItem);
        } catch (IOException e) {
            System.out.println("Couldn't add bookmark with entity Id " + bookmark.getEntityId());
        }
    }

    public static void addRecentActivity(Bookmark<?> bookmark, VBox vBox) {
        try {
            FXMLLoader loader = new FXMLLoader(Driver.class.getResource("BookmarkItem.fxml"));
            HBox bookmarkItem = loader.load();

            Label entityId = (Label) bookmarkItem.lookup("#entityId");
            Label date = (Label) bookmarkItem.lookup("#bookmarkDate");
            Button button = (Button) bookmarkItem.lookup("#viewB");
            ImageView image = (ImageView) bookmarkItem.lookup("#image");
            Rectangle background = (Rectangle) bookmarkItem.lookup("#background");

            switch (bookmark.getEntityType()) {
                case PATIENT -> {
                    Patient bPatient = (Patient) bookmark.getEntity();
                    entityId.setText(bPatient.getName());
                    date.setText(bookmark.getBookmarkDate().toString());
                    image.setImage(new Image("https://i.ibb.co/VBSjXMx/escalator-warning-blue.png"));
                    background.setStyle("-fx-stroke-width: 0; -fx-fill: #D2EBF9");
                    button.setOnAction(e -> {
                        PatientViewerController.patient = bPatient;
                        UIHandler.open("patient_viewer.fxml");
                    });
                }
                case SAMPLE -> {
                    Sample bSample = (Sample) bookmark.getEntity();
                    entityId.setText("#" + bSample.getSampleId());
                    date.setText(bookmark.getBookmarkDate().toString());
                    background.setStyle("-fx-stroke-width: 0; -fx-fill: #D2F9DB");
                    image.setImage(new Image("https://i.ibb.co/6r6mC0N/donut-small-blue.png"));
/*                    button.setOnAction(e -> {
                        PatientViewerController.patient = bPatient;
                        UIHandler.open("patient_viewer.fxml");
                    });*/
                }
                case TEST -> {
                    Test bTest = (Test) bookmark.getEntity();
                    entityId.setText("#" + bTest.getTestId());
                    date.setText(bookmark.getBookmarkDate().toString());
                    background.setStyle("-fx-stroke-width: 0; -fx-fill: #F9D5D2");
                    image.setImage(new Image("https://i.ibb.co/WkVPFht/test-blue.png"));
/*                    button.setOnAction(e -> {
                        PatientViewerController.patient = bPatient;
                        UIHandler.open("patient_viewer.fxml");
                    });*/
                }
                case INVOICE -> {
                    Invoice bInvoice = (Invoice) bookmark.getEntity();
                    entityId.setText("#" + bInvoice.getInvoiceId());
                    date.setText(bookmark.getBookmarkDate().toString());
                    background.setStyle("-fx-stroke-width: 0; -fx-fill: #F9EED2");
/*                    button.setOnAction(e -> {
                        PatientViewerController.patient = bPatient;
                        UIHandler.open("patient_viewer.fxml");
                    });*/
                }
                case REPORT -> {
                    Report bReport = (Report) bookmark.getEntity();
                    entityId.setText("#" + bReport.getReportId());
                    date.setText(bookmark.getBookmarkDate().toString());
                    background.setStyle("-fx-stroke-width: 0; -fx-fill: #F9EED2");
                    image.setImage(new Image("https://i.ibb.co/42Sgshw/report-blue.png"));
                }
            }

            vBox.getChildren().add(bookmarkItem);
        } catch (IOException e) {
            System.out.println("Couldn't add bookmark with entity Id " + bookmark.getEntityId());
        }
    }
}
