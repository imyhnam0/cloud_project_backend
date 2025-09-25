package com.cloudproject.community_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.cloudproject.community_backend.entity.User;
import com.cloudproject.community_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "회원", description = "회원 관련 API (회원가입/조회)")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // DTO 정의
    public record UserRegisterRequest(
            @Schema(description = "사용자 이메일", example = "test@example.com")
            String email,

            @Schema(description = "비밀번호", example = "password123")
            String password,

            @Schema(description = "닉네임", example = "tester")
            String username
    ) {}

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "이미 가입된 이메일")
    })
    @PostMapping("/register")
    public User register(@RequestBody UserRegisterRequest req) {
        // ✅ 이메일 중복 체크
        if (userRepository.findByEmail(req.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다.");
        }

        User user = new User();
        user.setEmail(req.email());

        // ✅ 비밀번호 암호화 후 저장
        String encodedPw = passwordEncoder.encode(req.password());
        user.setPassword(encodedPw);

        user.setUsername(req.username());
        return userRepository.save(user);
    }

    @Operation(summary = "모든 사용자 조회", description = "등록된 모든 사용자를 조회합니다.")
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "특정 사용자 조회", description = "ID로 특정 사용자를 조회합니다.")
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
