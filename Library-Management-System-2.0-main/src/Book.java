import java.time.LocalDate;

/**
 * Author: Candy Torres
 * Course: Software Development I - CEN 3024C
 * Date: March 24, 2024.
 * Class Name: Book
 * Description: Represents a book with an ID, title, author,
 * barcode, availability, checkout date, and due date.
 */
public class Book {
    // Fields
    private final int id;
    private final String title;
    private final String author;
    private final String barcode;
    private boolean available;
    private LocalDate dueDate;
    private LocalDate checkoutDate;

    // Constructor
    public Book(int id, String title, String author, String barcode, boolean available, LocalDate dueDate, LocalDate checkoutDate) {

        this.id = id;
        this.title = title;
        this.author = author;
        this.barcode = barcode;
        this.available = available;
        this.dueDate = dueDate;
        this.checkoutDate = checkoutDate;
    }

    // Getters
    public String getBarcode() {
        return barcode;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public int getId() {
        return id;
    }

    // Utility methods

    /**
     * Convert the book data to a string format suitable for saving to a file.
     *
     * @return The book data as a formatted string.
     */
    public String toFileString() {
        return id + "," + title + "," + author + "," + barcode + "," + available + "," + (dueDate != null ? dueDate.toString() : "null");
    }

    @Override
    public String toString() {
        return "Book [Book ID: " + id + ", Barcode Number: " + barcode + ", Title: " + title + ", Author: " + author + "]";
    }
}
