package org.paulnikepro.hw4.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import java.time.LocalDateTime;
import org.paulnikepro.hw4.todoapp.model.enums.Priority;
import org.paulnikepro.hw4.todoapp.model.enums.Status;

public record TodoCreateDto(

        @NotBlank(message = "Title cannot be blank")
        @Length(max = 100, message = "Title must be 100 characters or fewer")
        String title,

        @Size(max = 500, message = "Description should not exceed 500 characters")
        String description,

        @NotNull(message = "Due date is required")
        LocalDateTime dueDate,

        Priority priority,

        Status status
) {
        public Priority priority() {
                return priority != null ? priority : Priority.MEDIUM;
        }

        public Status status() {
                return status != null ? status : Status.PENDING;
        }
}
