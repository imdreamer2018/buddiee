package com.thoughtworks.buddiee.mapper;

import com.thoughtworks.buddiee.dto.UserRequestDTO;
import com.thoughtworks.buddiee.dto.UserResponseDTO;
import com.thoughtworks.buddiee.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserResponseDTO userToUserResponseDto(User user);
    User userRequestDtoToUser(UserRequestDTO userRequestDTO);
}
