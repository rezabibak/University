package org.example.pages;

import org.example.model.StudentDao;
import org.example.StudentFactory;
import org.example.StudentInterface;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class EditWindow extends JFrame {
    private final StudentDao studentDao = new StudentDao();

    private final JTextField searchIdField = new JTextField(10);
    private final JTextField phoneNumberField = new JTextField(10);
    private final JTextField emailField = new JTextField(10);
    private final JTextField addressField = new JTextField(10);
    private final JLabel firstNameLabel = new JLabel("");
    private final JLabel lastNameLabel = new JLabel("");
    private final JLabel dateOfBirthLabel = new JLabel("");
    private final JLabel nationalCodeLabel = new JLabel("");
    private final JLabel majorLabel = new JLabel("");

    private StudentInterface currentStudent;

    public EditWindow() {
        super("Edit Student");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // بخش جستجو با id
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Student ID:"));
        searchPanel.add(searchIdField);
        JButton loadButton = new JButton("Find Student");
        loadButton.addActionListener(_ -> loadStudent());
        searchPanel.add(loadButton);

        // بخش فیلدهای ویرایش
        JPanel editPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        editPanel.add(new JLabel("First Name:"));
        editPanel.add(firstNameLabel);
        editPanel.add(new JLabel("Last Name:"));
        editPanel.add(lastNameLabel);
        editPanel.add(new JLabel("Date of Birth:"));
        editPanel.add(dateOfBirthLabel);
        editPanel.add(new JLabel("National Code:"));
        editPanel.add(nationalCodeLabel);
        editPanel.add(new JLabel("Email Address:"));
        editPanel.add(emailField);
        editPanel.add(new JLabel("Phone Number:"));
        editPanel.add(phoneNumberField);
        editPanel.add(new JLabel("Address:"));
        editPanel.add(addressField);
        editPanel.add(new JLabel("Major:"));
        editPanel.add(majorLabel);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> saveChanges());

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(editPanel, BorderLayout.CENTER);
        panel.add(saveButton, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadStudent() {
        try {
            String idText = searchIdField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter number!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            long id = Long.parseLong(idText);
            currentStudent = studentDao.findByStudentId(id);

            if (currentStudent != null) {
                firstNameLabel.setText(currentStudent.getFirstName());
                lastNameLabel.setText(currentStudent.getLastName());
                dateOfBirthLabel.setText(currentStudent.getDateOfBirth());
                nationalCodeLabel.setText(currentStudent.getNationalCode());
                emailField.setText(currentStudent.getEmail());
                phoneNumberField.setText(currentStudent.getPhoneNumber());
                addressField.setText(currentStudent.getAddress());
                majorLabel.setText(currentStudent.getMajor());
            } else {
                JOptionPane.showMessageDialog(this, "Student ID " + id + " Not found!", "Result", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter number!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error to load: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveChanges() {
        if (currentStudent == null) {
            JOptionPane.showMessageDialog(this, "please insert one student !", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String email = emailField.getText();
            if (email.isEmpty()) email = currentStudent.getEmail();
            String phoneNumber = phoneNumberField.getText();
            if (phoneNumber.isEmpty()) phoneNumber = currentStudent.getPhoneNumber();
            if (!phoneNumber.matches("09\\d{9}")) {
                throw new IllegalArgumentException("شماره تلفن باید 11 رقم و با 09 شروع شود، مثلاً 09123456789");
            }
            String address = addressField.getText();
            if (address.isEmpty()) address = currentStudent.getAddress();

            StudentInterface updatedStudent = StudentFactory.createStudent(
                    currentStudent.getId(),
                    currentStudent.getStudentId(),
                    currentStudent.getFirstName(),
                    currentStudent.getLastName(),
                    currentStudent.getDateOfBirth(),
                    currentStudent.getNationalCode(),
                    email,
                    phoneNumber,
                    address,
                    currentStudent.getMajor(),
                    currentStudent.getEntryDate()
            );

            studentDao.updateStudent(updatedStudent);
            JOptionPane.showMessageDialog(this, "your edit is success!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            currentStudent = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error to save: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        firstNameLabel.setText("");
        lastNameLabel.setText("");
        dateOfBirthLabel.setText("");
        nationalCodeLabel.setText("");
        emailField.setText("");
        phoneNumberField.setText("");
        addressField.setText("");
        majorLabel.setText("");
    }
}