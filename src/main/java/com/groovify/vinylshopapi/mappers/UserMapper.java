package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.UserRegisterDTO;
import com.groovify.vinylshopapi.dtos.UserResponseDTO;
import com.groovify.vinylshopapi.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRegisterDTO userRegisterDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "isActive", source = "isActive")
    UserResponseDTO toResponseDTO(User user);

    List<UserResponseDTO> toResponseDTOs(List<User> users);
}
