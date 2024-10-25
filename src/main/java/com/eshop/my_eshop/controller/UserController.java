package com.eshop.my_eshop.controller;


import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshop.my_eshop.dto.UserDTO;
import com.eshop.my_eshop.exception.AlreadyExistsException;
import com.eshop.my_eshop.exception.ResourceNotFoundException;
import com.eshop.my_eshop.request.CreateUserRequest;
import com.eshop.my_eshop.request.UpdateUserRequest;
import com.eshop.my_eshop.response.ApiResponse;
import com.eshop.my_eshop.service.user.IUserService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/user")
@CrossOrigin("http://localhost:3000")
public class UserController {
    private final IUserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        try {
            UserDTO userDTO =  userService.getUserById(id);
            return ResponseEntity.ok().body(new ApiResponse("User found", userDTO));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("User Not Found", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            UserDTO userDTO = userService.createUser(request);
            return ResponseEntity.ok().body(new ApiResponse("User created", userDTO));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("User Not Created", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest request) {
        try {
            UserDTO userDTO = userService.updateUser(request, userId);
            return ResponseEntity.ok().body(new ApiResponse("User updated", userDTO));       
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("User Not Found", e.getMessage()));
        }
    }
}
