package me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub;

import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.TestStatus;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Test;
import me.yhamarsheh.dbms.phase3.dbmsphase3.storage.Query;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class TestsManager {
    private LinkedList<Test> tests;

    public TestsManager() {
        this.tests = new LinkedList<>();
    }

    public void initializeTests() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        List<Test> reportsList = query.fetchAllData("SELECT * FROM Tests", rs -> {
            try {
                return new Test(
                        rs.getInt("testId"),
                        rs.getInt("sampleId"),
                        TestStatus.valueOf(rs.getString("testStatus")),
                        rs.getDate("testDate").toLocalDate(),
                        rs.getDate("lastModified").toLocalDate()
                );
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        });

        tests.addAll(reportsList);
    }

    public void addTest(Test test) {
        tests.add(test);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("INSERT INTO Tests (testId, sample, testStatus, testDate) VALUES (?,?,?,?)",
                test.getTestId(),test.getSample(),test.getTestStatus(),test.getTestDate());


    }

    public void deleteTest(Test test) {
        tests.remove(test);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("DELETE FROM Tests WHERE testId=?",
                test.getTestId());
    }

    public void updateTest(Test test, long oldId) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("UPDATE Tests SET   testStatus=? WHERE testId=?",
                test.getTestStatus(),
                oldId);
    }

    public LinkedList<Test> getTests() {
        return tests;
    }

    public void setTests(LinkedList<Test> tests) {
        this.tests = tests;
    }

    public int getTotalTests() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get("SELECT COUNT(*) FROM Tests;", 1);
        return count != null ? count.intValue() : 0;
    }

    public List<Test> getTestsByDate(LocalDate testDate) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        return query.fetchAllData("SELECT * FROM Tests WHERE testDate = ?", rs -> {
            try {
                return GeneralUtils.getTestById(rs.getInt("testId"));
            } catch (SQLException ex) {
                System.err.println("Error fetching tests by date: " + ex.getMessage());
                return null;
            }
        }, Date.valueOf(testDate));
    }

    public Test getLastTest() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        return query.get("SELECT * FROM Tests ORDER BY testDate DESC LIMIT 1", 1);
    }

    public List<Test> getLastTestsByDate(LocalDate testDate, int limit) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        return query.fetchAllData(
                "SELECT * FROM Tests WHERE testDate = ? ORDER BY testDate DESC LIMIT ?",
                rs -> {
                    try {
                        return GeneralUtils.getTestById(rs.getInt("testId"));
                    } catch (SQLException ex) {
                        System.err.println("Error fetching tests by date: " + ex.getMessage());
                        return null;
                    }
                },
                Date.valueOf(testDate), limit  // تمرير testDate و limit كمعاملات
        );
    }

    public List<Test> getTestsBySampleId(int sampleId) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        return query.fetchAllData("SELECT * FROM Tests WHERE sampleId = ?", rs -> {
            try {
                return GeneralUtils.getTestById(rs.getInt("testId"));
            } catch (SQLException ex) {
                System.err.println("Error fetching tests by sample ID: " + ex.getMessage());
                return null;
            }
        }, sampleId);
    }

    public int getTestsWithinDateRange(Date startDate, Date endDate) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get("SELECT COUNT(*) AS testsCount FROM Tests WHERE testDate BETWEEN ? AND ?",1, startDate, endDate);
        return count != null ? count.intValue() : 0;
    }

    public String getDiscreteTestStatus() {
        Query query = new Query(Driver.getSQLConnection().getConnection());

        String result = query.get("SELECT testStatus, COUNT(*) AS count " +
                "FROM Tests " +
                "GROUP BY testStatus " +
                "ORDER BY count DESC " +
                "LIMIT 1;", 1);

        if (result == null) {
            System.out.println("No data available.");
            return "-";
        }

        return result;
    }

    public int getTestTotalReports(Test test) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get(
                "SELECT COUNT(r.reportId) " +
                        "FROM Reports r " +
                        "INNER JOIN Tests t ON r.testId = t.testId " +
                        "WHERE r.testId = ?;",
                1,
                test.getTestId()
        );

        return count != null ? count.intValue() : 0;
    }


/*
    public int countTestsByStatus(TestStatus status) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get("SELECT COUNT(*) FROM Tests WHERE testStatus = ?",
                1, status.toString());
        return count != null ? count.intValue() : 0;
    }
*/
}
