package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.dto.request.LikeRequest;
import org.sopt.dto.response.ApiResponse;
import org.sopt.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "좋아요 관련 API")
@RestController
@RequestMapping("/posts")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @Operation(summary = "좋아요 추가", description = "사용자가 특정 게시글에 좋아요를 추가합니다.")
    @PostMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<Void>> addLike(
            @PathVariable Long postId,
            @RequestBody LikeRequest request
    ) {
        likeService.addLike(postId, request);

        return ResponseEntity.ok(
                ApiResponse.success("좋아요 추가 완료!", null)
        );
    }

    @Operation(summary = "좋아요 취소", description = "사용자가 특정 게시글에 누른 좋아요를 취소합니다.")
    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<Void>> cancelLike(
            @PathVariable Long postId,
            @RequestBody LikeRequest request
    ) {
        likeService.cancelLike(postId, request);

        return ResponseEntity.ok(
                ApiResponse.success("좋아요 취소 완료!", null)
        );
    }
}
