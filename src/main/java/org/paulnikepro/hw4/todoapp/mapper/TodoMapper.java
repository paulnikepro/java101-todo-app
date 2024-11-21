package org.paulnikepro.hw4.todoapp.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.paulnikepro.hw4.todoapp.dto.TodoCreateDto;
import org.paulnikepro.hw4.todoapp.dto.TodoResponseDto;
import org.paulnikepro.hw4.todoapp.dto.TodoUpdateDto;
import org.paulnikepro.hw4.todoapp.model.Todo;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    Todo toEntity(TodoCreateDto todoCreateDto);

    Todo toEntity(TodoUpdateDto todoUpdateDto);

    TodoResponseDto toResponseDto(Todo todo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TodoUpdateDto dto, @MappingTarget Todo entity);
}
