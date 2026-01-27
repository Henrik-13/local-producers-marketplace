package edu.bbte.localproducersmarketplace.controller;

import edu.bbte.localproducersmarketplace.dto.in.LoginDto;
import edu.bbte.localproducersmarketplace.dto.in.RegisterDto;
import edu.bbte.localproducersmarketplace.dto.out.UserOutDto;
import edu.bbte.localproducersmarketplace.mapper.UserMapper;
import edu.bbte.localproducersmarketplace.model.Role;
import edu.bbte.localproducersmarketplace.model.User;
import edu.bbte.localproducersmarketplace.security.CustomUserDetailsService;
import edu.bbte.localproducersmarketplace.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserOutDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userDetailsService.loadUserByEmail(email);
        return ResponseEntity.ok(userMapper.userToUserOutDto(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDto loginDto) {
        String token = authService.login(loginDto.getEmail(), loginDto.getPassword());
        log.info("User logged in: {}", loginDto.getEmail());
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
        log.info("User registered: {}", registerDto.getEmail());
        return ResponseEntity.ok("User registered successfully");
    }
}

