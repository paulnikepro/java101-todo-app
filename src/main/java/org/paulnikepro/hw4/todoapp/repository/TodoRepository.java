package org.paulnikepro.hw4.todoapp.repository;

import org.paulnikepro.hw4.todoapp.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
