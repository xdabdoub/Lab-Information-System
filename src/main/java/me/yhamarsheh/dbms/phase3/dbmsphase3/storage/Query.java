package me.yhamarsheh.dbms.phase3.dbmsphase3.storage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Query {

    private final Connection connection;

    public Query(Connection connection) {
        this.connection = connection;
    }

    public int build(String query, Object... data) {
        int affectedRows = 0;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof Integer)
                    ps.setInt(i + 1, (int) data[i]);
                else if (data[i] instanceof String)
                    ps.setString(i + 1, (String) data[i]);
                else if (data[i] instanceof Boolean)
                    ps.setBoolean(i + 1, (boolean) data[i]);
                else if (data[i] instanceof Date)
                    ps.setDate(i + 1, (Date) data[i]);
                else if (data[i] instanceof byte[])
                    ps.setBytes(i + 1, (byte[]) data[i]);
                else ps.setObject(i + 1, data[i]);
            }
            affectedRows = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("An error occurred while executing query: " + e.getMessage());
        }

        return affectedRows;
    }

    public <T> List<T> fetchAllData(String query, Function<ResultSet, T> rowMapper, Object... data) {
        List<T> results = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof Integer)
                    ps.setInt(i + 1, (int) data[i]);
                else if (data[i] instanceof String)
                    ps.setString(i + 1, (String) data[i]);
                else if (data[i] instanceof Boolean)
                    ps.setBoolean(i + 1, (boolean) data[i]);
                else if (data[i] instanceof Date)
                    ps.setDate(i + 1, (Date) data[i]);
                else if (data[i] instanceof byte[])
                    ps.setBytes(i + 1, (byte[]) data[i]);
                else ps.setObject(i + 1, data[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(rowMapper.apply(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while executing query: " + e.getMessage());
        }

        return results;
    }

    public <T> T get(String query, int x, Object... data) {
        T result = null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof Integer)
                    ps.setInt(i + 1, (int) data[i]);
                else if (data[i] instanceof String)
                    ps.setString(i + 1, (String) data[i]);
                else if (data[i] instanceof Boolean)
                    ps.setBoolean(i + 1, (boolean) data[i]);
                else if (data[i] instanceof Date)
                    ps.setDate(i + 1, new java.sql.Date(((Date) data[i]).getTime()));
                else
                    ps.setObject(i + 1, data[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = (T) rs.getObject(x);
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while executing query: " + e.getMessage());
        }

        return result;
    }


}
