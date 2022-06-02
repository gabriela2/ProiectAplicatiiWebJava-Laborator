package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.*;
import com.example.railwaystationapprest.model.Ticket;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;

    @Test
    void findByEmailNotFound() {
        //arrange
        User user = new User();
        user.setEmail("abc");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()-> userService.findByEmail(anyString()));
        //assert
        assertEquals("User does not exist",exception.getMessage());
    }
    @Test
    void findByEmailFound() {
        //arrange
        User user = new User();
        user.setEmail("abc");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        //act
        User result = userService.findByEmail(anyString());
        //assert
        assertEquals(user.getEmail(),result.getEmail());
    }


    @Test
    void findByIdNotFound() {
        //arrange
        User user = new User();
        user.setId(1l);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()-> userService.findById(1));
        //assert
        assertEquals("user does not exist",exception.getMessage());
    }
    @Test
    void findByIdFound() {
        //arrange
        User user = new User();
        user.setId(1l);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        //act
        User result = userService.findById(1l);
        //assert
        assertEquals(user.getId(),result.getId());
    }

    @Test
    void createUserExists() {
        //arrange
        User user = new User();
        user.setEmail("abc");
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        //act
        ObjectAlreadyExistsException exception = assertThrows(ObjectAlreadyExistsException.class,()-> userService.create(user));
        //assert
        assertEquals("user already exists in the database",exception.getMessage());
    }

    @Test
    void createUserNotExists() {
        //arrange
        User user = new User();
        user.setEmail("abc");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        //act
        User result = userService.create(user);
        //assert
        assertEquals(user.getEmail(),result.getEmail());
    }

    @Test
    void patchBalanceUserIdNotFound() {
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("ana");
        user.setPassword("ana");
        when(userRepository.findById(1l)).thenReturn(Optional.empty());
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()-> userService.patchBalance(credentials,1l,19.9));
        assertEquals("User does not exist",exception.getMessage());
    }

    @Test
    void patchBalanceUserIdFoundBadCredential() {
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("test");
        user.setPassword("test");
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()-> userService.patchBalance(credentials,1l,19.9));
        assertEquals("The credentials used are not correct",exception.getMessage());
    }
    @Test
    void patchBalanceUserIdFoundCredentialsOkSumNegative() {
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("ana");
        user.setPassword("ana");
        user.setBalance(0.0);
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        BalanceUpdateException exception = assertThrows(BalanceUpdateException.class,()-> userService.patchBalance(credentials,1l,-19.9));
        assertEquals("The balance of the account cannot be less than 0",exception.getMessage());
    }

    @Test
    void patchBalanceUserIdFoundCredentialsOkSumOk() {
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("ana");
        user.setPassword("ana");
        user.setBalance(0.0);
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        userService.patchBalance(credentials,1l,19.9);
        verify(userRepository,times(1)).updateBalance(1,19.9);
    }

    @Test
    void deleteUserIdNotFound() {
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("ana");
        user.setPassword("ana");
        when(userRepository.findById(1l)).thenReturn(Optional.empty());
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()-> userService.deleteUser(1l,credentials));
        assertEquals("User does not exist",exception.getMessage());
    }

    @Test
    void deleteUserIdFoundBadCredential() {
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("test");
        user.setPassword("test");
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()-> userService.deleteUser(1l,credentials));
        assertEquals("The credentials used are not correct",exception.getMessage());
    }

    @Test
    void deleteUserIdFoundCredentialsOkTicketEmpty() {
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("ana");
        user.setPassword("ana");
        user.setBalance(0.0);
        List<Ticket> tickets = new ArrayList<>();
        user.setTickets(tickets);
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        userService.deleteUser(1l,credentials);
        verify(userRepository,times(1)).deleteById(1l);
    }


    @Test
    void deleteUserIdFoundCredentialsOkTickeNotEmpty() {
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("ana");
        user.setPassword("ana");
        user.setBalance(0.0);
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket();
        tickets.add(ticket);
        user.setTickets(tickets);
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        ObjectCannotBeDeletedException exception = assertThrows(ObjectCannotBeDeletedException.class,()->userService.deleteUser(1l,credentials));
        assertEquals("User cannot be deleted",exception.getMessage());
    }

    @Test
    void updateUserIdNotFound() {
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("ana");
        user.setPassword("ana");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()-> userService.updateUser(user,credentials));
        assertEquals("User does not exist",exception.getMessage());
    }

    @Test
    void updateUserIdFoundBadCredential() {
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("test");
        user.setPassword("test");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()-> userService.updateUser(user,credentials));
        assertEquals("The credentials used are not correct",exception.getMessage());
    }

    @Test
    void updateUserIdFoundEmailNotOk() {
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("ana");
        user.setPassword("ana");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        User result = userService.updateUser(user,credentials);
        assertEquals(user.getEmail(),result.getEmail());
    }
}