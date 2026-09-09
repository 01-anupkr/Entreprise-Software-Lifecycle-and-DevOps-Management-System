package com.neuroforge.neuroforge_backend.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.neuroforge.neuroforge_backend.entity.User;
import com.neuroforge.neuroforge_backend.repository.UserRepository;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuditEventService auditEventService;
    public UserService(UserRepository userRepository, AuditEventService auditEventService) {
        this.userRepository = userRepository;
        this.auditEventService = auditEventService;
    }
    public User createUser(User user) {
        User saved = userRepository.save(user);
        auditEventService.record("CREATED", "USER", saved.getEmail());
        return saved;
    }
    public List<User> getAllUsers() { return userRepository.findAll(); }
    public Optional<User> getUserById(Integer id) { return userRepository.findById(id); }
    public Optional<User> updateUser(Integer id, User replacement) {
        return userRepository.findById(id).map(user -> {
            user.setName(replacement.getName());
            user.setEmail(replacement.getEmail());
            user.setPassword(replacement.getPassword());
            user.setRole(replacement.getRole());
            User saved = userRepository.save(user);
            auditEventService.record("UPDATED", "USER", saved.getEmail());
            return saved;
        });
    }
    public Optional<User> patchUser(Integer id, User changes) {
        return userRepository.findById(id).map(user -> {
            if (changes.getName() != null) user.setName(changes.getName());
            if (changes.getEmail() != null) user.setEmail(changes.getEmail());
            if (changes.getPassword() != null) user.setPassword(changes.getPassword());
            if (changes.getRole() != null) user.setRole(changes.getRole());
            User saved = userRepository.save(user);
            auditEventService.record("PATCHED", "USER", saved.getEmail());
            return saved;
        });
    }
    public boolean deleteUser(Integer id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        auditEventService.record("DELETED", "USER", String.valueOf(id));
        return true;
    }
}
