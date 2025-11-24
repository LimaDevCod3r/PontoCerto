package github.com.LimaDevCod3r.controllers;

import github.com.LimaDevCod3r.dtos.user.UserCreateDTO;
import github.com.LimaDevCod3r.dtos.user.UserResponseDto;
import github.com.LimaDevCod3r.mappers.UserMapper;
import github.com.LimaDevCod3r.models.User;
import github.com.LimaDevCod3r.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDTO request) {
        User user = userMapper.toEntity(request);
        userService.createUser(user);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }
}
