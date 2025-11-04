package org.example.sample7;

/**
 * Represents a book in the bookstore, matching the detailed fields from the plan.
 * We use a Java Record for a concise, immutable data carrier.
 */
public record BookstoreBook(
    long id,
    String title,
    String author,
    String isbn,
    int publishedYear
) {}
