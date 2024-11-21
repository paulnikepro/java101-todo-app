package org.paulnikepro.hw4.todoapp.repository;

import org.paulnikepro.hw4.todoapp.model.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {
    List<TaskHistory> findByTodoId(Long todoId);
}

