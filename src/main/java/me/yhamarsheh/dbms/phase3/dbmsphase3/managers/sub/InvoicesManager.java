package me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub;

import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.InvoiceStatus;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Invoice;
import me.yhamarsheh.dbms.phase3.dbmsphase3.storage.Query;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoicesManager {

    private ArrayList<Invoice> invoices;

    public InvoicesManager() {
        this.invoices = new ArrayList<>();
    }

    public void initializeInvoices() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        List<Invoice> invoiceList = query.fetchAllData("SELECT * FROM Invoices", rs -> {
            try {
                return new Invoice(
                        rs.getLong("invoiceId"),
                        rs.getDate("invoiceDate").toLocalDate(),
                        rs.getDouble("amount"),
                        rs.getInt("patientId"),
                        InvoiceStatus.valueOf(rs.getString("invoiceStatus")),
                        rs.getDate("lastModified").toLocalDate()
                );
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        });

        invoices.addAll(invoiceList);
    }

    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("INSERT INTO Invoices (invoiceId, invoiceDate, amount, patient,invoiceStatus) VALUES (?,?,?,?,?)",
                invoice.getInvoiceId(),invoice.getInvoiceDate(),invoice.getAmount(),invoice.getPatient(),invoice.getInvoiceStatus());
    }

    public void deleteInvoice(Invoice invoice) {
        invoices.remove(invoice);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("DELETE FROM Invoices WHERE invoiceId=?",
                invoice.getInvoiceId());
    }

    public void updateInvoice(Invoice invoice, long oldId) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("UPDATE Invoices SET invoiceStatus=? WHERE invoiceId=?",
                invoice.getInvoiceId(),invoice.getInvoiceDate(),invoice.getAmount(),invoice.getPatient(),invoice.getInvoiceStatus(),
                oldId);
    }

    public ArrayList<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(ArrayList<Invoice> invoices) {
        this.invoices = invoices;
    }

    public int getTotalInvoices() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get("SELECT COUNT(*) FROM Invoices;", 1);
        return count != null ? count.intValue() : 0;
    }
    public List<Invoice> getInvoicesByStatus(InvoiceStatus status) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        return query.fetchAllData("SELECT * FROM Invoices WHERE invoiceStatus = ?", rs -> {
            try {
                return GeneralUtils.getInvoiceById(rs.getLong("invoiceId"));
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        }, status.toString());
    }

    public List<Invoice> getInvoicesByPatient(int patientId) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        return query.fetchAllData("SELECT * FROM Invoices WHERE patientId = ?", rs -> {
            try {
                return GeneralUtils.getInvoiceById(rs.getLong("invoiceId"));
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        }, patientId);
    }

    public int countInvoicesByStatus(InvoiceStatus status) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get("SELECT COUNT(*) FROM Invoices WHERE invoiceStatus = ?", 1,status.toString());
        return count != null ? count.intValue() : 0;
    }

}
