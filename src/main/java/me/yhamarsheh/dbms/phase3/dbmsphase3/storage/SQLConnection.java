package me.yhamarsheh.dbms.phase3.dbmsphase3.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {

    private final String host;
    private final int port;
    private final String user;
    private final String password;

    private final String database = "Medical_Laboratory";

    private Connection connection;

    public SQLConnection(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if (!isConnected()) {
            connection = null;

            String url = "jdbc:mysql://" + host
                    + ":" + port +
                    "/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException | SQLException ex) {
                throw new SQLException(ex);
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public Connection getConnection() {
        return connection;
    }

}
