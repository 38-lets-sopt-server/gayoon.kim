package org.sopt.dto.request;

public record MemberCreateRequest(
        String nickname,
        String email,
        String password
) {
}
