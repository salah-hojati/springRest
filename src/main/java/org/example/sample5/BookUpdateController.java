package org.example.sample5;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/books") // We add our new PUT method to the existing "/books" path
public class BookUpdateController {

    // Use a ConcurrentHashMap for a mutable, thread-safe in-memory database
    private final Map<Long, BookstoreBook> bookDatabase = new ConcurrentHashMap<>();

    // Constructor to initialize the database
    public BookUpdateController() {
        // Pre-populate with some data
        bookDatabase.put(1L, new BookstoreBook(1, "Harry Potter and the Philosopher's Stone", "J.K. Rowling", "9780439708180", 1997));
        bookDatabase.put(2L, new BookstoreBook(2, "The Hobbit", "J.R.R. Tolkien", "9780547928227", 1937));
        bookDatabase.put(3L, new BookstoreBook(3, "1984", "George Orwell", "9780451524935", 1949));
        bookDatabase.put(4L, new BookstoreBook(4, "The Lord of the Rings", "J.R.R. Tolkien", "9780618640157", 1954));
        bookDatabase.put(5L, new BookstoreBook(5, "Harry Potter and the Chamber of Secrets", "J.K. Rowling", "9780439064873", 1998));
        bookDatabase.put(6L, new BookstoreBook(6, "Animal Farm", "George Orwell", "9780451526342", 1945));
    }

    /**
     * Implements Scenario 4: Updating a resource with a full replacement (PUT).
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookstoreBook> updateBook(
            @PathVariable long id,
            @Valid @RequestBody UpdateBookRequest updateRequest
    ) {
        // 1. Check if the resource to be updated actually exists.
        if (!bookDatabase.containsKey(id)) {
            // If not, return 404 Not Found, as per the scenario.
            return ResponseEntity.notFound().build();
        }

        // 2. Create the updated book object.
        // The ID from the URL path is the source of truth, not any ID that might be in the body.
        BookstoreBook updatedBook = new BookstoreBook(
            id,
            updateRequest.title(),
            updateRequest.author(),
            updateRequest.isbn(),
            updateRequest.publishedYear()
        );

        // 3. Replace the old book with the new one in the database.
        bookDatabase.put(id, updatedBook);

        // 4. Return 200 OK with the newly updated resource in the body.
        return ResponseEntity.ok(updatedBook);
    }
}
