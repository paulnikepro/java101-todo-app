package org.paulnikepro.hw4.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.paulnikepro.hw4.todoapp.model.enums.Priority;
import org.paulnikepro.hw4.todoapp.model.enums.Status;

import java.time.LocalDateTime;

public record TodoUpdateDto(

        @NotBlank(message = "Title cannot be blank")
        @Length(max = 100, message = "Title should not exceed 100 characters")
        String title,

        @Size(max = 500, message = "Description should be up to 500 characters")
        String description,

        @NotNull(message = "Due date is required")
        LocalDateTime dueDate,

        Priority priority,

        @NotNull(message = "Status is required")
        Status status
) { }
