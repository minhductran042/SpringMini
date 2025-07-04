package com.minhductran.tutorial.minhductran.mappers;

import ch.qos.logback.core.model.ComponentModel;
import com.minhductran.tutorial.minhductran.dto.request.UserCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.UserDetailRespone;
import com.minhductran.tutorial.minhductran.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreationDTO request);

    @Mapping(target = "logo", source = "logo")
    UserDetailRespone toUserDetailResponse(User user);
    void updateEntity(@MappingTarget User user, UserUpdateDTO request);
}
