package com.example.demo;

import com.example.demo.UserDto;
import com.example.demo.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}