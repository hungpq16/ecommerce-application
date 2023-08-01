package com.example.demo.service;

import java.util.Optional;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;

public interface CartService {

    Optional<Cart> findById(Long id);

    <S extends Cart> S save(S entity);

    Cart findByUser(User user);

}
