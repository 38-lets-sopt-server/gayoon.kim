package org.sopt.dto.response;

import org.sopt.domain.Member;

public record MemberResponse(
        Long id,
        String nickname,
        String email
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getNickname(),
                member.getEmail()
        );
    }
}