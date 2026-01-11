package edu.bbte.localproducersmarketplace.controller;

import edu.bbte.localproducersmarketplace.dto.in.LoginDto;
import edu.bbte.localproducersmarketplace.dto.in.RegisterDto;
import edu.bbte.localproducersmarketplace.model.Role;
import edu.bbte.localproducersmarketplace.model.User;
import edu.bbte.localproducersmarketplace.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDto loginDto) {
        String token = authService.login(loginDto.getEmail(), loginDto.getPassword());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDto registerDto) {
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setName(registerDto.getName());
        user.setPassword(registerDto.getPassword());
        try {
            user.setRole(Role.valueOf(registerDto.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role");
        }

        authService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }
}

