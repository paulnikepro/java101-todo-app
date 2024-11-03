package org.paulnikepro.hw4.todoapp.service;

import lombok.RequiredArgsConstructor;
import org.paulnikepro.hw4.todoapp.dto.TodoCreateDto;
import org.paulnikepro.hw4.todoapp.dto.TodoResponseDto;
import org.paulnikepro.hw4.todoapp.dto.TodoUpdateDto;
import org.paulnikepro.hw4.todoapp.model.TaskHistory;
import org.paulnikepro.hw4.todoapp.model.Todo;
import org.paulnikepro.hw4.todoapp.model.enums.Status;
import org.paulnikepro.hw4.todoapp.mapper.TaskHistoryMapper;
import org.paulnikepro.hw4.todoapp.mapper.TodoMapper;
import org.paulnikepro.hw4.todoapp.repository.TaskHistoryRepository;
import org.paulnikepro.hw4.todoapp.repository.TodoRepository;
import jakarta.transaction.Transactional;
import org.paulnikepro.hw4.todoapp.dto.TaskHistoryResponseDto;
import org.paulnikepro.hw4.todoapp.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final TaskHistoryRepository taskHistoryRepository;
    private final TaskHistoryMapper taskHistoryMapper;

    @Override
    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return todoRepository.existsById(id);
    }

    public TodoResponseDto findById(Long id) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        // Implement this method to convert Todo to TodoResponseDto
        // Or throw an exception, depending on your preference
        return todoOptional.map(this::convertToDto).orElse(null);
    }

    private TodoResponseDto convertToDto(Todo todo) {
        return new TodoResponseDto(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getDueDate(),
                todo.getPriority(),
                todo.getStatus(),
                todo.getCreatedDate(),
                todo.getUpdatedDate(),
                todo.getUserId()
        );
    }

    @Override
    public TodoResponseDto save(TodoCreateDto todoCreateDto) {
        Todo todo = todoMapper.toEntity(todoCreateDto);
        todo.setStatus(Status.PENDING);
        todo.setUserId(1L);
        todo.setCreatedDate(LocalDateTime.now());
        todo.setUpdatedDate(LocalDateTime.now());
        Todo savedTodo = todoRepository.save(todo);

        // Initial history entry for creation
        TaskHistory history = new TaskHistory();
        history.setTodo(savedTodo);
        history.setOldState("N/A");
        history.setNewState(savedTodo.getStatus().toString());
        history.setChangeDate(LocalDateTime.now());
        history.setChangedBy("System");
        taskHistoryRepository.save(history);

        return todoMapper.toResponseDto(savedTodo);
    }

    @Override
    @Transactional
    public TodoResponseDto update(Long id, TodoUpdateDto todoUpdateDto) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo with id " + id + " not found."));

        // Keep a copy of the old state for history
        String oldState = existingTodo.toString();

        // Update fields from DTO
        todoMapper.updateEntityFromDto(todoUpdateDto, existingTodo);
        existingTodo.setUpdatedDate(LocalDateTime.now());

        // Create history entry
        if (!oldState.equals(existingTodo.toString())) {
            TaskHistory history = new TaskHistory();
            history.setTodo(existingTodo);
            history.setOldState(oldState);
            history.setNewState(existingTodo.toString());
            history.setChangeDate(LocalDateTime.now());
            history.setChangedBy("User");
            taskHistoryRepository.save(history);
        }

        Todo savedTodo = todoRepository.save(existingTodo);
        return todoMapper.toResponseDto(savedTodo);
    }

    @Override
    public void delete(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo with id " + id + " not found."));

        checkSoftDelete(todo, "Todo with id " + id + " is already deleted.");

        // Set deletion flag and update date
        todo.setDeleted(true);
        todo.setUpdatedDate(LocalDateTime.now());
        todoRepository.save(todo);

        // Record deletion in history
        TaskHistory history = new TaskHistory();
        history.setTodo(todo);
        history.setOldState(todo.getStatus().toString());
        history.setNewState("DELETED");
        history.setChangeDate(LocalDateTime.now());
        history.setChangedBy("User");
        taskHistoryRepository.save(history);
    }

    @Override
    public List<TaskHistoryResponseDto> findTaskHistory(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo with id " + id + " not found."));

        checkSoftDelete(todo, "Cannot view history of a deleted Todo.");

        // Fetch task history for the given todo id
        List<TaskHistory> historyList = taskHistoryRepository.findByTodoId(id);

        return historyList.stream()
                .map(taskHistoryMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    private void checkSoftDelete(Todo todo, String errorMessage) {
        if (todo.isDeleted()) {
            throw new IllegalStateException(errorMessage);
        }
    }
}
