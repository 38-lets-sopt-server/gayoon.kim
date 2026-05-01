package org.sopt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.Post;

import java.time.LocalDateTime;

// 게시글 조회 응답
@Schema(description = "게시글 조회 응답 DTO")
public record PostResponse(
        @Schema(description = "게시글 id", example = "1")
        Long id,

        @Schema(description = "게시글 제목", example = "첫 번째 게시글")
        String title,

        @Schema(description = "게시글 내용", example = "게시글 내용입니다.")
        String content,

        @Schema(description = "작성자 닉네임", example = "가윤")
        String author,

        @Schema(description = "작성 시각", example = "2026-05-01T12:00:00")
        LocalDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt()
        );
    }
}