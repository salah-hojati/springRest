package org.example.sample4;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/books")
public class BookCreationController {

    private final List<BookstoreBook> bookDatabase = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @PostMapping
    public ResponseEntity<BookstoreBook> createBook(@Valid @RequestBody NewBookRequest newBookRequest) {
        // Generate a new unique ID
        long newId = idGenerator.incrementAndGet();

        // Create a new BookstoreBook from the NewBookRequest
        BookstoreBook newBook = new BookstoreBook(
            newId,
            newBookRequest.title(),
            newBookRequest.author(),
            newBookRequest.isbn(),
            newBookRequest.publishedYear()
        );

        // Save the new book to the database
        bookDatabase.add(newBook);

        // Build the URI for the new resource
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newId)
            .toUri();

        // Return the new book with a 201 Created status and the Location header
        return ResponseEntity.created(location).body(newBook);
    }
}
