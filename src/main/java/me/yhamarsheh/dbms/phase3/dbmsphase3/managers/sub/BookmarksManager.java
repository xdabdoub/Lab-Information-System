package me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub;

import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.EntityType;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.*;
import me.yhamarsheh.dbms.phase3.dbmsphase3.storage.Query;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookmarksManager {

    private ArrayList<Bookmark<?>> bookmarks;

    public BookmarksManager() {
        this.bookmarks = new ArrayList<>();
        initializeBookmarks();
    }

    public void initializeBookmarks() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        List<Bookmark<?>> bookmarkList = query.fetchAllData("SELECT * FROM Bookmarks", rs -> {
            try {
                String entityTypeStr = rs.getString("entityType");
                EntityType entityType = EntityType.valueOf(entityTypeStr.toUpperCase());

                int userId = rs.getInt("userId");

                long entityId = rs.getLong("entityId");

                return switch (entityType) {
                    case PATIENT -> new Bookmark<Patient>(rs.getInt("bookmarkId"), userId, entityId, entityType, rs.getDate("bookmarkedAt").toLocalDate());
                    case DOCTOR -> new Bookmark<User>(rs.getInt("bookmarkId"), userId, entityId, entityType, rs.getDate("bookmarkedAt").toLocalDate());
                    case SAMPLE -> new Bookmark<Sample>(rs.getInt("bookmarkId"), userId, entityId, entityType, rs.getDate("bookmarkedAt").toLocalDate());
                    case REPORT -> new Bookmark<Report>(rs.getInt("bookmarkId"), userId, entityId, entityType, rs.getDate("bookmarkedAt").toLocalDate());
                    case TEST -> new Bookmark<Test>(rs.getInt("bookmarkId"), userId, entityId, entityType, rs.getDate("bookmarkedAt").toLocalDate());
                    case USER -> new Bookmark<User>(rs.getInt("bookmarkId"), userId, entityId, entityType, rs.getDate("bookmarkedAt").toLocalDate());
                    case INVOICE -> new Bookmark<Invoice>(rs.getInt("bookmarkId"), userId, entityId, entityType, rs.getDate("bookmarkedAt").toLocalDate());
                };
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        });

        bookmarks.addAll(bookmarkList);
    }
    public void addBookMark(Bookmark<?> bookmark) {
        bookmarks.add(bookmark);
        Query query = new Query(Driver.getSQLConnection().getConnection());

        String entityTypeStr = bookmark.getEntityType().toString();

        query.build("INSERT INTO Bookmarks (userId, entityId, entityType, bookmarkedAt) VALUES (?,?,?,?)",
                bookmark.getUser().getDoctor().getId(),
                bookmark.getEntityId(),
                entityTypeStr,
                Date.valueOf(LocalDate.now())
        );
    }


    public void deleteBookmark(Bookmark<?> bookmark) {
        bookmarks.remove(bookmark);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("DELETE FROM Bookmarks WHERE bookmarkId=?",
                bookmark.getBookmarkId());
    }

    public Bookmark<?> getBookmarkByObject(Object obj) {
        return bookmarks.stream().filter(bookmark -> bookmark.getEntity().equals(obj)).findFirst().orElse(null);
    }

    public ArrayList<Bookmark<?>> getBookmarks() {
        return bookmarks;
    }

    public boolean bookmarkExists(Object obj) {
        return bookmarks.stream().anyMatch(bookmark -> bookmark.getEntity().equals(obj));
    }
}
