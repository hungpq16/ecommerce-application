package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestConstant;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserSuccess() {
        when(encoder.encode(TestConstant.PASSWORD)).thenReturn("thisIsHashed");
        final ResponseEntity<User> response = userController.createUser(this.getCreateUserRequest());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(TestConstant.USERNAME, user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void createUserFail() {
        // In the case password length less than 7
        CreateUserRequest createUserRequest1 = this.getCreateUserRequest();
        createUserRequest1.setPassword("12345");
        createUserRequest1.setConfirmPassword("12345");
        final ResponseEntity<User> resPassword = userController.createUser(createUserRequest1);
        assertNotNull(resPassword);
        assertEquals(400, resPassword.getStatusCodeValue());

        // In the case re-enter the password does not match
        CreateUserRequest createUserRequest2 = this.getCreateUserRequest();
        createUserRequest2.setConfirmPassword("confirmPassword");
        final ResponseEntity<User> resPasswordNotMatch = userController.createUser(createUserRequest2);
        assertNotNull(resPasswordNotMatch);
        assertEquals(400, resPasswordNotMatch.getStatusCodeValue());
    }

    @Test
    public void findUserSuccess() {
        User userTest = new User(1L, TestConstant.USERNAME, TestConstant.PASSWORD);

        // find by id
        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest));
        final ResponseEntity<User> resFindById = userController.findById(userTest.getId());
        User user = resFindById.getBody();
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals(TestConstant.USERNAME, user.getUsername());

        // find by name
        when(userRepository.findByUsername(TestConstant.USERNAME)).thenReturn(userTest);
        final ResponseEntity<User> resFindByName = userController.findByUserName(TestConstant.USERNAME);
        user = resFindByName.getBody();
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals(TestConstant.USERNAME, user.getUsername());
    }

    @Test
    public void findUserFail() {
        // find user by userId
        final ResponseEntity<User> responseByID = userController.findById(5L);
        assertNull(responseByID.getBody());

        // find user by userName
        final ResponseEntity<User> responseByName = userController.findByUserName("test5");
        assertNull(responseByName.getBody());
    }

    private CreateUserRequest getCreateUserRequest() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(TestConstant.USERNAME);
        r.setPassword(TestConstant.PASSWORD);
        r.setConfirmPassword(TestConstant.PASSWORD);
        return r;
    }

}
