package me.yhamarsheh.dbms.phase3.dbmsphase3.objects;

import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.time.LocalDate;


public class Report {

    private int reportId;
    private Test test;
    private LocalDate date;
    private String result;

    private LocalDate lastModified;

    public Report(int reportId, int testId, LocalDate date, String result, LocalDate lastModified) {
        this.reportId = reportId;
        this.test = GeneralUtils.getTestById(testId);
        this.date = date;
        this.result = result;
        this.lastModified = lastModified;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }
}
