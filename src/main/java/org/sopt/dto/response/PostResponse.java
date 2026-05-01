package org.sopt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.Post;

// 게시글 조회 응답
@Schema(description = "게시글 조회 응답 DTO")
public record PostResponse(
        Long id,
        String title,
        String content,
        String author,
        String createdAt
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