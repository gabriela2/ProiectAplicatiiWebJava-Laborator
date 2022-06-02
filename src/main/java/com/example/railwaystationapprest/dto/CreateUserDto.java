package com.example.railwaystationapprest.dto;

import com.example.railwaystationapprest.model.UserType;

import javax.validation.constraints.*;

import static com.example.railwaystationapprest.model.Pattern.PASSWORD_VALIDATION;

public class CreateUserDto {
    @NotBlank(message = "Email cannot be null")
    @Email
    private String email;
    @Pattern(regexp = PASSWORD_VALIDATION, message = "The password must contain at least one digit, at least one lowercase character, at least one uppercase character, at least one special character and a length between 8 and 20 characters")
    @NotBlank()
    private String password;
    @Size(max = 50)
    private String lastName;
    @Size(max = 100)
    private String firstName;
    @NotNull()
    private UserType userType;

    public CreateUserDto() {
    }

    public CreateUserDto(String email, String password, String lastName, String firstName, UserType userType) {
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
