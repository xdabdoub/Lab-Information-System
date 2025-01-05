package me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub;

import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.Permission;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.User;
import me.yhamarsheh.dbms.phase3.dbmsphase3.storage.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersManager {

    private ArrayList<User> users;
    private User activeUser;

    public UsersManager() {
        this.users = new ArrayList<>();
    }

    public void initializeUsers() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        List<User> userList = query.fetchAllData("SELECT * FROM Users", rs -> {
            try {
                return new User(
                        rs.getBytes("username"),
                        rs.getBytes("password"),
                        rs.getLong("doctorId"),
                        Permission.valueOf(rs.getString("permission").toUpperCase())
                );
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            return null;
        });

        users.addAll(userList);
    }

    public void addUser(User user) {
        users.add(user);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("INSERT INTO Users (doctorId, username, password, permission) VALUES (?,?,?,?)",
                user.getDoctor().getId(), user.getUsername(), user.getPassword(), user.getPermission().toString());

    }

    public void deleteUser(User user) {
        users.remove(user);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("DELETE FROM Users WHERE doctorId=?",
                user.getDoctor().getId());
    }

    public void updateUser(User user, long oldId) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("UPDATE Users SET username=?, password=?, permission=? WHERE doctorId=?",
                user.getUsername(), user.getPassword(), user.getPermission().toString(),
                oldId);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }
}
