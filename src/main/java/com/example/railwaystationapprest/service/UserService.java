package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.*;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new ObjectAlreadyExistsException("user");
        }
        return userRepository.save(user);
    }

    public User findById(long id) {
        User existingUser = userRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("user"));
        return existingUser;
    }

    @Transactional
    public User updateUser(User user, Credentials credentials) {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new ObjectNotFoundException("User"));
        if(!(existingUser.getEmail().equals(credentials.getEmail()) && existingUser.getPassword().equals(credentials.getPassword()))){
            throw new BadCredentialsException();
        }

        existingUser.setPassword(user.getPassword());
        existingUser.setLastName(user.getLastName());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setUserType(user.getUserType());
        return userRepository.save(existingUser);
    }

    public void deleteUser(long id, Credentials credentials) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User"));
        if(!(existingUser.getEmail().equals(credentials.getEmail()) && existingUser.getPassword().equals(credentials.getPassword()))){
            throw new BadCredentialsException();
        }
        if(existingUser.getTickets().isEmpty()){
            userRepository.deleteById(id);
        }else {
            throw new ObjectCannotBeDeletedException("User");
        }

    }

    @Transactional
    public void patchBalance(Credentials credentials, long id, Double balance) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User"));
        if(!(existingUser.getEmail().equals(credentials.getEmail()) && existingUser.getPassword().equals(credentials.getPassword()))){
            throw new BadCredentialsException();
        }
        if(existingUser.getBalance()+balance>=0){
            userRepository.updateBalance(id,balance);
        }else {
            throw new BalanceUpdateException();
        }
    }

    public User findByEmail(String email) {
        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> new ObjectNotFoundException("User"));
        return existingUser;
    }
}
