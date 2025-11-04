package org.example.sample2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/books") // The endpoint from your plan
public class BookstoreController {

    private final List<BookstoreBook> bookDatabase;

    // Constructor to initialize an in-memory database of books
    public BookstoreController() {
        this.bookDatabase = List.of(
            new BookstoreBook(1, "Harry Potter and the Philosopher's Stone", "J.K. Rowling", "9780439708180", 1997),
            new BookstoreBook(2, "The Hobbit", "J.R.R. Tolkien", "9780547928227", 1937),
            new BookstoreBook(3, "1984", "George Orwell", "9780451524935", 1949),
            new BookstoreBook(4, "The Lord of the Rings", "J.R.R. Tolkien", "9780618640157", 1954),
            new BookstoreBook(5, "Harry Potter and the Chamber of Secrets", "J.K. Rowling", "9780439064873", 1998),
            new BookstoreBook(6, "Animal Farm", "George Orwell", "9780451526342", 1945)
        );
    }

    /**
     * Implements Scenario 1: Getting a list of all books with optional filtering, sorting, and pagination.
     */
    @GetMapping
    public List<BookstoreBook> getBooks(
            // --- Filtering ---
            @RequestParam(required = false) String author,

            // --- Sorting ---
            @RequestParam(required = false, defaultValue = "title") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order,

            // --- Pagination ---
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        // Start with a stream of all books
        Stream<BookstoreBook> bookStream = bookDatabase.stream();

        // 1. Apply Filtering
        if (author != null && !author.isBlank()) {
            bookStream = bookStream.filter(book -> book.author().equalsIgnoreCase(author));
        }

        // 2. Apply Sorting
        Comparator<BookstoreBook> comparator = switch (sort.toLowerCase()) {
            case "author" -> Comparator.comparing(BookstoreBook::author);
            case "publishedyear" -> Comparator.comparing(BookstoreBook::publishedYear);
            default -> Comparator.comparing(BookstoreBook::title);
        };

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        // The sorted() method on a stream is an "intermediate operation"
        bookStream = bookStream.sorted(comparator);

        // 3. Apply Pagination
        // skip() and limit() are also intermediate operations
        bookStream = bookStream.skip((long) page * limit).limit(limit);

        // 4. Collect the results into a list
        // collect() is a "terminal operation" that triggers the stream processing
        return bookStream.collect(Collectors.toList());
    }
}
