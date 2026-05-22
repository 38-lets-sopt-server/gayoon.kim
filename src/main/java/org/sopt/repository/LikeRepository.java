package org.sopt.repository;

import org.sopt.domain.Like;
import org.sopt.domain.Member;
import org.sopt.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByMemberAndPost(Member member, Post post);

    Optional<Like> findByMemberAndPost(Member member, Post post);
}