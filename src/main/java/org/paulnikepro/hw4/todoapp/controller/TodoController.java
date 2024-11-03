package org.paulnikepro.hw4.todoapp.controller;

import org.paulnikepro.hw4.todoapp.dto.TodoCreateDto;
import org.paulnikepro.hw4.todoapp.dto.TodoResponseDto;
import org.paulnikepro.hw4.todoapp.dto.TodoUpdateDto;
import org.paulnikepro.hw4.todoapp.dto.TaskHistoryResponseDto;
import org.paulnikepro.hw4.todoapp.service.TodoServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoServiceImpl todoService;

    @PostMapping
    public TodoResponseDto save(@Valid @RequestBody TodoCreateDto todoCreateDto) {
        return todoService.save(todoCreateDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable Long id) {
        TodoResponseDto todoResponse = todoService.findById(id); // Assuming this method exists in your service
        if (todoResponse != null) {
            return ResponseEntity.ok(todoResponse); // Return 200 OK with the todo details
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if the todo does not exist
        }
    }

    @PutMapping("/{id}")
    public TodoResponseDto updateTodo(@PathVariable Long id, @Valid @RequestBody TodoUpdateDto todoUpdateDto) {
        return todoService.update(id, todoUpdateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        if (!todoService.existsById(id)) {
            return ResponseEntity.notFound().build(); // Handle not found case
        }
        todoService.deleteById(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
//        todoService.deleteById(id);
//        return ResponseEntity.noContent().build(); // 204 No Content
//    }

    @GetMapping("/{id}/history")
    public List<TaskHistoryResponseDto> getTaskHistory(@PathVariable Long id) {
        return todoService.findTaskHistory(id);
    }
}
