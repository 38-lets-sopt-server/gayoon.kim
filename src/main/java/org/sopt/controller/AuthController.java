package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.sopt.dto.response.ApiResponse;
import org.sopt.dto.response.TokenResponse;
import org.sopt.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.sopt.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인 (Access Token + Refresh Token 발급)")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        TokenResponse tokens = authService.login(email, password);

        return ResponseEntity.ok(ApiResponse.success(tokens));
    }
}