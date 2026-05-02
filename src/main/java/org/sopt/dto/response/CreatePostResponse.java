package org.sopt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

// 게시글 작성 응답
@Schema(description = "게시글 작성 응답 DTO")
public record CreatePostResponse(
        Long id
) {
}