package me.yhamarsheh.dbms.phase3.dbmsphase3;

import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.Permission;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.PrimaryManager;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.UIHandler;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.User;
import me.yhamarsheh.dbms.phase3.dbmsphase3.storage.SQLConnection;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.sql.SQLException;

public class Driver {

    public static final PrimaryManager PRIMARY_MANAGER = new PrimaryManager();
    private static UIHandler uiHandler;
    private static SQLConnection sqlConnection;

    public static void main(String[] args) throws InterruptedException {
        uiHandler = new UIHandler();

        try {
            sqlConnection = new SQLConnection("localhost", 3306, "root", "");
            sqlConnection.connect();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Could not connect to database. More Info: " + e.getMessage());
            return;
        }

        PRIMARY_MANAGER.getPatientsManager().initializePatients();
        PRIMARY_MANAGER.getDoctorsManager().initializeDoctors();
        PRIMARY_MANAGER.getSamplesManager().initializeSamples();
        PRIMARY_MANAGER.getUsersManager().initializeUsers();

        PRIMARY_MANAGER.getUsersManager().addUser(new User(GeneralUtils.encodeString("yazan.h.22"), GeneralUtils.encodeString("yazan@123/"), 1, Permission.ADMINISTRATOR));

        UIHandler.launchApp(args);
    }

    public static SQLConnection getSQLConnection() {
        return sqlConnection;
    }
}