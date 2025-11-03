package org.example.sample;

// A DTO for handling partial book updates.
// Fields can be null if they are not being updated.
public record BookUpdateRequest(String title, String author) {
}