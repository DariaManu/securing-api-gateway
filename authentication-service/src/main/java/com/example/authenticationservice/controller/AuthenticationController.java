package com.example.authenticationservice.controller;

import com.example.authenticationservice.model.UserEntity;
import com.example.authenticationservice.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserEntity userEntity) {
        return ResponseEntity.ok(authenticationService.registerUser(userEntity));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String userName, @RequestParam String password) {
        return ResponseEntity.ok(authenticationService.login(userName, password));
    }
}
