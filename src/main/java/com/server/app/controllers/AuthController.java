package com.server.app.controllers;

import com.server.app.config.JsonWebToken;
import com.server.app.dto.auth.AuthResponseDto;
import com.server.app.dto.auth.LoginDto;
import com.server.app.dto.auth.SignupDto;
import com.server.app.dto.auth.UpdatePasswordDto;
import com.server.app.dto.auth.UpdateProfileDto;
import com.server.app.entities.User;
import com.server.app.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JsonWebToken jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto dto) {
        User user = userService.login(dto.getUsername(), dto.getPassword());
        String token = jwtUtil.createToken(user);
        return ResponseEntity.ok(new AuthResponseDto(token, user));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@Valid @RequestBody SignupDto dto) {
        User user = userService.signup(dto);
        String token = jwtUtil.createToken(user);
        return ResponseEntity.ok(new AuthResponseDto(token, user));
    }

    @GetMapping("/profile")
    public ResponseEntity<User> profile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/profile")
    public ResponseEntity<AuthResponseDto> updateProfile(@Valid @RequestBody UpdateProfileDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User updated = userService.updateAuthProfile(user.getId(), dto);
        String newToken = jwtUtil.createToken(updated);
        return ResponseEntity.ok(new AuthResponseDto(newToken, updated));
    }

    @PutMapping("/update/password")
    public ResponseEntity<User> updatePassword(@Valid @RequestBody UpdatePasswordDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User updated = userService.updatePassword(user.getId(), dto);
        return ResponseEntity.ok(updated);
    }
}
