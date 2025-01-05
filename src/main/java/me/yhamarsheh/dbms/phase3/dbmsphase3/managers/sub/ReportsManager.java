package me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub;

import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Report;
import me.yhamarsheh.dbms.phase3.dbmsphase3.storage.Query;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ReportsManager {
    private LinkedList<Report> reports;

    public ReportsManager() {
        this.reports = new LinkedList<>();
    }

    public void initializeReports() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        List<Report> reportsList = query.fetchAllData("SELECT * FROM Reports", rs -> {
            try {
                return new Report(
                        rs.getInt("reportId"),
                        rs.getInt("testId"),
                        rs.getDate("reportDate").toLocalDate(),
                        rs.getString("result"),
                        rs.getDate("lastModified").toLocalDate()

                );
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        });

        reports.addAll(reportsList);
    }

    public void addReports(Report report) {
        reports.add(report);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("INSERT INTO Reports (reportId, testId, reportDate, result) VALUES (?,?,?,?)",
                report.getReportId(), report.getTest(), report.getDate(), report.getResult());

    }

    public void deleteReports(Report report) {
        reports.remove(report);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("DELETE FROM Reports WHERE reportId=?",
                report.getReportId());
    }

    public void updateReports(Report report, long oldId) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("UPDATE Reports SET   result=? WHERE reportId=?",
                report.getReportId(),
                oldId);
    }

    public LinkedList<Report> getReports() {
        return reports;
    }

    public void setReports(LinkedList<Report> reports) {
        this.reports = reports;
    }

    public int getTotalReports() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get("SELECT COUNT(*) FROM Reports;", 1);
        return count != null ? count.intValue() : 0;
    }

    public List<Report> getReportsWithinDateRange(Date startDate, Date endDate) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        return query.fetchAllData("SELECT reportId FROM Reports WHERE reportDate BETWEEN ? AND ?", rs -> {
            try {
                return GeneralUtils.getReportById(rs.getInt("reportId"));
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        }, startDate, endDate);
    }

    public int getReportCountWithinDateRange(Date startDate, Date endDate) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Integer count = query.get("SELECT COUNT(*) AS reportCount FROM Reports WHERE reportDate BETWEEN ? AND ?",1, startDate, endDate);
        return count != null ? count : 0;
    }

        public Report getLatestReport() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Integer reportId = query.get("SELECT reportId FROM Reports ORDER BY reportDate DESC LIMIT 1;",1);

        return GeneralUtils.getReportById(reportId != null ? reportId : 0);
    }

    public int getDiscreteTestId() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Integer gender = query.get("SELECT testId, COUNT(*) AS count FROM Reports GROUP BY testId ORDER BY count DESC LIMIT 1;", 1);

        return gender != null ? gender : 0;
    }


/*    public List<String> getReportCountsByResult() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        return query.fetchAllData("SELECT result, COUNT(*) AS count FROM Reports GROUP BY result ORDER BY count DESC", rs -> {
            try {
                return "Result: " + rs.getString("result") + ", Count: " + rs.getInt("count");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        });
    }*/
}




