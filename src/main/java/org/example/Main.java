package org.example;

import org.example.model.StudentDao;
import ir.huri.jcal.JalaliCalendar;
import org.example.pages.EditWindow;
import org.example.pages.SearchWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;

public class Main {
    private static final StudentDao studentDao = new StudentDao();

    private static final JTextField firstNameField = new JTextField(10);
    private static final JTextField lastNameField = new JTextField(10);
    private static final JTextField dateOfBirthField = new JTextField(10);
    private static final JTextField nationalCodeField = new JTextField(10);
    private static final JTextField emailField = new JTextField(10);
    private static final JTextField phoneNumberField = new JTextField(10);
    private static final JTextField addressField = new JTextField(10);
    private static final JTextField majorField = new JTextField(10);

    private static DefaultTableModel tableModel;
    private static JTable studentTable;

    public static void main(String[] args) {
        JFrame frame = new JFrame("سیستم ثبت‌نام دانشجو");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(lastNameField);

        JLabel dateLabel = new JLabel("Date of birthday(YYYY/MM/DD):");
        dateOfBirthField.setToolTipText("تاریخ تولد را به فرمت شمسی وارد کنید، مثلاً: 1380/02/25");
        inputPanel.add(dateLabel);
        inputPanel.add(dateOfBirthField);

        inputPanel.add(new JLabel("National Code(10 number):"));
        nationalCodeField.setToolTipText("کدملی باید 10 رقم باشد، مثلاً: 1234567890");
        inputPanel.add(nationalCodeField);

        inputPanel.add(new JLabel("Email Address:"));
        emailField.setToolTipText("ایمیل معتبر وارد کنید، مثلاً: example@domain.com");
        inputPanel.add(emailField);

        JLabel phoneLabel = new JLabel("Phone Number(Iran):");
        phoneNumberField.setToolTipText("شماره را با فرمت 09123456789 وارد کنید (11 رقم)");
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneNumberField);

        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("Major:"));
        inputPanel.add(majorField);

        String[] columnNames = {"ID", "Student ID", "First Name", "Last Name", "Date of birthday", "National Code", "Email", "Phone number", "Address", "Major", "Entry Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        JButton saveButton = new JButton("Registration");
        saveButton.addActionListener(_ -> saveStudent());

        JButton deleteButton = new JButton("Remove");
        deleteButton.addActionListener(_ -> deleteStudent());

        JButton searchWindowButton = new JButton("Search");
        searchWindowButton.addActionListener(_ -> {
            SearchWindow searchWindow = new SearchWindow();
            searchWindow.setVisible(true);
        });

        JButton editWindowButton = new JButton("Edit");
        editWindowButton.addActionListener(_ -> {
            EditWindow editWindow = new EditWindow();
            editWindow.setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchWindowButton);
        buttonPanel.add(editWindowButton);

        // پنل اصلی
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);

        loadStudents();
    }

    private static void saveStudent() {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String dateOfBirth = dateOfBirthField.getText();
            validateJalaliDate(dateOfBirth);

            String nationalCode = nationalCodeField.getText();
            if (!nationalCode.matches("\\d{10}")) {
                throw new IllegalArgumentException("کدملی باید 10 رقم باشد");
            }

            String email = emailField.getText();
            String phoneNumber = phoneNumberField.getText();
            if (!phoneNumber.matches("09\\d{9}")) {
                throw new IllegalArgumentException("شماره تلفن باید 11 رقم و با 09 شروع شود، مثلاً 09123456789");
            }
            String address = addressField.getText();
            String major = majorField.getText();

            StudentInterface student = StudentFactory.createStudent(0, null, firstName, lastName, dateOfBirth, nationalCode, email, phoneNumber, address, major, null);
            studentDao.saveStudent(student);
            loadStudents();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error to save: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                long id = (long) tableModel.getValueAt(selectedRow, 0);
                studentDao.deleteStudent(id);
                loadStudents();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error to delete: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "please select on student!", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void loadStudents() {
        try {
            tableModel.setRowCount(0);
            List<StudentInterface> students = studentDao.findAll();
            for (StudentInterface student : students) {
                tableModel.addRow(new Object[]{
                        student.getId(),
                        student.getStudentId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getDateOfBirth(),
                        student.getNationalCode(),
                        student.getEmail(),
                        student.getPhoneNumber(),
                        student.getAddress(),
                        student.getMajor(),
                        student.getEntryDate()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error to load: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        dateOfBirthField.setText("");
        nationalCodeField.setText("");
        emailField.setText("");
        phoneNumberField.setText("");
        addressField.setText("");
        majorField.setText("");
    }

    private static void validateJalaliDate(String date) {
        if (!date.matches("\\d{4}/\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("تاریخ تولد باید به فرمت YYYY/MM/DD باشد، مثلاً 1380/02/25");
        }

        String[] parts = date.split("/");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        try {
            JalaliCalendar jc = new JalaliCalendar(year, month, day);
            GregorianCalendar gc = jc.toGregorian();
            gc.getTime();
            if (year < 1300 || year > 1405) {
                throw new IllegalArgumentException("سال باید بین 1300 تا 1405 باشد");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("تاریخ شمسی نامعتبر است. لطفاً یک تاریخ معتبر وارد کنید، مثلاً 1380/02/25");
        }
    }
}