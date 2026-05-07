package ch.mahmud.bawan.job_marketplace.controllers;

import ch.mahmud.bawan.job_marketplace.dtos.UserResponseDto;
import ch.mahmud.bawan.job_marketplace.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @Tag(name = "User", description = "Get users")
    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> all() {
        List<UserResponseDto> result = userService.getAllUsers();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //ToDo: Get     /users/{userID}
    //      Delete  /users/{userID}
    //      PUT     /users/{userId}
}
