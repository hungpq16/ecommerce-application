package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    public static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        // In the case userName, password is not type
        if (createUserRequest == null || createUserRequest.getUsername() == null
                || createUserRequest.getPassword() == null || createUserRequest.getConfirmPassword() == null) {
            log.error("[UserController][createUser]: not input full item.");
            return ResponseEntity.badRequest().build();
        }
        
        // In the case password length less than 7
        if (createUserRequest.getPassword().length() < 7) {
            log.error("[UserController][createUser]: password length must be greater than 7.");
            return ResponseEntity.badRequest().build();
        }

        // In the case re-enter the password does not match
        if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            log.error("File [UserController#createUser]: password confirmed does not match password.");
            return ResponseEntity.badRequest().build();
        }
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        userRepository.save(user);
        // Add log4j when register account successfully
        log.info("File [UserController#createUser]: Create username " + createUserRequest.getUsername()
                + " is successfully!");
        return ResponseEntity.ok(user);
    }

}
