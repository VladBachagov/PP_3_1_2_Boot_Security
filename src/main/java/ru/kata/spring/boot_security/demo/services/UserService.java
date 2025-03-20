package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    void save(User user);

    void deleteById(Long id);

    void update(User user);

    Optional<User> findByUsername(String username);
}