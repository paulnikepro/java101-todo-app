package org.paulnikepro.hw4.todoapp.service;

import org.paulnikepro.hw4.todoapp.dto.TaskHistoryResponseDto;
import org.paulnikepro.hw4.todoapp.dto.TodoCreateDto;
import org.paulnikepro.hw4.todoapp.dto.TodoResponseDto;
import org.paulnikepro.hw4.todoapp.dto.TodoUpdateDto;

import java.util.List;

public interface TodoService {
    TodoResponseDto save(TodoCreateDto todoCreateDto);

    TodoResponseDto update(Long id, TodoUpdateDto todoUpdateDto);

    void delete(Long id);

    List<TaskHistoryResponseDto> findTaskHistory(Long id);
}
