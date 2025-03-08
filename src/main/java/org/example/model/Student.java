package org.example.model;

import org.example.StudentInterface;

import java.time.LocalDateTime;

final class Student implements StudentInterface {
    private final long id;
    private final String studentId;
    private final String firstName;
    private final String lastName;
    private final String dateOfBirth;
    private final String nationalCode;
    private final String email;
    private final String phoneNumber;
    private final String address;
    private final String major;
    private final LocalDateTime entryDate;

    private Student(long id, String studentId, String firstName, String lastName, String dateOfBirth,
                    String nationalCode, String email, String phoneNumber, String address, String major, LocalDateTime entryDate) {
        this.id = id;
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nationalCode = nationalCode;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.major = major;
        this.entryDate = entryDate;
    }

    static Student createStudent(long id, String studentId, String firstName, String lastName, String dateOfBirth,
                                 String nationalCode, String email, String phoneNumber, String address, String major, LocalDateTime entryDate) {
        return new Student(id, studentId, firstName, lastName, dateOfBirth, nationalCode, email, phoneNumber, address, major, entryDate);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getStudentId() {
        return studentId;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String getNationalCode() {
        return nationalCode;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getMajor() {
        return major;
    }

    @Override
    public LocalDateTime getEntryDate() {
        return entryDate;
    }


}
