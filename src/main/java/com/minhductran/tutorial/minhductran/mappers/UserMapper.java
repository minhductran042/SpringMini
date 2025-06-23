package com.minhductran.tutorial.minhductran.mappers;

import ch.qos.logback.core.model.ComponentModel;
import com.minhductran.tutorial.minhductran.dto.request.UserCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateDTO;
import com.minhductran.tutorial.minhductran.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreationDTO request);

    void updateEntity(@MappingTarget User user, UserUpdateDTO request);
}
