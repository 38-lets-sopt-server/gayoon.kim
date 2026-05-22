package org.sopt.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.dto.request.MemberCreateRequest;
import org.sopt.dto.response.ApiResponse;
import org.sopt.dto.response.MemberResponse;
import org.sopt.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> join(
            @RequestBody MemberCreateRequest request
    ) {
        MemberResponse response = memberService.join(request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
