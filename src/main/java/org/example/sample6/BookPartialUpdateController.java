package org.example.sample6;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/books") // We add our new PATCH method to the existing "/books" path
public class BookPartialUpdateController {

    // Use a ConcurrentHashMap for a mutable, thread-safe in-memory database
    private final Map<Long, BookstoreBook> bookDatabase = new ConcurrentHashMap<>();

    // Constructor to initialize the database with some data
    public BookPartialUpdateController() {
        // Pre-populate with some data
        bookDatabase.put(1L, new BookstoreBook(1, "Harry Potter and the Philosopher's Stone", "J.K. Rowling", "9780439708180", 1997));
        bookDatabase.put(2L, new BookstoreBook(2, "The Hobbit", "J.R.R. Tolkien", "9780547928227", 1937));
        bookDatabase.put(3L, new BookstoreBook(3, "1984", "George Orwell", "9780451524935", 1949));
        bookDatabase.put(4L, new BookstoreBook(4, "The Lord of the Rings", "J.R.R. Tolkien", "9780618640157", 1954));
        bookDatabase.put(5L, new BookstoreBook(5, "Harry Potter and the Chamber of Secrets", "J.K. Rowling", "9780439064873", 1998));
        bookDatabase.put(6L, new BookstoreBook(6, "Animal Farm", "George Orwell", "9780451526342", 1945));
    }

    /**
     * Implements Scenario 5: Updating a resource with a partial update (PATCH).
     */
    @PatchMapping("/{id}")
    public ResponseEntity<BookstoreBook> updateBook(
            @PathVariable long id,
            @RequestBody PartialBookUpdateRequest updateRequest
    ) {
        // 1. Check if the resource to be updated exists.
        BookstoreBook existingBook = bookDatabase.get(id);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }

        // 2. Apply the updates from the request.
        // For each field in the DTO, check if it's present in the request.
        // If it is, update the corresponding field in the existing book.
        String title = (updateRequest.title() != null) ? updateRequest.title() : existingBook.title();
        String author = (updateRequest.author() != null) ? updateRequest.author() : existingBook.author();
        String isbn = (updateRequest.isbn() != null) ? updateRequest.isbn() : existingBook.isbn();
        Integer publishedYear = (updateRequest.publishedYear() != null) ? updateRequest.publishedYear() : existingBook.publishedYear();

        // 3. Create the updated book object.
        BookstoreBook updatedBook = new BookstoreBook(
            id,
            title,
            author,
            isbn,
            publishedYear
        );

        // 4. Replace the old book with the new one in the database.
        bookDatabase.put(id, updatedBook);

        // 5. Return 200 OK with the newly updated resource in the body.
        return ResponseEntity.ok(updatedBook);
    }
}
