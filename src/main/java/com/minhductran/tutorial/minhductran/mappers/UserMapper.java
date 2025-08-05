package com.minhductran.tutorial.minhductran.mappers;

import com.minhductran.tutorial.minhductran.dto.request.User.UserCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.User.UserUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.User.UserDetailRespone;
import com.minhductran.tutorial.minhductran.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "status", source = "status")
    @Mapping(target = "email", source = "email")
    User toEntity(UserCreationDTO request);

    @Mapping(target = "logo", source = "logo")
    @Mapping(target = "status", source = "status")
    UserDetailRespone toUserDetailResponse(User user);
    void updateEntity(@MappingTarget User user, UserUpdateDTO request);
}
