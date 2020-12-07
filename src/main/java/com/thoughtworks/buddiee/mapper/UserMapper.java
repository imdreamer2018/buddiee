package com.thoughtworks.buddiee.mapper;

import com.thoughtworks.buddiee.dto.UserDTO;
import com.thoughtworks.buddiee.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserDTO userToUserDto(User user);
    User userDtoToUser(UserDTO userDTO);
}
