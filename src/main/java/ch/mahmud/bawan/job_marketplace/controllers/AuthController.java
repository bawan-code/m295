package ch.mahmud.bawan.job_marketplace.controllers;

import ch.mahmud.bawan.job_marketplace.dtos.UserRegisterRequestDto;
import ch.mahmud.bawan.job_marketplace.dtos.UserResponseDto;
import ch.mahmud.bawan.job_marketplace.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterRequestDto request) {
        UserResponseDto createdUser = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }
}