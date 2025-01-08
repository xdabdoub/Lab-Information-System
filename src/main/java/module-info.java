module me.yhamarsheh.dbms.phase3.dbmsphase3 {
    requires javafx.fxml;
    requires com.gluonhq.charm.glisten;
    requires java.sql;
    requires java.desktop;


    opens me.yhamarsheh.dbms.phase3.dbmsphase3 to javafx.fxml;
    exports me.yhamarsheh.dbms.phase3.dbmsphase3;
    exports me.yhamarsheh.dbms.phase3.dbmsphase3.controllers;
    exports me.yhamarsheh.dbms.phase3.dbmsphase3.managers;
    opens me.yhamarsheh.dbms.phase3.dbmsphase3.controllers to javafx.fxml;
    exports me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.sample;
    opens me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.sample to javafx.fxml;
    exports me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.patient;
    opens me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.patient to javafx.fxml;
    opens me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.tests to javafx.fxml;
    opens me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.reports to javafx.fxml;
    opens me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.invoices to javafx.fxml;
    opens me.yhamarsheh.dbms.phase3.dbmsphase3.controllers.dashboard to javafx.fxml;

}