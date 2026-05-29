package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.sopt.dto.response.ApiResponse;
import org.sopt.dto.response.MemberResponse;
import org.sopt.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeController {

    private final AuthService authService;

    @Operation(summary = "내 정보 조회 (Access Token 검증)")
    @GetMapping("/api/v1/me")
    public ResponseEntity<ApiResponse<MemberResponse>> me(Authentication authentication) {

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("인증되지 않았습니다.");
        }

        Long memberId = Long.parseLong(authentication.getName());
        MemberResponse member = authService.getMemberById(memberId);

        return ResponseEntity.ok(ApiResponse.success(member));
    }
}