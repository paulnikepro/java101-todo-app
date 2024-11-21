package org.paulnikepro.hw4.todoapp.dto;

import org.paulnikepro.hw4.todoapp.model.enums.Priority;
import org.paulnikepro.hw4.todoapp.model.enums.Status;

import java.time.LocalDateTime;

public record TodoResponseDto(
        Long id,
        String title,
        String description,
        LocalDateTime dueDate,
        Priority priority,
        Status status,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        Long userId
) { }
