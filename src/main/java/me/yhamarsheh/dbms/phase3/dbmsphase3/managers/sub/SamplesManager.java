package me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub;

import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.TestStatus;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Patient;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Sample;
import me.yhamarsheh.dbms.phase3.dbmsphase3.storage.Query;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class SamplesManager {

    private LinkedList<Sample> samples;

    public SamplesManager() {
        this.samples = new LinkedList<>();
    }

    public void initializeSamples() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        List<Sample> sampleList = query.fetchAllData("SELECT * FROM SAMPLES", rs -> {
            try {
                return new Sample(
                        rs.getInt("sampleId"),
                        rs.getLong("patientId"),
                        rs.getLong("collectedBy"),
                        rs.getString("sampleType"),
                        rs.getDate("collectionDate").toLocalDate(),
                        (rs.getDate("lastModified") == null ? LocalDate.now() : rs.getDate("lastModified").toLocalDate())
                );
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        });

        samples.addAll(sampleList);
    }

    public void addSample(Sample sample) {
        samples.add(sample);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("INSERT INTO Samples (sampleId, patientId, sampleType, collectionDate, collectedBy, lastModified) VALUES (?,?,?,?,?,?)",
                sample.getSampleId(), sample.getPatient().getId(), sample.getSampleType().toString(), Date.valueOf(sample.getCollectionDate()), sample.getCollectedBy().getId(),
                Date.valueOf(sample.getLastModified()));
    }

    public void deleteSample(Sample sample) {
        samples.remove(sample);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("DELETE FROM samples WHERE sampleId=?",
                sample.getSampleId());
    }

    public void updateSample(Sample sample, long oldId) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("UPDATE samples SET sampleId=?, patientId=?, sampleType=?, collectionDate=?, collectedBy=? WHERE sampleId=?",
                sample.getSampleId(), sample.getPatient().getId(), sample.getSampleType().toString(), Date.valueOf(sample.getCollectionDate()),
                sample.getCollectedBy().getId(),
                oldId);
    }

    public LinkedList<Sample> getSamples() {
        return samples;
    }

    public void setSamples(LinkedList<Sample> samples) {
        this.samples = samples;
    }

    public int getTotalSamples() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get("SELECT COUNT(*) FROM samples;", 1);
        return count != null ? count.intValue() : 0;
    }

    public String getDiscreteSampleType() {
        Query query = new Query(Driver.getSQLConnection().getConnection());

        String result = query.get("SELECT sampleType, COUNT(*) AS count " +
                "FROM samples " +
                "GROUP BY sampleType " +
                "ORDER BY count DESC " +
                "LIMIT 1;", 1);

        if (result == null) {
            System.out.println("No data available.");
            return "-";
        }

        return result;
    }


    public String getDiscretePatientsGender() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        String result = query.get("SELECT p.gender, COUNT(*) AS count " +
                "FROM patients p, samples s " +
                "WHERE p.patientId = s.patientId " +
                "GROUP BY p.gender " +
                "ORDER BY count DESC " +
                "LIMIT 1;", 1);

        if (result == null) {
            System.out.println("No data available.");
            return "-";
        }

        return result;
    }

    public int getSampleTotalTests(Sample sample) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get(
                "SELECT COUNT(t.testId) " +
                        "FROM Tests t " +
                        "INNER JOIN Samples s ON t.sampleId = s.patientId " +
                        "WHERE t.sampleId = ?;",
                1,
                sample.getSampleId()
        );

        return count != null ? count.intValue() : 0;
    }

    public int getSampleTotalTestsByStatus(Sample sample, TestStatus status) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get(
                "SELECT COUNT(t.testId) " +
                        "FROM Tests t " +
                        "INNER JOIN Samples s ON t.sampleId = s.patientId " +
                        "WHERE t.sampleId = ? AND t.testStatus = ?;",
                1,
                sample.getSampleId(), status.toString()
        );

        return count != null ? count.intValue() : 0;
    }

    public int getSampleTotalTestsByNotStatus(Sample sample, TestStatus status) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get(
                "SELECT COUNT(t.testId) " +
                        "FROM Tests t " +
                        "INNER JOIN Samples s ON t.sampleId = s.patientId " +
                        "WHERE t.sampleId = ? AND t.testStatus <> ?;",
                1,
                sample.getSampleId(), status.toString()
        );

        return count != null ? count.intValue() : 0;
    }

}