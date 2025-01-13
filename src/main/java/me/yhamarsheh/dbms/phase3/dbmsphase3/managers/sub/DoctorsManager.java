package me.yhamarsheh.dbms.phase3.dbmsphase3.managers.sub;

import javafx.scene.control.Button;
import me.yhamarsheh.dbms.phase3.dbmsphase3.Driver;
import me.yhamarsheh.dbms.phase3.dbmsphase3.objects.Doctor;
import me.yhamarsheh.dbms.phase3.dbmsphase3.storage.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorsManager {

    private ArrayList<Doctor> doctors;

    public DoctorsManager() {
        this.doctors = new ArrayList<>();
    }

    public void initializeDoctors() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        List<Doctor> doctorList = query.fetchAllData("SELECT * FROM Doctors", rs -> {
            try {
                return new Doctor(
                        rs.getLong("doctorId"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        });

        doctors.addAll(doctorList);
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("INSERT INTO Doctors (doctorId, name, phone, address) VALUES (?,?,?,?)",
                doctor.getId(), doctor.getName(), doctor.getPhone(), doctor.getAddress());
    }

    public void deleteDoctor(Doctor doctor) {
        doctors.remove(doctor);
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("DELETE FROM Doctors WHERE doctorId=?",
                doctor.getId());
    }

    public void updateDoctor(Doctor doctor, long oldId) {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        query.build("UPDATE Doctors SET doctorId=?, name=?, phone=?, address=? WHERE doctorId=?",
                doctor.getId(), doctor.getName() , doctor.getPhone(), doctor.getAddress(),
                oldId);
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }

    public int getTotalDoctors() {
        Query query = new Query(Driver.getSQLConnection().getConnection());
        Long count = query.get("SELECT COUNT(*) FROM Doctors;",    1);
        return count != null ? count.intValue() : 0;
    }

}