package org.example.sample7;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/books") // We add our new DELETE method to the existing "/books" path
public class BookDeletionController {

    // Use a ConcurrentHashMap for a mutable, thread-safe in-memory database
    private final Map<Long, BookstoreBook> bookDatabase = new ConcurrentHashMap<>();

    // Constructor to initialize the database
    public BookDeletionController() {
        // Pre-populate with some data that we can delete
        bookDatabase.put(1L, new BookstoreBook(1, "Harry Potter and the Philosopher's Stone", "J.K. Rowling", "9780439708180", 1997));
        bookDatabase.put(2L, new BookstoreBook(2, "The Hobbit", "J.R.R. Tolkien", "9780547928227", 1937));
        bookDatabase.put(3L, new BookstoreBook(3, "1984", "George Orwell", "9780451524935", 1949));
        // We will try to delete book with ID 6
        bookDatabase.put(6L, new BookstoreBook(6, "Animal Farm", "George Orwell", "9780451526342", 1945));
    }

    /**
     * Implements Scenario 6: Deleting a resource by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        // The remove() method on a Map is perfect for this.
        // It removes the entry and returns the value that was associated with the key.
        // If no entry existed for the key, it returns null.
        BookstoreBook removedBook = bookDatabase.remove(id);

        if (removedBook == null) {
            // If remove() returns null, the book to delete didn't exist.
            // Return 404 Not Found, as per the scenario.
            return ResponseEntity.notFound().build();
        }

        // If a book was successfully removed, return 204 No Content.
        // This is the standard, clean response for a successful deletion.
        return ResponseEntity.noContent().build();
    }
}
