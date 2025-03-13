package org.example.pages;

import org.example.model.StudentDao;
import org.example.StudentInterface;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

// NOTE Java Swing is obsolete, consider to use an alternative framework like Java-Fx
public class SearchWindow extends JFrame {
    private final StudentDao studentDao = new StudentDao();
    private final JTextField searchIdField = new JTextField(10);
    private final JTextArea resultArea = new JTextArea(10, 30);

    public SearchWindow() {
        super("Search Student");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // بخش ورودی
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(searchIdField);
        JButton searchButton = new JButton("search");
        searchButton.addActionListener(_ -> searchStudent());
        inputPanel.add(searchButton);

        // بخش نمایش نتیجه
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Thoma", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
    }

    private void searchStudent() {
        try {
            String idText = searchIdField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter Student ID!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            long id = Long.parseLong(idText);
            StudentInterface student = studentDao.findByStudentId(id);

            if (student != null) {
                resultArea.setText(
                        "ID: " + student.getId() + "\n" +
                                "Student ID: " + student.getStudentId() + "\n" +
                                "First name: " + student.getFirstName() + "\n" +
                                "Last name: " + student.getLastName() + "\n" +
                                "Date of birthday: " + student.getDateOfBirth() + "\n" +
                                "NNational code: " + student.getNationalCode() + "\n" +
                                "Email: " + student.getEmail() + "\n" +
                                "Phone number: " + student.getPhoneNumber() + "\n" +
                                "Address: " + student.getAddress() + "\n" +
                                "Major: " + student.getMajor() + "\n" +
                                "Entry Date: " + student.getEntryDate()
                );
            } else {
                resultArea.setText("Student ID " + id + " not found!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "please enter number!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error to search: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}