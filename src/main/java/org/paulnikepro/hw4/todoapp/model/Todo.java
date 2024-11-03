package org.paulnikepro.hw4.todoapp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.paulnikepro.hw4.todoapp.model.enums.Priority;
import org.paulnikepro.hw4.todoapp.model.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "todos")
@SQLDelete(sql = "UPDATE todos SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class Todo {

    // Unique identifier for the Todo item
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Short description or title of the task
    private String title;

    // Detailed description of the task
    private String description;

    // When the task is due
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    // When the task was created
    private LocalDateTime createdDate;

    // When the task was last updated
    private LocalDateTime updatedDate;

    // The ID of the user who owns the task. Hardcode value 1 for now. We will replace it when Security is implemented.
    private Long userId = 1L;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskHistory> taskHistories = new ArrayList<>();

    @Column(nullable = false)
    private boolean isDeleted = false;
}
