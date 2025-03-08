package org.example;

import java.time.LocalDateTime;
import java.lang.reflect.Method;

public class StudentFactory {
    public static StudentInterface createStudent(long id, String studentId, String firstName, String lastName,
                                                 String dateOfBirth, String nationalCode, String email,
                                                 String phoneNumber, String address, String major,
                                                 LocalDateTime entryDate) {
        try {
            // Using reflection to access the package-private Student.createStudent method
            Class<?> clazz = Class.forName("org.example.model.Student");
            Method createMethod = clazz.getDeclaredMethod("createStudent",
                    long.class, String.class, String.class, String.class,
                    String.class, String.class, String.class, String.class,
                    String.class, String.class, LocalDateTime.class);
            createMethod.setAccessible(true);
            return (StudentInterface) createMethod.invoke(null, id, studentId, firstName, lastName,
                    dateOfBirth, nationalCode, email, phoneNumber, address, major, entryDate);
        } catch (Exception e) {
            throw new RuntimeException("Error creating student: " + e.getMessage(), e);
        }
    }

}
