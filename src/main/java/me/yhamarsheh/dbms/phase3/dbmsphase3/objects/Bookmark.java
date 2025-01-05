package me.yhamarsheh.dbms.phase3.dbmsphase3.objects;

import me.yhamarsheh.dbms.phase3.dbmsphase3.enums.EntityType;
import me.yhamarsheh.dbms.phase3.dbmsphase3.utilities.GeneralUtils;

import java.time.LocalDate;

public class Bookmark<T> {

    private int bookmarkId;
    private User user;
    private long entityId;
    private EntityType entityType;
    private LocalDate bookmarkDate;
    private T entity;

    public Bookmark(int bookmarkId, long doctorId, long entityId, EntityType entityType, LocalDate bookmarkDate) {
        this.bookmarkId = bookmarkId;
        this.user = GeneralUtils.getUserByDoctorId(doctorId);
        this.entityId = entityId;
        this.entityType = entityType;
        this.bookmarkDate = bookmarkDate;

        init();
    }

    private void init() {
        if (entityType == EntityType.PATIENT) entity = (T) GeneralUtils.getPatientById(entityId);
        else if (entityType == EntityType.DOCTOR) entity = (T) GeneralUtils.getDoctorById(entityId);
        else if (entityType == EntityType.SAMPLE) entity = (T) GeneralUtils.getSampleById((int) entityId);
        else if (entityType == EntityType.REPORT) entity = (T) GeneralUtils.getReportById((int) entityId);
        else if (entityType == EntityType.TEST) entity = (T) GeneralUtils.getTestById((int) entityId);
        else if (entityType == EntityType.USER) entity = (T) GeneralUtils.getUserByDoctorId(entityId);
        else if (entityType == EntityType.INVOICE) entity = (T) GeneralUtils.getInvoiceById(entityId);
    }

    public int getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(int bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public LocalDate getBookmarkDate() {
        return bookmarkDate;
    }

    public void setBookmarkDate(LocalDate bookmarkDate) {
        this.bookmarkDate = bookmarkDate;
    }
}
