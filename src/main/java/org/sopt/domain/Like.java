package org.sopt.domain;

import jakarta.persistence.*;

@Entity
@Table(
        name = "likes", //like는 sql 검색 키워드라 테이블명을 like로 하면 위험하다고 함
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_like_user_post",
                    columnNames = {"user_id", "post_id"} //한 유저가 같은 게시물에 좋아요 여러번 누르기 불가능하도록 db에서도 막아놓기
            )
        }
)

public class Like extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    protected Like() {}

    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

}
