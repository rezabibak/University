package org.example;

import java.time.LocalDateTime;
import java.lang.reflect.Method;

// NOTE Factory design pattern usually use when have an interface or abstract class with different implementations,
// but we don't want client code deal with how to create an instance of those.
// take look at https://refactoring.guru/design-patterns/factory-method
public class StudentFactory {
    public static StudentInterface createStudent(long id, String studentId, String firstName, String lastName,
                                                 String dateOfBirth, String nationalCode, String email,
                                                 String phoneNumber, String address, String major,
                                                 LocalDateTime entryDate) {
        try {
            // NOTE as I mentioned in Student.createStudent method, here you can use
            // Object student = clazz.getDeclaredConstructor().newInstance();
            // then you can use Reaction agan on student object and set value for all declaredFields of this object

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
