package org.sopt.dto.request;

// 게시글 수정 요청
public record UpdatePostRequest(
        String title,
        String content
) {
}
