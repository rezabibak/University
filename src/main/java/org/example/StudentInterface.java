package org.example;

import java.time.LocalDateTime;

// NOTE In OOP interfaces usually used for defining one or more functionalities as a contract among other classes
// in here StudentInterface just used for data access field which is unnecessary and can declare inside Student class.
// Methods like registerNewStudent(), getStudentInfo(), disableStudent() , .... are examples for StudentInterface
public interface StudentInterface {
    long getId();
    String getStudentId();
    String getFirstName();
    String getLastName();
    String getDateOfBirth();
    String getNationalCode();
    String getEmail();
    String getPhoneNumber();
    String getAddress();
    String getMajor();
    LocalDateTime getEntryDate();
}