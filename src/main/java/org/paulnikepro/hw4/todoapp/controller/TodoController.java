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

    @PutMapping("/{id}")
    public TodoResponseDto updateTodo(@PathVariable Long id, @Valid @RequestBody TodoUpdateDto todoUpdateDto) {
        return todoService.update(id, todoUpdateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/history")
    public List<TaskHistoryResponseDto> getTaskHistory(@PathVariable Long id) {
        return todoService.findTaskHistory(id);
    }
}
