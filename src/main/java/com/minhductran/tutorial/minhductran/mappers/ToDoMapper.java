package com.minhductran.tutorial.minhductran.mappers;

import com.minhductran.tutorial.minhductran.dto.request.ToDoCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.ToDoUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.ToDoDetailResponse;
import com.minhductran.tutorial.minhductran.model.ToDo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ToDoMapper {
    @Mapping(target = "status", source = "status")
    ToDo toEntity(ToDoCreationDTO request);

    @Mapping(target = "status", source = "status")
    ToDoDetailResponse toToDoDetailResponse(ToDo todo);

    ToDo updateEntity(@MappingTarget ToDo todo, ToDoUpdateDTO request);
}
