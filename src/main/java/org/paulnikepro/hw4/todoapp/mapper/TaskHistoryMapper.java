package org.paulnikepro.hw4.todoapp.mapper;

import org.paulnikepro.hw4.todoapp.dto.TaskHistoryResponseDto;
import org.paulnikepro.hw4.todoapp.model.TaskHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskHistoryMapper {

    @Mapping(target = "todoId", source = "todo.id")
    TaskHistoryResponseDto toResponseDto(TaskHistory taskHistory);
}
