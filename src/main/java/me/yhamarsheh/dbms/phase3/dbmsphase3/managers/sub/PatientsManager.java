package me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub;

import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.InvoiceStatus;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Patient;
import me.yhamarsheh.dbms.phase3.dbmsphase3.storage.Query;

import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PatientsManager {

    private LinkedList<Patient> patients;

    public PatientsManager() {
        this.patients = new LinkedList<>();
    }

    public void initializePatients() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        List<Patient> patientsList = query.fetchAllData("SELECT * FROM Patients", rs -> {
            try {
                return new Patient(
                        rs.getLong("patientId"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getDate("dateOfBirth").toLocalDate(),
                        rs.getString("phone"),
                        rs.getString("address")
                );
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        });

        patients.addAll(patientsList);
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("INSERT INTO Patients (patientId, name, gender, dateOfBirth, phone, address) VALUES (?,?,?,?,?,?)",
                patient.getId(), patient.getName(), patient.getGender(), Date.valueOf(patient.getDateOfBirth()), patient.getPhone(), patient.getAddress());
    }

    public void deletePatient(Patient patient) {
        patients.remove(patient);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("DELETE FROM Patients WHERE patientId=?",
                patient.getId());
    }

    public void updatePatient(Patient patient, long oldId) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("UPDATE Patients SET patientId=?, name=?, gender=?, dateOfBirth=?, phone=?, address=? WHERE patientId=?",
                patient.getId(), patient.getName(), patient.getGender(), Date.valueOf(patient.getDateOfBirth()), patient.getPhone(), patient.getAddress(),
                oldId);
    }

    public LinkedList<Patient> getPatients() {
        return patients;
    }

    public void setPatients(LinkedList<Patient> patients) {
        this.patients = patients;
    }

    public int getTotalPatients() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get("SELECT COUNT(*) FROM Patients;", 1);
        return count != null ? count.intValue() : 0;
    }

    public String getDiscreteGender() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        String gender = query.get("SELECT gender, COUNT(*) AS count FROM Patients GROUP BY gender ORDER BY count DESC LIMIT 1;", 1);
        if (gender == null || gender.isEmpty()) gender = "-";

        return gender;
    }

    public int getPatientTotalSamples(Patient patient) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get(
                "SELECT COUNT(s.sampleId) " +
                        "FROM samples s " +
                        "INNER JOIN patients p ON s.patientId = p.patientId " +
                        "WHERE s.patientId = ?;",
                1,
                patient.getId()
        );

        return count != null ? count.intValue() : 0;
    }

    public int getPatientTotalInvoices(Patient patient) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get(
                "SELECT COUNT(i.invoiceId) " +
                        "FROM invoices i " +
                        "INNER JOIN patients p ON i.patientId = p.patientId " +
                        "WHERE i.patientId = ?;",
                1,
                patient.getId()
        );

        return count != null ? count.intValue() : 0;
    }

    public int getPatientTotalDueInvoices(Patient patient) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get(
                "SELECT COUNT(i.invoiceId) " +
                        "FROM invoices i " +
                        "INNER JOIN patients p ON i.patientId = p.patientId " +
                        "WHERE i.patientId = ? AND i.invoiceStatus = ?;",
                1,
                patient.getId(),
                InvoiceStatus.DUE.toString()
        );

        return count != null ? count.intValue() : 0;
    }

    public int getPatientDueAmount(Patient patient) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long totalAmount = query.get(
                "SELECT SUM(i.amount) " +
                        "FROM invoices i " +
                        "INNER JOIN patients p ON i.patientId = p.patientId " +
                        "WHERE i.patientId = ? AND i.invoiceStatus = ? ;",
                1,
                patient.getId(),
                InvoiceStatus.DUE.toString()
        );

        return totalAmount != null ? totalAmount.intValue() : 0;
    }
}
