package me.yhamarsheh.dbms.phase3.dbmsphase3.objects;

import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.Permission;
import me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub.BookmarksManager;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

public class User {

    private byte[] username;
    private byte[] password;
    private Doctor doctor;
    private Permission permission;
    private BookmarksManager bookmarksManager;

    public User(byte[] username, byte[] password, long doctorId, Permission permission) {
        this.username = username;
        this.password = password;
        this.doctor = GeneralUtils.getDoctorById(doctorId);
        this.permission = permission;
        this.bookmarksManager = new BookmarksManager();
    }

    public byte[] getUsername() {
        return username;
    }

    public void setUsername(byte[] username) {
        this.username = username;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public boolean isAdmin() {
        return permission == Permission.ADMINISTRATOR;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public BookmarksManager getBookmarksManager() {
        return bookmarksManager;
    }

    public void setBookmarksManager(BookmarksManager bookmarksManager) {
        this.bookmarksManager = bookmarksManager;
    }
}
