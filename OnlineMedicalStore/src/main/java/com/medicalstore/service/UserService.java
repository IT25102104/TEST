package com.medicalstore.service;

import com.medicalstore.entity.User;
import com.medicalstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // In a real app, encrypt password before saving
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean authenticate(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        // Simple check (no encryption yet)
        return user.isPresent() && user.get().getPassword().equals(password);

        public List<User> getAllUsers() {
            return userRepository.findAll();
        }

        public long countUsers() {
            return userRepository.count();
        }

        public void deleteUser(Long id) {
            userRepository.deleteById(id);
        }
    }

}