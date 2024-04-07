package JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.sql.Date;

/*
 * Author: Candy Torres
 * Course: Software Development I - CEN 3024C
 * Due Date: April 7, 2024.
 * Class Name: JDBC_GUI
 * Description: Represents the main page for Library Management System GUI integrating SQLite.
 * This page provides access to various functions such as adding, removing,
 * checking books in/out, upload books from a txt file, and database display.
 */
public class JDBC_GUI extends JFrame {
    // Database URL
    private static final String DB_URL = "jdbc:sqlite:LMSlibrary.db";
    private Connection connection;
    private Statement statement;
    private JLabel outputLabel;

    public JDBC_GUI() {

        // Frame setup
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Set font and alignment for title
        Font titleFont = new Font("Arial", Font.BOLD, 24); // Choose your desired font and size
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("Library Management System", JLabel.CENTER);
        titleLabel.setFont(titleFont);

        // Initialize components
        outputLabel = new JLabel("Please make a selection");
        outputLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align the text
        outputLabel.setFont(outputLabel.getFont().deriveFont(Font.BOLD, 20f)); // Increase font size and make it bold
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));



        // Add components to frame
        getContentPane().add(titleLabel, BorderLayout.NORTH); // Add titleLabel instead of outputLabel
        getContentPane().add(buttonPanel, BorderLayout.CENTER);


        // Connect to the database
        try {
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set up main buttons
        switchToMainButtons(buttonPanel);
    }

    // Switch to main buttons
    private void switchToMainButtons(JPanel buttonPanel) {
        buttonPanel.removeAll();
        JButton displayButton = new JButton("Display Database");
        JButton removeButton = new JButton("Remove Book");
        JButton checkOutButton = new JButton("Check Out Book");
        JButton checkInButton = new JButton("Check In Book");
        JButton exitButton = new JButton("Exit");

        // Set font for buttons
        Font buttonFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
        displayButton.setFont(buttonFont);
        removeButton.setFont(buttonFont);
        checkOutButton.setFont(buttonFont);
        checkInButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        // Add action listeners for buttons
        displayButton.addActionListener(e -> displayDatabase());
        removeButton.addActionListener(e -> removeBook());
        checkOutButton.addActionListener(e -> checkOutBook());
        checkInButton.addActionListener(e -> checkInBook());
        exitButton.addActionListener(e -> exitApplication());

        // Add buttons to panel
        buttonPanel.add(displayButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(checkInButton);
        buttonPanel.add(exitButton);

        // Refresh the panel
        revalidate();
        repaint();
    }

    /**
     * Remove books from SQL database
     */
    private void removeBook() {
        String barcode = JOptionPane.showInputDialog(null, "Enter barcode of the book to remove:");
        if (barcode != null) {
            try {
                String query = "DELETE FROM books WHERE barcode = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, barcode);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(null, "Book removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Book with barcode " + barcode + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred while removing the book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Check out a book
     * User is able to check out a book from the SQL database by title.
     * Title is supplied by user.
     */
    private void checkOutBook() {
        String title = JOptionPane.showInputDialog(null, "Enter the title of the book to check out:");
        if (title != null) {
            try {
                // Check if the book is available (not already checked out)
                String query = "SELECT * FROM books WHERE title = ? AND status = 'checked in'";
                PreparedStatement checkStatement = connection.prepareStatement(query);
                checkStatement.setString(1, title);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    // Book is available, update its status and due date
                    int id = resultSet.getInt("barcode");
                    String updateQuery = "UPDATE books SET status = 'checked out', due_date = ? WHERE barcode = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    // Calculate due date (4 weeks from the current date)
                    LocalDate currentDate = LocalDate.now();
                    LocalDate dueDate = currentDate.plusWeeks(4);
                    Date sqlDueDate = Date.valueOf(dueDate);

                    // Set parameters for the update statement
                    updateStatement.setDate(1, sqlDueDate);
                    updateStatement.setInt(2, id);

                    // Execute the update statement
                    int rowsUpdated = updateStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(null, "Book '" + title + "' checked out successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to check out the book.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Book '" + title + "' is not available for checkout.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred while checking out the book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Check in a book
     * User is able to check in a book from the SQL database by title.
     * Title is supplied by user.
     */
    private void checkInBook() {
        String title = JOptionPane.showInputDialog(null, "Enter the title of the book to check in:");
        if (title != null) {
            try {
                // Check if the book is checked out
                String query = "SELECT * FROM books WHERE title = ? AND status = 'checked out'";
                PreparedStatement checkStatement = connection.prepareStatement(query);
                checkStatement.setString(1, title);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    // Book is checked out, update its status and due date to null
                    int id = resultSet.getInt("barcode");
                    String updateQuery = "UPDATE books SET status = 'checked in', due_date = NULL WHERE barcode = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    // Set parameter for the update statement
                    updateStatement.setInt(1, id);

                    // Execute the update statement
                    int rowsUpdated = updateStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(null, "Book '" + title + "' checked in successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to check in the book.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Book '" + title + "' is not currently checked out.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred while checking in the book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Display database contents from SQL database.
     */

    private void displayDatabase() {

        JFrame bookList = new JFrame();
        bookList.setSize(680, 450);
        // Initialize components
        JLabel bookItems = new JLabel();
        bookItems.setHorizontalAlignment(SwingConstants.CENTER); // Center align the text
        bookItems.setFont(bookItems.getFont().deriveFont(Font.BOLD, 20f)); // Increase font size and make it bold



        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            StringBuilder stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                Integer barcode = resultSet.getInt("barcode");
                String status = resultSet.getString("status");
                String dueDate = resultSet.getString("due_date");

                stringBuilder.append(", Title: ").append(title).append(", Author: ")
                        .append(author).append(", Barcode: ").append(barcode).append(", Status: ").append(status)
                        .append(", Due Date: ").append(dueDate).append("<br>");
            }
            bookItems.setText("<html><body><div align='center'>Please make a selection</div><br>" + stringBuilder.toString() + "</body></html>");
            bookList.getContentPane().add(bookItems);
            bookList.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exit application
     */
    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JDBC_GUI().setVisible(true));
    }
}
