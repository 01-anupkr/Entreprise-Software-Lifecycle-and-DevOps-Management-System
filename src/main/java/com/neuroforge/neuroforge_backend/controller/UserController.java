package com.neuroforge.neuroforge_backend.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neuroforge.neuroforge_backend.entity.User;
import com.neuroforge.neuroforge_backend.service.UserService;
@RestController @RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public User createUser(@RequestBody User user) { return userService.createUser(user); }
    @GetMapping public List<User> getAllUsers() { return userService.getAllUsers(); }
    @GetMapping("/{id}") public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        return userService.updateUser(id, user).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PatchMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<User> patchUser(@PathVariable Integer id, @RequestBody User user) {
        return userService.patchUser(id, user).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        return userService.deleteUser(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
