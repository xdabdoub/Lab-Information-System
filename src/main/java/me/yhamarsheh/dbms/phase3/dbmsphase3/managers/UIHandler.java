package me.yhamarsheh.dbms.phase3.dbmsphase3.managers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.User;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.FXUtils;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UIHandler extends Application {

    private static Stage stage;
    private static UIHandler instance;

    public static void launchApp(String[] args) {
        launch(args);
    }

    public static UIHandler getInstance() {
        return instance;
    }

    @Override
    public void init() {
        instance = this;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader loader = null;
        String filePath = System.getProperty("user.home") + "/AppData/Roaming/Medical Laboratory/cookies.txt";
        File cookiesFile = new File(filePath);

        if (cookiesFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cookiesFile))) {
                String nextSession = reader.readLine();
                String credentials = reader.readLine();

                if (credentials != null) {
                    String[] parts = credentials.split(",");
                    System.out.println("test");

                    String username = GeneralUtils.decodeString(parts[0].getBytes());
                    String password = GeneralUtils.decodeString(parts[1].getBytes());

                    User user = GeneralUtils.getUserByCredentials(username, password);
                    if (user == null) {
                        loader = new FXMLLoader(Driver.class.getResource("login.fxml"));
                        return;
                    }

                    Driver.PRIMARY_MANAGER.getUsersManager().setActiveUser(user);
                    loader = new FXMLLoader(Driver.class.getResource("patients.fxml"));
                    FXUtils.alert("Welcome back, " + user.getDoctor().getName() + "!", Alert.AlertType.INFORMATION).show();
                } else {
                    loader = new FXMLLoader(Driver.class.getResource("login.fxml"));
                }
            } catch (IOException ignored) {
                loader = new FXMLLoader(Driver.class.getResource("login.fxml"));
            }
        } else {
            loader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        }

        Scene scene = new Scene(loader.load());

        stage.setScene(scene);

        stage.show();
    }

    public static void open(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource(fxml));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public static void close() {
        stage.close();
    }

    public static Stage getStage() {
        return stage;
    }
}