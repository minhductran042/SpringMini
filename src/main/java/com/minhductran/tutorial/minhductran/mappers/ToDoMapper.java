package com.minhductran.tutorial.minhductran.mappers;

import com.minhductran.tutorial.minhductran.dto.request.ToDoCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.ToDoUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.ToDoDetailResponse;
import com.minhductran.tutorial.minhductran.model.ToDo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ToDoMapper {
    ToDo toEntity(ToDoCreationDTO request);
    ToDoDetailResponse toToDoDetailResponse(ToDo todo);
    ToDo updateEntity(@MappingTarget ToDo todo, ToDoUpdateDTO request);
}
