package me.yhamarsheh.dbms.phase3.dbmsphase3.managers;

import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub.*;

public class PrimaryManager {

    private final PatientsManager patientsManager;
    private final SamplesManager samplesManager;
    private final UsersManager usersManager;
    private final DoctorsManager doctorsManager;
    private final InvoicesManager invoicesManager;
    private final TestsManager testsManager;
    private final ReportsManager reportsManager;

    public PrimaryManager() {
        this.patientsManager = new PatientsManager();
        this.samplesManager = new SamplesManager();
        this.usersManager = new UsersManager();
        this.doctorsManager = new DoctorsManager();
        this.invoicesManager = new InvoicesManager();
        this.testsManager = new TestsManager();
        this.reportsManager = new ReportsManager();
    }

    public PatientsManager getPatientsManager() {
        return patientsManager;
    }

    public SamplesManager getSamplesManager() {
        return samplesManager;
    }

    public UsersManager getUsersManager() {
        return usersManager;
    }

    public DoctorsManager getDoctorsManager() {
        return doctorsManager;
    }

    public InvoicesManager getInvoicesManager() {
        return invoicesManager;
    }

    public TestsManager getTestsManager() {
        return testsManager;
    }

    public ReportsManager getReportsManager() {
        return reportsManager;
    }
}
