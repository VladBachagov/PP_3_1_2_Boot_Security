package ru.kata.spring.boot_security.demo.errors;

public class UsernameNotUniqueException extends IllegalArgumentException {
    public UsernameNotUniqueException(String message) {
        super(message);
    }
}