package org.dharmik.usermanegmentapp.Controller;

import lombok.AllArgsConstructor;
import org.dharmik.usermanegmentapp.Entity.User;
import org.dharmik.usermanegmentapp.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userEntryService;

    @GetMapping("/")
    public List<User> getAllUsers() {
        return userEntryService.getAll();
    }

    @PostMapping("/register")
    public User saveEntry(@RequestBody User user) {
        user.setDate(LocalDateTime.now());
        // FIX: Renamed method call to match service.
        userEntryService.saveEntry(user);
        return user;
    }

    @PutMapping("/{id}")
    public User updateEntry(@PathVariable Integer id, @RequestBody User updatedUser) {
        User existingUser = userEntryService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(updatedUser.getPassword());
        }
        userEntryService.saveEntry(existingUser);
        return existingUser;
    }

    @GetMapping("/{id}")
    public User getEntryById(@PathVariable Integer id) {
        return userEntryService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id) {
        userEntryService.deleteEntry(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}