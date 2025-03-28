package org.example.model;

import org.example.StudentFactory;
import org.example.StudentInterface;
import org.example.util.DatabaseConfig;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// NOTE it's better to define student dao functionalities in a StudentDao interface and then implement it;
public class StudentDao {

    //Create student
    public void saveStudent(StudentInterface student) throws SQLException, IllegalAccessException, NoSuchFieldException {
        String sql = "INSERT INTO students (id, student_id, first_name, last_name, date_of_birth, national_code, email, phone_number, address, major, entry_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // NOTE one of the general solution for generating id for a table is using sequence.
                // some databases like oracle has a builtin sequence object others like MySql has auto-generate id feature
                // as a suggestion you can consider to use these solutions instead.

                //Call getNextId to create new id
                long nextId = getNextId(conn);

                LocalDateTime entryDate = LocalDateTime.now();
                String year = String.valueOf(entryDate.getYear());
                // NOTE what dose 11213 means? is it a magic number !!?
                //Create studentID like this year + 11213 + id => Ex: 2025 11213 014
                int sequence = getNextSequence(conn, year);
                String studentId = year + "11213" + String.format("%03d", sequence);

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setLong(1, nextId);
                    stmt.setString(2, studentId);
                    stmt.setString(3, student.getFirstName());
                    stmt.setString(4, student.getLastName());
                    stmt.setString(5, student.getDateOfBirth());
                    stmt.setString(6, student.getNationalCode());
                    stmt.setString(7, student.getEmail());
                    stmt.setString(8, student.getPhoneNumber());
                    stmt.setString(9, student.getAddress());
                    stmt.setString(10, student.getMajor());
                    stmt.setObject(11, entryDate);

                    stmt.executeUpdate();
                }


                Field idField = student.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(student, nextId);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;// NOTE handel expectation properly. this is not a good way to handel transaction exceptions
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    //For show max id is created
    private long getNextId(Connection conn) throws SQLException {
        String query = "SELECT MAX(id) FROM students";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                long maxId = rs.getLong(1);
                return maxId + 1;
            }
            return 1;
        }
    }

    private int getNextSequence(Connection conn, String year) throws SQLException {
        String query = "SELECT COUNT(*) FROM students WHERE student_id LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, year + "11213%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) + 1;
                }
            }
        }
        return 1;
    }

    //this method to find by StudentID not id
    public StudentInterface findByStudentId(long studentID) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, studentID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createStudentFromResultSet(rs);
                }
            }
        }
        return null;
    }

    //This method to show all table
    public List<StudentInterface> findAll() throws SQLException {
        List<StudentInterface> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                students.add(createStudentFromResultSet(rs));
            }
        }
        return students;
    }

    private StudentInterface createStudentFromResultSet(ResultSet rs) throws SQLException {
        // NOTE one of the usage of Reflection to create Student object in runtime is here.
        // Inset of having a pre-defined factory method like createStudent() you can create and set student
        // object field for each result set column. for example
        // if (rs.getString("first_name") != null)
        //    set firstName field via reflection
        // ORM Frameworks like Hibernate, MyBatis or Spring data jdbc use this technic to map q query result to an entity object
        return StudentFactory.createStudent(
                rs.getLong("id"),
                rs.getString("student_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("date_of_birth"),
                rs.getString("national_code"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getString("address"),
                rs.getString("major"),
                rs.getObject("entry_date", LocalDateTime.class)
        );
    }

    //For edit student
    public void updateStudent(StudentInterface updatedStudent) throws SQLException {
        String sql = "UPDATE students SET student_id = ?, first_name = ?, last_name = ?, date_of_birth = ?, national_code = ?, email = ?, phone_number = ?, address = ?, major = ?, entry_date = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, updatedStudent.getStudentId());
            stmt.setString(2, updatedStudent.getFirstName());
            stmt.setString(3, updatedStudent.getLastName());
            stmt.setString(4, updatedStudent.getDateOfBirth());
            stmt.setString(5, updatedStudent.getNationalCode());
            stmt.setString(6, updatedStudent.getEmail());
            stmt.setString(7, updatedStudent.getPhoneNumber());
            stmt.setString(8, updatedStudent.getAddress());
            stmt.setString(9, updatedStudent.getMajor());
            stmt.setObject(10, updatedStudent.getEntryDate());
            stmt.setLong(11, updatedStudent.getId());

            stmt.executeUpdate();
        }
    }

    //For remove Student by id,
    //but I use swing so you can delete with select
    public void deleteStudent(long id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}