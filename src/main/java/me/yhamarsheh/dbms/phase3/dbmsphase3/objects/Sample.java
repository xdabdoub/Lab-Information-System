package me.yhamarsheh.dbms.phase3.dbmsphase3.objects;

import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.SampleType;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.time.LocalDate;

public class Sample {

    private int sampleId;
    private Patient patient;
    private SampleType sampleType;
    private LocalDate collectionDate;
    private Doctor collectedBy;

    private LocalDate lastModified;

    public Sample(int sampleId, long patientId, long collectedById, String sampleType, LocalDate collectionDate, LocalDate lastModified) {
        this.sampleId = sampleId;
        this.patient = GeneralUtils.getPatientById(patientId);
        this.collectedBy = GeneralUtils.getDoctorById(collectedById);
        this.sampleType = SampleType.valueOf(sampleType);
        this.collectionDate = collectionDate;
        this.lastModified = lastModified;
    }

    public int getSampleId() {
        return sampleId;
    }

    public void setSampleId(int sampleId) {
        this.sampleId = sampleId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public LocalDate getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(LocalDate collectionDate) {
        this.collectionDate = collectionDate;
    }

    public Doctor getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(Doctor collectedBy) {
        this.collectedBy = collectedBy;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }
}