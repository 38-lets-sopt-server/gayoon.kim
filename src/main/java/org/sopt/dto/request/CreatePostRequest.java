package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

// 게시글 작성 요청 (클라이언트 → 서버)
@Schema(description = "게시글 작성 요청 DTO")
public record CreatePostRequest(
        Long userId,
        String title,
        String content
) {
}
