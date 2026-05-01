package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "좋아요 요청 DTO")
public record LikeRequest(
        @Schema(description = "유저 id", example = "1")
        Long userId
        ) {
}
