package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 작성 요청 DTO")
public record CreatePostRequest(
        String title,
        String content
) {
}