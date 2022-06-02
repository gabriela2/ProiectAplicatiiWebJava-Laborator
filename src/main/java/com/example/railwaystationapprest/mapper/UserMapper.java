package com.example.railwaystationapprest.mapper;

import com.example.railwaystationapprest.dto.CreateUserDto;
import com.example.railwaystationapprest.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User convertUserDtoToUser(CreateUserDto userDto) {
        return new User(userDto.getEmail(),userDto.getPassword(),userDto.getUserType(),userDto.getLastName(),userDto.getFirstName());
    }

    public CreateUserDto convertUserToUserDto(User user){
        return new CreateUserDto(user.getEmail(), user.getPassword(), user.getLastName(), user.getFirstName(), user.getUserType());
    }
}
