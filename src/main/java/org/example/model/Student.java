package org.example.model;

import org.example.StudentInterface;

import java.time.LocalDateTime;

// NOTE It is great to define immutable student class, but remember for data class you always must override equals and hashCode
// methods, if you don't know why, google it
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

    // NOTE Why you make this contractor private?,
    // you make all fields of this class final witch means you expect to instantiate fields through public contractor.
    // If you want to instantiate this class only via Reflection is better to make your field none final and
    // remove this contractor and make the default contractor, private
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

    // NOTE If you define this static factory method just for instantiating an instance of this class via Reflection,
    // It's unnecessary. with Reflection API you can create instance of class by accessing to contractors of a class then
    // invoking newInstance() method
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
