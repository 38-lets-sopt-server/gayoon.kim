package org.sopt.domain;

import jakarta.persistence.*;

@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Column
    private String imageKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected Post() {
    }

    public Post(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public Post(Member member, String title, String content, String imageKey) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.imageKey = imageKey;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageKey() {
        return imageKey;
    }

    public String getAuthor() {
        return member.getNickname();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getInfo() {
        return "[" + id + "] " + title + " - " + getAuthor() + " (" + getCreatedAt() + ")\n" + content;
    }
}