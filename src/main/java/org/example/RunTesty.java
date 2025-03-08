package org.example;

import ir.huri.jcal.JalaliCalendar;
import org.example.model.StudentDao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

public class RunTesty {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("======== Welcome to University page ========");
            System.out.println("1-Create student");
            System.out.println("2-Show all students");
            System.out.println("3-Find");
            System.out.println("4-Edit");
            System.out.println("5-Remove");
            System.out.println("6-Exit");
            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Please Enter number!");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        new Create().createStudent(scanner);
                        break;
                    case 2:
                        new Show().loadStudents();
                        break;
                    case 3:
                        new Show().loadStudentById(scanner);
                        break;
                    case 4:
                        new Edit().edit(scanner);
                        break;
                    case 5:
                        new Delete().delete(scanner);
                        break;
                    case 6:
                        System.out.println("خداحافظ!");
                        break;
                    default:
                        System.out.println("گزینه نامعتبر است!");
                }
            } catch (Exception e) {
                System.out.println("خطا: " + e.getMessage());
            }
        } while (choice != 6);
    }
}

class Create {
    private final StudentDao studentDao = new StudentDao();

    protected void createStudent(Scanner scanner) throws Exception {
        System.out.println("\n======== Registration page ========\n");

        System.out.print("First name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Date of birth (yyyy/mm/dd): ");
        String dateOfBirthStr = scanner.nextLine();
        LocalDate dateOfBirth = validateAndParseJalaliDate(dateOfBirthStr);

        System.out.print("national code: ");
        String nationalCode = scanner.nextLine();
        if (!nationalCode.matches("\\d{10}")) {
            throw new IllegalArgumentException("national code must contain 10 digits");
        }

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Phone number: ");
        String phoneNumber = scanner.nextLine();
        if (!phoneNumber.matches("09\\d{9}")) {
            throw new IllegalArgumentException("The phone number must be 11 digits and start with 09, for example 09123456789");
        }

        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("Major: ");
        String major = scanner.nextLine();

        StudentInterface student = StudentFactory.createStudent(0, null, firstName, lastName, String.valueOf(dateOfBirth), nationalCode,
                email, phoneNumber, address, major, null);

        studentDao.saveStudent(student);
        System.out.println("The student was successfully registered.");
    }

    private LocalDate validateAndParseJalaliDate(String date) {
        if (!date.matches("\\d{4}/\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("Date of birth must be in YYYY/MM/DD format, for example, 02/25/1380");
        }

        String[] parts = date.split("/");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        if (year < 1300 || year > 1402) {
            throw new IllegalArgumentException("The year must be between 1300 and 1402.");
        }

        try {
            JalaliCalendar jc = new JalaliCalendar(year, month, day);
            GregorianCalendar gc = jc.toGregorian();
            return gc.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (Exception e) {
            throw new IllegalArgumentException("The solar date is invalid. For example, 1380/02/25");
        }
    }
}

class Show {
    private final StudentDao studentDao = new StudentDao();

    protected void loadStudents() {
        System.out.println("\n======== Show all students ========\n");
        try {
            List<StudentInterface> students = studentDao.findAll();
            if (students.isEmpty()) {
                System.out.println("No student found!");
            }
            for (StudentInterface student : students) {
                System.out.println("ID: " + student.getId() +
                        ", Student ID: " + student.getStudentId() +
                        ", First name: " + student.getFirstName() +
                        ", Last name: " + student.getLastName() +
                        ", Date of birth: " + student.getDateOfBirth() +
                        ",National code: " + student.getNationalCode() +
                        ",Email: " + student.getEmail() +
                        ", Phone number: " + student.getPhoneNumber() +
                        ",Address: " + student.getAddress() +
                        ",Major: " + student.getMajor() +
                        ",Date of manufacture: " + student.getEntryDate());
            }
        } catch (SQLException e) {
            System.out.println("Error to load: " + e.getMessage());
        }
    }

    protected void loadStudentById(Scanner scanner) throws SQLException {
        System.out.println("\n======== Search page ========\n");
        System.out.print("Enter student ID: ");
        String idText = scanner.nextLine();
        try {
            long id = Long.parseLong(idText);
            StudentInterface student = studentDao.findByStudentId(id);
            if (student != null) {
                System.out.println("ID: " + student.getId() +
                        "\nStudent ID: " + student.getStudentId() +
                        "\nFirst name: " + student.getFirstName() +
                        "\nLast name: " + student.getLastName() +
                        "\nDate of birth: " + student.getDateOfBirth() +
                        "\nNational code" + student.getNationalCode() +
                        "\nEmail" + student.getEmail() +
                        "\nPhone number: " + student.getPhoneNumber() +
                        "\nAddress" + student.getAddress() +
                        "\nMajor" + student.getMajor() +
                        "\nDate of manufacture: " + student.getEntryDate());
            } else {
                System.out.println("Student id " + id + " Not found!");
            }
        } catch (NumberFormatException e) {
            System.out.println("please enter number!");
        }
    }
}

class Edit {
    private final StudentDao studentDao = new StudentDao();

    protected void edit(Scanner scanner) throws SQLException {
        System.out.println("\n======== Edit page ========\n");
        System.out.print("please enter student ID: ");
        String idText = scanner.nextLine();
        if (idText.isEmpty()) {
            System.out.println("Please enter student ID!");
            return;
        }

        try {
            long id = Long.parseLong(idText);
            StudentInterface student = studentDao.findByStudentId(id);
            if (student != null) {
                System.out.println("Current student information: " + student);

                System.out.print("New email (blank for no change): ");
                String email = scanner.nextLine();
                if (email.isEmpty()) email = student.getEmail();

                System.out.print("New  phone number (blank for no change): ");
                String phoneNumber = scanner.nextLine();
                if (phoneNumber.isEmpty()) {
                    phoneNumber = student.getPhoneNumber();
                } else if (!phoneNumber.matches("09\\d{9}")) {
                    throw new IllegalArgumentException("The phone number must be 11 digits and start with 09, for example 09123456789");
                }

                System.out.print("New address (blank for no change): ");
                String address = scanner.nextLine();
                if (address.isEmpty()) address = student.getAddress();

                StudentInterface updatedStudent = StudentFactory.createStudent(
                        student.getId(), student.getStudentId(), student.getFirstName(), student.getLastName(),
                        student.getDateOfBirth(), student.getNationalCode(), email, phoneNumber, address,
                        student.getMajor(), student.getEntryDate()
                );
                studentDao.updateStudent(updatedStudent);
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("Student not Found!");
            }
        } catch (NumberFormatException e) {
            System.out.println("student id must be number!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

class Delete {
    private final StudentDao studentDao = new StudentDao();

    protected void delete(Scanner scanner) throws SQLException {
        System.out.println("\n======== Delete page ========\n");
        System.out.print("Enter student ID: ");
        String idText = scanner.nextLine();
        try {
            long id = Long.parseLong(idText);
            StudentInterface student = studentDao.findByStudentId(id);
            if (student != null) {
                studentDao.deleteStudent(id);
                System.out.println("The student was successfully deleted.");
            } else {
                System.out.println("Student not found!");
            }
        } catch (NumberFormatException e) {
            System.out.println("student id must be number!");
        }
    }
}