package org.sopt.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.Member;
import org.sopt.dto.request.MemberCreateRequest;
import org.sopt.dto.response.MemberResponse;
import org.sopt.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse join(MemberCreateRequest request) {
        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member member = new Member(
                request.nickname(),
                request.email(),
                request.password()
        );

        Member savedMember = memberRepository.save(member);

        return MemberResponse.from(savedMember);
    }
}