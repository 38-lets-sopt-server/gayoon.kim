package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

// 게시글 수정 요청
@Schema(description = "게시글 수정 요청 DTO")
public record UpdatePostRequest(
        String title,
        String content
) {
}
