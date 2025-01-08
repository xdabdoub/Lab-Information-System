package me.yhamarsheh.dbms.phase3.dbmsphase3.objects;



import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.InvoiceStatus;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.time.LocalDate;

public class Invoice {

    private long invoiceId;
    private LocalDate invoiceDate;
    private double amount;
    private Patient patient;
    private InvoiceStatus invoiceStatus;
    private String description;

    private LocalDate lastModified;

    public Invoice(long invoiceId, LocalDate invoiceDate, double amount, long patientId, InvoiceStatus invoiceStatus, String description,
                   LocalDate lastModified) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.amount = amount;
        this.patient = GeneralUtils.getPatientById(patientId);
        this.invoiceStatus = invoiceStatus;
        this.description = description;
        this.lastModified = lastModified;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
