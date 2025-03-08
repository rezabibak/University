package org.example;

import java.time.LocalDateTime;

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