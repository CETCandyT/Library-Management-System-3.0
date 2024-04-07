import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Iterator;
import java.io.File;

/* Author: Candy Torres
 * Course: Software Development I - CEN 3024C
 * Due Date: March 24, 2024.
 * Class Name: MainFrame for GUI entry and functions for LMS Main Menu display.
 * Description: Represents the main page for Library Management System GUI.
 * This page provides access to various functions such as adding, removing,
 * checking books in/out, upload books from a txt file, and database display.
 */
public class MainFrame extends JFrame {

    // Reference to the library instance
    private final Library library;

    public MainFrame() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the library
        library = new Library("books.txt");

        // Create main panel to hold components
        JPanel mainPanel = new JPanel(new BorderLayout());
        // Add padding around the main panel
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create panel for display buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1));
        // Add padding around the button panel
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //label for LMS Main Menu message
        JLabel messageLabel = new JLabel("Please make a selection");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.add(messageLabel);

        // Initialize Main Menu LMS system buttons
        JButton addButton = new JButton("Add a Book");
        JButton importBooksButton = new JButton("Import Book List");
        JButton removeByBarcodeButton = new JButton("Remove a Book by barcode");
        JButton removeByTitleButton = new JButton("Remove a Book by title");
        JButton checkOutButton = new JButton("Check Out a book");
        JButton checkInButton = new JButton("Check In a book");
        JButton displayButton = new JButton("Display database");
        JButton exitButton = new JButton("Exit");

        // Add buttons to button panel
        buttonPanel.add(addButton);
        buttonPanel.add(importBooksButton);
        buttonPanel.add(removeByBarcodeButton);
        buttonPanel.add(removeByTitleButton);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(checkInButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(exitButton);

        // Add button panel to main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        // Add main panel to frame
        add(mainPanel);
        // Set frame size
        setSize(400, 400);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Add action listeners
        addButton.addActionListener(event -> navigateToAddBookPage());
        importBooksButton.addActionListener(event -> importBooks());
        removeByBarcodeButton.addActionListener(event -> navigateToRemoveByBarcodePage());
        removeByTitleButton.addActionListener(event -> navigateToRemoveByTitlePage());
        checkOutButton.addActionListener(event -> navigateToCheckOutPage());
        checkInButton.addActionListener(event -> navigateToCheckInPage());
        displayButton.addActionListener(event -> navigateToDisplayDatabasePage());
        exitButton.addActionListener(event -> confirmExit());
    }

    /**
     * Method: importBooks
     * This method allows the user to import books from a selected file into the LMS database.
     */
    private void importBooks() {
        // Create a file chooser dialog
        JFileChooser fileChooser = new JFileChooser();

        // Set the dialog to accept only files, not directories
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Show the dialog and wait for the user to choose a file
        int result = fileChooser.showOpenDialog(this);

        // Check if the user selected a file
        if (result == JFileChooser.APPROVE_OPTION) {
            // Get the selected file
            File selectedFile = fileChooser.getSelectedFile();

            // Attempt to import books from the selected file to LMS database
            Library library = new Library(selectedFile.getAbsolutePath());
            library.loadBooksFromFile(selectedFile.getAbsolutePath());

            // Confirmation message display  when books were imported successfully
            JOptionPane.showMessageDialog(this, "Books imported successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Confirmation message display if the user canceled or closed the dialog without selecting a file
            JOptionPane.showMessageDialog(this, "No file selected.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Method: navigateToAddBookPage
     * This allows the user to navigate to the "Add Book" page by displaying the dialog box.
     */
    private void navigateToAddBookPage() {
        // Method to display the add book dialog
        addBookDialog();
        System.out.println("Navigating to 'Add a Book' page...");
    }

    /**
     * Method: createDialog
     * created a dialog window with the specified title and returns the created dialog.
     */
    private JDialog createDialog(String title) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400); // Set the size as needed
        dialog.setLocationRelativeTo(this); // Center the dialog relative to the main frame
        return dialog;
    }
    /**
     * Method to display the add book dialog menu.
     * This method creates a dialog window with input fields for the user to add books individually.
     * It contains also contains save/cancel buttons. when the save button is selected,
     * the book's information is retrieved from the input fields,
     * a new book object is created, added to the LMS library, and a confirmation message is displayed for the user.
     */
    private void addBookDialog() {
        // Create a new dialog window
        JDialog dialog = createDialog("Add Book");

        // Create panel to hold input fields
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add labels and text fields for book information
        JTextField idField = new JTextField();
        addLabelAndTextField(inputPanel, "Please enter book ID: ", idField);
        JTextField titleField = new JTextField();
        addLabelAndTextField(inputPanel, "Please enter book title: ", titleField);
        JTextField authorField = new JTextField();
        addLabelAndTextField(inputPanel, "Please enter book Author: ", authorField);
        JTextField barcodeField = new JTextField();
        addLabelAndTextField(inputPanel, "Please enter barcode: ", barcodeField);

        // create panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(event -> {

            // Retrieve input values
            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String author = authorField.getText();
            String barcode = barcodeField.getText();

            // Create a new book object
            Book book = new Book(id, title, author, barcode, true, null, null);

            // Add the book to the library
            library.addBook(book);

            // Display a confirmation message
            JOptionPane.showMessageDialog(dialog, "Book saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Close the dialog after saving
            dialog.dispose();
        });
        buttonPanel.add(saveButton);

        // Add cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> dialog.dispose());
        buttonPanel.add(cancelButton);

        // Add input panel and button panel to dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Make the dialog visible
        dialog.setVisible(true);
    }
    /**
     * Method to add a label and text field to a panel.
     * This method creates a label with the specified text and associates it with the provided text field,
     * then adds both components to the given panel.
     */
    private void addLabelAndTextField(JPanel panel, String labelText, JTextField textField) {
        panel.add(new JLabel(labelText));
        panel.add(textField);
    }

    /**
     * Method to remove a book from the library by its barcode.
     * This method iterates through the list of books in the LMS library and removes the book with the barcode.
     * it then saves the changes in the database.
     * @param barcode The barcode of the book to be removed.
     * @return true if the book is successfully removed, false if the book with the specified barcode is not found.
     */
    private boolean removeBookByBarcode(String barcode) {
        Book bookToRemove = null;
        for (Book book : library.getBooks()) {
            if (book.getBarcode().equals(barcode)) {
                bookToRemove = book;
                break;
            }
        }
        if (bookToRemove != null) {
            library.getBooks().remove(bookToRemove);
            library.saveBooksToFile(); // Save changes to file after removing a book
            return true;
        } else {
            return false; // Return false if the book with the specified barcode is not found
        }
    }
    /**
     * Method to navigate to "Remove a Book by barcode" page
     */
    private void navigateToRemoveByBarcodePage() {
        // Create a dialog window for removing a book by barcode
        JDialog dialog = new JDialog(this, "Remove Book by Barcode", true);
        dialog.setLayout(new BorderLayout());

        // Create panel to hold input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add label and text field for barcode input
        JTextField barcodeField = new JTextField();
        inputPanel.add(new JLabel("Please enter barcode: "));
        inputPanel.add(barcodeField);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add remove button
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(event -> {
            String barcode = barcodeField.getText();
            boolean removed = removeBookByBarcode(barcode);
            if (removed) {
                JOptionPane.showMessageDialog(dialog, "Book removed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Book with barcode " + barcode + " not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose(); // Close the dialog after removing the book
        });
        buttonPanel.add(removeButton);

        // Add cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> dialog.dispose());
        buttonPanel.add(cancelButton);

        // Add input panel and button panel to dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog size and visibility
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Method to remove a book by its title
     */
    private boolean removeBookByTitle(String title) {
        // Convert the title to lowercase for case-insensitive comparison
        String lowercaseTitle = title.toLowerCase();

        // Get the list of books from the library
        List<Book> books = library.getBooks();
        if (books != null) {

            // Create an iterator to iterate over the list
            Iterator<Book> iterator = books.iterator();
            while (iterator.hasNext()) {
                Book book = iterator.next();
                // Compare the lowercase titles
                if (book.getTitle().toLowerCase().equals(lowercaseTitle)) {
                    iterator.remove(); // Remove the book from the list
                    library.saveBooksToFile(); // Save the updated list to the file
                    return true;
                }
            }
        }
        return false; // returns book not found or list is empty
    }

    /**
     * Method to navigate to "Remove a book by title" page
     * This segment allows the user to remove a book by its title name.
     */
    private void navigateToRemoveByTitlePage() {
        // Create a dialog window for removing a book by title
        JDialog dialog = new JDialog(this, "Remove Book by Title", true);
        dialog.setLayout(new BorderLayout());

        // Create menu panel to hold input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add label and text field for title input from the user
        JTextField titleField = new JTextField();
        inputPanel.add(new JLabel("Please enter Title: "));
        inputPanel.add(titleField);

        //Create menu panel for all the button options
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add remove button
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(event -> {
            String title = titleField.getText(); // Get the title input
            boolean removed = removeBookByTitle(title); // Call removeBookByTitle with the entered title
            if (removed) {
                JOptionPane.showMessageDialog(dialog, "Book removed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Book with Title " + title + " not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose();
        });
        buttonPanel.add(removeButton);

        // Add cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> dialog.dispose());
        buttonPanel.add(cancelButton);

        // Add input panel and button panel to dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog size and visibility
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    /**
     * Method to check out a book by its title.
     * it returns true if the book was successfully checked out, or false otherwise.
     */
    private boolean checkOutBook(String title) {
        // Call the checkOutBook method from the LMS library and return true if a book object is returned
        // (indicating the book was successfully checked out), or false.

        return library.checkOutBook(title) != null;
    }
    /**
     * Method to navigate to the "Check out a book" page.
     * This method displays a dialog window for the user to enter the title of the book they wish to check out.
     * once  clicking the "Check Out" button, the LMS system attempts to proceed with the check-out process
     * If check-out is successful, a  success message will display including the due date for the book to be returned.
     * If the book is already checked out or not found, an error message is also displayed.
     */
    private void navigateToCheckOutPage() {
        JDialog dialog = new JDialog(this, "Check Out a Book", true);
        dialog.setLayout(new BorderLayout());

        // Create panel to hold input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add label and text field for title input
        JTextField titleField = new JTextField();
        inputPanel.add(new JLabel("Please enter title to check out: "));
        inputPanel.add(titleField);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add check-out button
        JButton checkOutButton = new JButton("Check Out");
        checkOutButton.addActionListener(event -> {
            String title = titleField.getText();
            boolean checkedOut = checkOutBook(title);
            if (checkedOut) {
                Book checkedOutBook = library.findBookByTitle(title);
                LocalDate dueDate = checkedOutBook.getDueDate();
                JOptionPane.showMessageDialog(dialog, "Book checked out successfully.\nDue date: " + dueDate, "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Book with title " + title + " is already checked out or not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(checkOutButton);

        // Add cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> dialog.dispose());
        buttonPanel.add(cancelButton);

        // Add input panel and button panel to dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog size and visibility
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    /**
     * Method to check in a book with its title.
     * this method updates the status of a book with the specified title to "checked-in" in the LMS library database.
     * If the book is successfully checked-in, the availability status is updated, and due date is set to null.
     * If the book is already checked in, an error message will be displayed.
     */
    private boolean checkInBook(String title) {
        // call checkInBook method from the library
        Book bookToCheckIn = library.findBookByTitle(title);

        if (bookToCheckIn != null && !bookToCheckIn.isAvailable()) {
            // Update book status to "check in"
            bookToCheckIn.setAvailable(true);
            // Set due date to null (this indicates that the book is no longer checked out)
            bookToCheckIn.setDueDate(null); // set due date to null
            // Display success message
            JOptionPane.showMessageDialog(null, "Book checked in successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else if (bookToCheckIn != null) {
            // Display error message if the book is already checked in
            JOptionPane.showMessageDialog(null, "Book with title " + title + " is already checked in", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Display error message if book is not found in the database
            JOptionPane.showMessageDialog(null, "Book with title " + title + " not found", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    /**
     * Method to navigate to "Check in a book" page.
     * This method display a dialog box where the user can input the title of the book they wish to check in.
     * when the button "check in" is selected, the specified book is checked in by calling the checkInBook method from the library class
     * the dialog box closes if the operation is successful.
     */
    private void navigateToCheckInPage() {
        JDialog dialog = new JDialog(this, "Check In a Book", true);
        dialog.setLayout(new BorderLayout());

        // Create panel to hold input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add label and text field for title input
        JTextField titleField = new JTextField();
        inputPanel.add(new JLabel("Please enter title to check in: "));
        inputPanel.add(titleField);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add check in button
        JButton checkInButton = new JButton("Check in");
        checkInButton.addActionListener(event -> {
            String title = titleField.getText();
            checkInBook(title);
            dialog.dispose();
        });
        buttonPanel.add(checkInButton);

        // Add cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> dialog.dispose());
        buttonPanel.add(cancelButton);

        // Add input panel and button panel to dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog size and visibility
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    /**
     * Method to navigate to display the books saved in the Library Management System database.
     */
    private void navigateToDisplayDatabasePage() {
        // Call the displayDatabase() method to update the library data if needed
        library.displayDatabase();

        // Create a new dialog window to display the database contents
        JDialog dialog = new JDialog(this, "Library Database", true);
        dialog.setLayout(new BorderLayout());

        // Create a text area to display the database contents
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false); // Make the text area read-only

        // Retrieve the database contents from the library and format them with labels
        StringBuilder databaseContent = new StringBuilder();
        for (Book book : library.getBooks()) {
            databaseContent.append("Book ID: ").append(book.getId()).append(", ")
                    .append("Title: ").append(book.getTitle()).append(", ")
                    .append("Author: ").append(book.getAuthor()).append(", ")
                    .append("Barcode: ").append(book.getBarcode()).append("\n");
        }
        textArea.setText(databaseContent.toString());

        // Add the text area to a scroll pane to enable scrolling
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Add the scroll pane to the dialog
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Set dialog size and visibility
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Method to format book information from a Book object according to the desired format
     * This method uses the getters from the Book class to retrieve book information.
     */
    private String formatBookInfo(Book book) {
        return String.format("%d,%s,%s,%s", book.getId(), book.getTitle(), book.getAuthor(), book.getBarcode());
    }
    /**
     * Method to confirm exit of the LMS application
     */
    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }
}
