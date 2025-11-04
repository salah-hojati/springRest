package org.example.sample3;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books") // The endpoint from your plan
public class BookDetailsController {

    private final List<BookstoreBook> bookDatabase;

    // Constructor to initialize an in-memory database of books
    public BookDetailsController() {
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
     * Implements Scenario 2: Getting a single book by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookstoreBook> getBookById(@PathVariable long id) {
        Optional<BookstoreBook> book = bookDatabase.stream()
                .filter(b -> b.id() == id)
                .findFirst();

        return book.map(ResponseEntity::ok) // Book found: return 200 OK with the book
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Book not found: return 404 Not Found
    }
}
