import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookTest {
    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library("book.txt");
    }

    private void printTestTitle(String title) {
        System.out.println(title);
    }

    /**
     * 1. Add Books
     */

    @Test
    @DisplayName("Test adding books to the database")
    @Order(1)
    void testAddBookToDatabase() {
        printTestTitle("1. Test - Adding books");

        // Add book
        Book divergentBook = new Book(11, "Divergent", "Veronica Roth", "20245", true, null, null);
        library.addBook(divergentBook);

        // Retrieve the list of books from the library
        ArrayList<Book> booksInDatabase = library.getBooks();

        // Check if the added book is in the database
        assertTrue(booksInDatabase.stream().anyMatch(book -> book.getTitle().equals("Divergent")));

        // Print message for added book
        System.out.println("Book 'Divergent by Veronica Roth' has been added.");

        // Line separator
        System.out.println("----------------------------------");
    }


    /**
     * 2. Remove books by barcode
     */
    @Test
    @DisplayName("Test remove book by barcode")
    @Order(2)
    void testRemoveBookByBarcode() {
        printTestTitle("2. Test - Remove books by barcode");

        // Add a book
        library.addBook(new Book(16, "Harry Potter", "J.K. Rowling", "13579", true, null, null));

        // Remove a book by barcode
        library.removeBookByBarcode("13579");

        // Print message about the removed book
        System.out.println("Book with barcode 13579, Harry Potter, has been removed.");

        // Line separator
        System.out.println("----------------------------------");
    }

    /**
     * 3. Remove books by title
     */
    @Test
    @DisplayName("Test remove book by title")
    @Order(3)
    void testRemoveBookByTitle() {
        printTestTitle("3. Test - Remove books by title");

        // Add a book
        library.addBook(new Book(13, "The Great Gatsby", "F. Scott Fitzgerald", "98765", true, null, null));

        // Remove a book by title
        library.removeBookByTitle("The Great Gatsby");

        // Check if the book is removed from the LMS database
        assertFalse(library.getBooks().stream().anyMatch(book -> book.getTitle().equals("The Great Gatsby")));

        // Line separator
        System.out.println("----------------------------------");
    }

    /**
     * 4. Check Out Book
     */

    @Test
    @DisplayName("Test Checking Out a Book")
    @Order(4)
    void testCheckOutBook() {
        printTestTitle("4. Test - Check Out Book");

        // Add a book
        Book bookToAdd = new Book(14, "To Kill a Mockingbird", "Harper Lee", "12345", true, null, null);
        library.addBook(bookToAdd);

        // Check out the book
        library.checkOutBook("To Kill a Mockingbird");

        // Retrieve the book after checking out
        Book checkedOutBook = library.findBookByTitle("To Kill a Mockingbird");

        // Check if the book was checked out and the due date is not null
        assertNotNull(checkedOutBook, "Book should have been checked out");
        assertNotNull(checkedOutBook.getDueDate(), "Due date should not be null after checking out");

        // Line separator
        System.out.println("----------------------------------");
    }

    /**
     * 5. Check-in Books
     */
    @Test
    @DisplayName("Test Checking In a Book")
    @Order(5)
    void testCheckInBook() {
        printTestTitle("5. Test - Check-in Books");

        // Add a book
        library.addBook(new Book(15, "Pride and Prejudice", "Jane Austen", "24681", false, LocalDate.now().plusDays(7), LocalDate.now()));

        // Check in a book
        library.checkInBook("Pride and Prejudice");

        // Retrieve the book from the library
        Book checkedInBook = library.findBookByTitle("Pride and Prejudice");

        // Assert that the book is checked in and the due date is null
        assertNull(checkedInBook.getDueDate(), "Due date should be null after checking in");

        // Line separator
        System.out.println("----------------------------------");
    }
}
