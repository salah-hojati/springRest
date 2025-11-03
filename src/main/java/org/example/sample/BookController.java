package org.example.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/books") // Base path for all methods in this controller
public class BookController {

    // Using a thread-safe map as a simple in-memory database
    private final ConcurrentHashMap<Long, Book> books = new ConcurrentHashMap<>();
    // Using an atomic long for thread-safe ID generation
    private final AtomicLong counter = new AtomicLong();

    public BookController() {
        // Pre-populate with some data
        long id1 = counter.incrementAndGet();
        books.put(id1, new Book(id1, "The Hobbit", "J.R.R. Tolkien"));
        long id2 = counter.incrementAndGet();
        books.put(id2, new Book(id2, "1984", "George Orwell"));
    }

    // GET /api/books
    @GetMapping
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    // GET /api/books/1
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable long id) {
        Book book = books.get(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    // POST /api/books
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        long newId = counter.incrementAndGet();
        // Create a new book instance with the generated ID
        Book newBook = new Book(newId, book.title(), book.author());
        books.put(newId, newBook);
        return newBook;
    }

    // --- PUT (Update/Replace) ---
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable long id, @RequestBody Book updatedBook) {
        if (!books.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        // Create a new record with the path ID and new data, then replace it in the map
        Book bookToSave = new Book(id, updatedBook.title(), updatedBook.author());
        books.put(id, bookToSave);
        return ResponseEntity.ok(bookToSave);
    }

    // --- PATCH (Partial Update) ---
    @PatchMapping("/{id}")
    public ResponseEntity<Book> partiallyUpdateBook(@PathVariable long id, @RequestBody BookUpdateRequest updateRequest) {
        Book existingBook = books.get(id);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }

        // Check which fields are provided in the request and update them
        String newTitle = (updateRequest.title() != null) ? updateRequest.title() : existingBook.title();
        String newAuthor = (updateRequest.author() != null) ? updateRequest.author() : existingBook.author();

        Book patchedBook = new Book(id, newTitle, newAuthor);
        books.put(id, patchedBook);
        return ResponseEntity.ok(patchedBook);
    }

    // --- DELETE (Delete) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        if (books.remove(id) == null) {
            // If remove() returns null, the key didn't exist
            return ResponseEntity.notFound().build();
        }
        // Return 204 No Content, the standard for a successful deletion
        return ResponseEntity.noContent().build();
    }



}