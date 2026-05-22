package org.sopt.domain;

import jakarta.persistence.*;

@Entity
@Table(
        name = "likes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_like_member_post",
                        columnNames = {"member_id", "post_id"}
                )
        }
)
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    protected Like() {
    }

    public Like(Member member, Post post) {
        this.member = member;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Post getPost() {
        return post;
    }
}