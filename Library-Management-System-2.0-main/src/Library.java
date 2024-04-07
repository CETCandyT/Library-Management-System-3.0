import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.time.format.DateTimeFormatter;

/*
 * Author: Candy Torres
 * Course: Software Development I - CEN 3024C
 * Date: March 24, 2024
 * Class Name: Library
 * Description: Manages the library system, including adding,
 * removing, listing, and checking in/out books.
 */

public class Library {
    private final ArrayList<Book> books;
    private final String fileName;

    /*
     * Constructor for the Library class.
     * @param fileName The name of the file containing book data.
     */
    public Library(String fileName) {
        // load books from the file
        this.books = new ArrayList<>();
        this.fileName = fileName;
        loadBooksFromFile(fileName); // Call instance method to load books from file
    }

    /**
     * Method to add a book to the library.
     * @param book The book to be added.
     */
    public void addBook(Book book) {
        books.add(book); // Add the book to the list
        saveBooksToFile(); // Save the updated list to the file
    }
    /**
     * Method to add books from a file to the library.
     * @param filePath The path of the file containing book data.
     */
    public void addBooksFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    int bookID = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String author = parts[2];
                    String barcode = parts[3];
                    boolean available = Boolean.parseBoolean(parts[4]);
                    try {
                        LocalDate dueDate = LocalDate.parse(parts[5]);
                        Book book = new Book(bookID, title, author, barcode, available, dueDate, null);
                        books.add(book);
                    } catch (DateTimeParseException e) {
                        System.out.println("Error parsing due date for book: " + title);
                    }
                } else {
                    System.out.println("Invalid book entry: " + line);
                }
            }
            System.out.println("Books added successfully from file: " + filePath);
        } catch (IOException e) {
            System.out.println("Error adding books from file: " + e.getMessage());
        }
    }

    /**
     * Method to remove a book from the library by its barcode.
     * @param barcode The barcode of the book to be removed.
     */
    public void removeBookByBarcode(String barcode) {
        Iterator<Book> iterator = books.iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.getBarcode().equals(barcode)) {
                iterator.remove();
                System.out.println("Book with barcode " + barcode + " has been removed.");
                saveBooksToFile(); // Save changes to file after removing a book
                return;
            }
        }
        System.out.println("Book with barcode " + barcode + " not found.");
    }

    /**
     * Method to remove a book from the library by its title.
     * @param title The title of the book to be removed.
     */
    public void removeBookByTitle(String title) {
        Iterator<Book> iterator = books.iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.getTitle().equals(title)) { // Compare the book's title with the input title
                iterator.remove();
                System.out.println("Book with title '" + title + "' has been removed.");
                saveBooksToFile(); // Save changes to file after removing a book
                return;
            }
        }
        System.out.println("Book with title '" + title + "' not found.");
    }


    /**
     * Method to display all books in the library.
     */
    public void listAllBooks() {
        System.out.println("Listing all books:");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    /**
     * Method to display the contents of the database.
     */
    public void displayDatabase() {
        System.out.println("Displaying contents of the database:");
        for (Book book : books) {
            System.out.println(book);
        }
    }


    /**
     * Method to save the books to a file.
     */
    public void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Book book : books) {
                writer.write(book.toFileString());
                writer.newLine();
            }
            System.out.println("Books saved successfully to file.");
        } catch (IOException e) {
            System.out.println("Error saving books to file: " + e.getMessage());
        }
    }

    /**
     * Method to load books from a file into the library.
     * @param fileName The name of the file containing book data.
     */
    public void loadBooksFromFile(String fileName) {
        // Define the date format expected in the file
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            books.clear(); // clear existing books before loading file
            String line;
            while ((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    int bookID = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String author = parts[2];
                    String barcode = parts[3];
                    boolean available = Boolean.parseBoolean(parts[4]);
                    LocalDate dueDate;
                    if (!available) {
                        dueDate = LocalDate.parse(parts[5], formatter); // use formatter
                    } else {
                        dueDate = null;
                    }
                    books.add(new Book(bookID, title, author, barcode, available, dueDate, null));
                } else  {
                    System.out.println("Invalid book entry: " + line);
                }
            }
            System.out.println("Books loaded successfully from file.");
        } catch (IOException e) {
            System.out.println("Error loading books from file: " + e.getMessage());
        }
    }

    /**
     * Method to find a book by its title.
     * @param title The title of the book to be found.
     * @return The book with the specified title, or null if not found.
     */
    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Method to check out a book from the library
     * @param title The title of the book to be checked out.
     * @return The checked out book, or null if not found or not available.
     */
    public Book checkOutBook(String title) {
        Book book = findBookByTitle(title);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            book.setCheckoutDate(LocalDate.now());
            LocalDate dueDate = calculateDueDate(book.getCheckoutDate()); // Calculate due date
            book.setDueDate(dueDate); // Set due date
            return book; // Return the checked-out book
        } else if (book != null) {
            System.out.println("Book '" + title + "' is not available for checkout.");
        } else {
            System.out.println("Book '" + title + "' not found.");
        }
        return null; // Return null if the book is not available for checkout or not found
    }

    /**
     * Method to check in a book to the library.
     * @param title The title of the book to be checked in.
     */
    public void checkInBook(String title) {
        Book book = findBookByTitle(title);
        if (book != null && !book.isAvailable()) {
            book.setAvailable(true);
            book.setCheckoutDate(null);
            book.setDueDate(null);
            System.out.println("Book '" + title + "' checked in successfully.");
            saveBooksToFile(); // Save changes to file after checking in a book
        }
    }

    /**
     * Method to calculate the due date for checking out a book.
     * @param checkoutDate The checkout date of the book.
     * @return The due date of the book.
     */
    private LocalDate calculateDueDate(LocalDate checkoutDate) {
        return checkoutDate.plusWeeks(4);
    }

    /**
     * Getter method for the list of books.
     * @return The list of books in the library.
     */
    public ArrayList<Book> getBooks() {
        return books;
    }
}