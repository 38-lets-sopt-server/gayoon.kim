package org.sopt.domain;

import jakarta.persistence.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // 게시글 상세 화면 — 특정 게시글 식별용
    private String title;     // 목록, 상세, 글쓰기 화면 — 제목
    private String content;   // 목록(미리보기), 상세(전체) 화면 — 내용
    @ManyToOne(fetch = FetchType.LAZY)  // User : Post = 1 : N
    @JoinColumn(name = "user_id", nullable = false)       // post 테이블에 user_id FK 컬럼이 생겨요
    private User user;

    protected Post() {}  // JPA 기본 생성자

    private String createdAt; // 목록, 상세 화면 — 작성 시각

    public Post(User user, String title, String content, String createdAt) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return user.getNickname();}
    public String getCreatedAt() { return createdAt; }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getInfo() {
        return "[" + id + "] " + title + " - " + getAuthor() + " (" + createdAt + ")\n" + content;
    }
}