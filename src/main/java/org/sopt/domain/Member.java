package org.sopt.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider oauthProvider;

    private String socialId;

    protected Member() {
    }

    public Member(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.oauthProvider = OAuthProvider.LOCAL;
    }

    private Member(String nickname, String email, OAuthProvider oauthProvider, String socialId) {
        this.nickname = nickname;
        this.email = email;
        this.password = null;
        this.oauthProvider = oauthProvider;
        this.socialId = socialId;
    }

    public static Member createOAuthMember(
            String nickname,
            String email,
            OAuthProvider oauthProvider,
            String socialId
    ) {
        return new Member(nickname, email, oauthProvider, socialId);
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}