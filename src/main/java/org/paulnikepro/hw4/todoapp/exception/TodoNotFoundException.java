package org.paulnikepro.hw4.todoapp.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(Long id) {
        super("Todo item with id " + id + " not found.");
    }
}
