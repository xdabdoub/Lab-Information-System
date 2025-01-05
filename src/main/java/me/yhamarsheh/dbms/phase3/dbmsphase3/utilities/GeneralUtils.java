package me.yhamarsheh.dbms.phase3.dbmsphase3.utilities;

import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class GeneralUtils {

    public static Patient getPatientById(long Id) {
       return Driver.PRIMARY_MANAGER.getPatientsManager().getPatients().stream().filter(patient -> patient.getId() == Id).findFirst().orElse(null);
    }

    public static Doctor getDoctorById(long Id) {
        return Driver.PRIMARY_MANAGER.getDoctorsManager().getDoctors().stream().filter(doctor -> doctor.getId() == Id).findFirst().orElse(null);
    }

    public static Report getReportById(int Id) {
        return Driver.PRIMARY_MANAGER.getReportsManager().getReports().stream().filter(report -> report.getReportId() == Id).findFirst().orElse(null);
    }

    public static Sample getSampleById(int Id) {
        return Driver.PRIMARY_MANAGER.getSamplesManager().getSamples().stream().filter(sample -> sample.getSampleId() == Id).findFirst().orElse(null);
    }

    public static Test getTestById(int Id) {
        return Driver.PRIMARY_MANAGER.getTestsManager().getTests().stream().filter(test -> test.getTestId() == Id).findFirst().orElse(null);
    }

    public static int getLastSampleId() {
        if (Driver.PRIMARY_MANAGER.getSamplesManager().getSamples().isEmpty()) return 0;
        return Driver.PRIMARY_MANAGER.getSamplesManager().getSamples().getLast().getSampleId();
    }

    public static int getLastBookmarkId(User user) {
        if (user.getBookmarksManager().getBookmarks().isEmpty()) return 0;
        return user.getBookmarksManager().getBookmarks().get(user.getBookmarksManager().getBookmarks().size() - 1).getBookmarkId();
    }


    public static int getLastTestId() {
        if (Driver.PRIMARY_MANAGER.getTestsManager().getTests().isEmpty()) return 0;
        return Driver.PRIMARY_MANAGER.getTestsManager().getTests().getLast().getTestId();
    }

    public static User getUserByCredentials(String username, String password) {
/*        byte[] usernameEncoded = encodeString(username);
        byte[] passwordEncoded = encodeString(password);*/

        return Driver.PRIMARY_MANAGER.getUsersManager().getUsers().stream().filter(user -> decodeString(user.getUsername()).equalsIgnoreCase(username)
                && decodeString(user.getPassword()).equalsIgnoreCase(password)).findFirst().orElse(null);
    }

    public static User getUserByDoctorId(long doctorId) {
        return Driver.PRIMARY_MANAGER.getUsersManager().getUsers().stream().filter(user -> user.getDoctor().getId() == doctorId).findFirst().orElse(null);
    }

    public static Invoice getInvoiceById(long Id) {
        return Driver.PRIMARY_MANAGER.getInvoicesManager().getInvoices().stream().filter(invoice -> invoice.getInvoiceId() == Id).findFirst().orElse(null);
    }

    public static byte[] encodeString(String str) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(str.getBytes());
    }

    public static String decodeString(byte[] arr) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(arr));
    }

    public static int dateDifference(LocalDate date1, LocalDate date2) {
        return (int) ChronoUnit.YEARS.between(date1, date2);
    }
}
