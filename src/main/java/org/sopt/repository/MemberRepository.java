package org.sopt.repository;

import org.sopt.domain.Member;
import org.sopt.domain.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByOauthProviderAndSocialId(OAuthProvider oauthProvider, String socialId);
}