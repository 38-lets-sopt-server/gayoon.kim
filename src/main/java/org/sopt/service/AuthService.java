package org.sopt.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.AccessTokenBlacklist;
import org.sopt.domain.Member;
import org.sopt.domain.RefreshToken;
import org.sopt.dto.response.MemberResponse;
import org.sopt.dto.response.TokenResponse;
import org.sopt.repository.AccessTokenBlacklistRepository;
import org.sopt.repository.MemberRepository;
import org.sopt.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;

    @Value("${security.jwt.refresh-token-expires-in-seconds:1209600}")
    private long refreshTokenExpiresInSeconds;

    public MemberResponse loginWithCredentials(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return MemberResponse.from(member);
    }

    @Transactional
    public TokenResponse login(String email, String password) {
        MemberResponse member = loginWithCredentials(email, password);

        String accessToken = jwtService.generateAccessToken(member.id(), member.email());
        String refreshToken = jwtService.generateRefreshToken(member.id());

        refreshTokenRepository.deleteByMemberId(member.id());
        refreshTokenRepository.save(
                RefreshToken.of(member.id(), refreshToken, refreshTokenExpiresInSeconds)
        );

        return TokenResponse.of(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse reissue(String refreshToken) {
        Long memberId = jwtService.verifyAndGetMemberId(refreshToken);

        RefreshToken savedRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Refresh Token입니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        String newAccessToken = jwtService.generateAccessToken(member.getId(), member.getEmail());
        String newRefreshToken = jwtService.generateRefreshToken(member.getId());

        savedRefreshToken.rotate(newRefreshToken, refreshTokenExpiresInSeconds);

        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

    public MemberResponse getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return MemberResponse.from(member);
    }

    @Transactional
    public void logout(Long memberId, String accessToken) {
        refreshTokenRepository.deleteByMemberId(memberId);

        LocalDateTime expiresAt = jwtService.getExpiresAt(accessToken);

        if (!accessTokenBlacklistRepository.existsByToken(accessToken)) {
            accessTokenBlacklistRepository.save(
                    AccessTokenBlacklist.of(accessToken, expiresAt)
            );
        }
    }
}