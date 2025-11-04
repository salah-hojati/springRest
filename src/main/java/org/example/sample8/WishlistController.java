package org.example.sample8;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WishlistController {

    // This is Salah's personal wishlist. Only he should be able to see it.
    private final List<String> salahsWishlist = List.of("The Silmarillion", "Dune Messiah");

    @GetMapping("/wishlist")
    public ResponseEntity<List<String>> getWishlist() {
        // Get the current user's authentication details from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // --- This is the AUTHORIZATION check ---
        // We check if the authenticated user is the owner of the resource.
        if ("salah".equals(currentUsername)) {
            // Success (200 OK): The user is authorized.
            return ResponseEntity.ok(salahsWishlist);
        } else {
            // Error (403 Forbidden): The user is authenticated but not authorized for this resource.
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
