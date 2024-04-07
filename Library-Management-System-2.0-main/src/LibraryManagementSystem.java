import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Author: Candy Torres
 * Course: Software Development I - CEN 3024C
 * Date: March 24, 2024.
 * Class Name: LibraryManagementSystem
 * Description: This class represents the main entry point for the Library Management System (LMS) application.
 *              It allows users to interact with the library database by adding, removing,
 *              checking out, and checking in books.
 */

public class LibraryManagementSystem {


    /**
     * Main method to start the Library Management System application.
     * @param args Command-line arguments.
     */

    public static void main(String[] args) {
        Library library = new Library("books.txt"); // Create a Library object with a database file name
        Scanner scanner = new Scanner(System.in);

        // Load books from file on startup
        library.loadBooksFromFile("books.txt");

        // Add shutdown hook to save to file when program exits
        Runtime.getRuntime().addShutdownHook(new Thread(library::saveBooksToFile));

        boolean active = true;
        while (active) {
            displayMenu();
            System.out.print("Enter your selection: ");
            int userSelection = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (userSelection) {
                case 1:
                    addBook(scanner, library);
                    break;
                case 2:
                    removeBookByBarcode(scanner, library);
                    break;
                case 3:
                    removeBookByTitle(scanner, library);
                    break;
                case 4:
                    checkOutBook(scanner, library);
                    break;
                case 5:
                    checkInBook(scanner, library);
                    break;
                case 6:
                    displayDatabase(library);
                    break;
                case 7:
                    active = false;
                    System.out.println("Exiting the program");
                    break;
                default:
                    System.out.println("Invalid input, please try again");
            }

            System.out.println(); // this is to add  an empty line after each selection (visual purposes)
        }
        scanner.close();
    }

    /**
     * Display the main menu of the Library Management System.
     */
    private static void displayMenu() {
        System.out.println("""
            Make a selection
            1. Add a book
            2. Remove a book by barcode
            3. Remove a book by title
            4. Check out a book
            5. Check in a book
            6. Display the Contents of the Database
            7. Exit
            """);
    }

    /**
     * Add a book to the library database.
     * @param scanner Scanner object for user input.
     * @param library Library object that represents  the library database.
     */
    public static void addBook(Scanner scanner, Library library) {

        System.out.println("Please enter book ID: ");
        int bookID = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.println("Please enter book title: ");
        String bookTitle = scanner.nextLine();

        System.out.println("Please enter the book Author: ");
        String bookAuthor = scanner.nextLine();

        System.out.println("Please enter the book Barcode: ");
        String bookBarcode = scanner.nextLine(); // Assuming barcode is supplied by the user

        library.addBook(new Book(bookID, bookTitle, bookAuthor, bookBarcode, true, null, null));
    }

    /**
     * Remove a book from the library database by its barcode.
     * @param scanner Scanner object for user input.
     * @param library Library object representing the library database.
     */
    private static void removeBookByBarcode(Scanner scanner, Library library) {
        System.out.println("Enter the barcode of the book to remove: ");
        String removeBarcode = scanner.nextLine();
        library.removeBookByBarcode(removeBarcode);
    }

    /**
     * Remove a book from the library database by its title.
     * @param scanner Scanner object for user input.
     * @param library Library object representing the library database.
     */
    private static void removeBookByTitle(Scanner scanner, Library library) {
        System.out.println("Enter the title of the book to remove: ");
        String removeTitle = scanner.nextLine();
        library.removeBookByTitle(removeTitle);
    }

    /**
     * Check out a book from the library database by its title.
     * @param scanner Scanner object for user input.
     * @param library Library object representing the library database.
     */
    private static void checkOutBook(Scanner scanner, Library library) {
        System.out.println("Enter the title of the book to check out: ");
        String checkoutTitle = scanner.nextLine();
        Book checkedOutBook = library.checkOutBook(checkoutTitle);
        if (checkedOutBook != null) {
            // Print necessary information here
            System.out.println("Book '" + checkoutTitle + "' checked out successfully:");
            System.out.println(checkedOutBook);
            System.out.println("Checkout Date: " + checkedOutBook.getCheckoutDate());
            System.out.println("Due Date: " + checkedOutBook.getDueDate());
        }
    }
    /**
     * Check in a book to the library database by its title.
     * @param scanner Scanner object for user input.
     * @param library Library object representing the library database.
     */
    private static void checkInBook(Scanner scanner, Library library) {
        System.out.println("Enter the title of the book to check in: ");
        String checkInTitle = scanner.nextLine();
        library.checkInBook(checkInTitle);
    }

    /**
     * Display the contents of the library database in ascending order by book ID.
     * @param library Library object representing the library database.
     */
    private static void displayDatabase(Library library) {
        System.out.println("Displaying contents of the database in ascending order by book ID:");
        ArrayList<Book> sortedBooks = library.getBooks();
        sortedBooks.sort(Comparator.comparingInt(Book::getId));
        for (Book book : sortedBooks) {
            System.out.println(book);
        }
    }
}