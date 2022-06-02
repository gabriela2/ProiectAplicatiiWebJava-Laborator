package com.example.railwaystationapprest.controller;

import com.example.railwaystationapprest.dto.CreateUserDto;
import com.example.railwaystationapprest.exception.InvalidUpdateException;
import com.example.railwaystationapprest.mapper.UserMapper;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.service.Credentials;
import com.example.railwaystationapprest.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserDto userDto){
        User savedUser = userService.create(userMapper.convertUserDtoToUser(userDto));
        return ResponseEntity.created(URI.create("/user/"+ savedUser.getId())).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateUserDto> updateUser(@RequestHeader(Credentials.AUTHORIZATION)String header, @Valid @RequestBody CreateUserDto userDto, @PathVariable long id){
        User existingUser = userService.findById(id);
        if(existingUser.getEmail().equals(userDto.getEmail())==false){
            throw new InvalidUpdateException();
        }
        Credentials credentials = new Credentials(header);
        return ResponseEntity.ok(userMapper.convertUserToUserDto(userService.updateUser(userMapper.convertUserDtoToUser(userDto),credentials)));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@RequestHeader(Credentials.AUTHORIZATION)String header,@PathVariable long id){
        Credentials credentials= new Credentials(header);
        userService.deleteUser(id,credentials);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchBalance(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id, @RequestParam(required = true) Double balance){
        Credentials credentials = new Credentials(header);
        userService.patchBalance(credentials,id,balance);
        return ResponseEntity.noContent().build();
    }


}
