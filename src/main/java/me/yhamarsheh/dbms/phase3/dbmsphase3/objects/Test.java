package me.yhamarsheh.dbms.phase3.dbmsphase3.objects;

import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.TestStatus;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.time.LocalDate;

public class Test {

    private int testId;
    private Sample sample;
    private TestStatus testStatus;
    private LocalDate testDate;

    private LocalDate lastModified;

    public Test(int testId, int sampleId, TestStatus testStatus, LocalDate testDate, LocalDate lastModified) {
        this.testId = testId;
        this.sample = GeneralUtils.getSampleById(sampleId);
        this.testStatus = testStatus;
        this.testDate = testDate;
        this.lastModified = lastModified;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public TestStatus getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(TestStatus testStatus) {
        this.testStatus = testStatus;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }
}
