package com.example.demo.service;

import java.util.Optional;

import com.example.demo.model.persistence.User;

public interface UserService {

    User findByUsername(String username);

    Optional<User> findById(Long id);

    User save(User user);

}
