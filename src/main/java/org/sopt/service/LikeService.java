package org.sopt.service;

import org.sopt.domain.Like;
import org.sopt.domain.Member;
import org.sopt.domain.Post;
import org.sopt.exception.AlreadyLikedException;
import org.sopt.exception.ErrorCode;
import org.sopt.exception.LikeNotFoundException;
import org.sopt.exception.NotFoundException;
import org.sopt.repository.LikeRepository;
import org.sopt.repository.MemberRepository;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public LikeService(
            LikeRepository likeRepository,
            MemberRepository memberRepository,
            PostRepository postRepository
    ) {
        this.likeRepository = likeRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public void addLike(Long memberId, Long postId) {
        validateLikeRequest(memberId, postId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        boolean alreadyLiked = likeRepository.existsByMemberAndPost(member, post);

        if (alreadyLiked) {
            throw new AlreadyLikedException();
        }

        Like like = new Like(member, post);
        likeRepository.save(like);
    }

    @Transactional
    public void cancelLike(Long memberId, Long postId) {
        validateLikeRequest(memberId, postId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        Like like = likeRepository.findByMemberAndPost(member, post)
                .orElseThrow(LikeNotFoundException::new);

        likeRepository.delete(like);
    }

    private void validateLikeRequest(Long memberId, Long postId) {
        if (memberId == null) {
            throw new IllegalArgumentException("회원 id는 필수입니다.");
        }

        if (postId == null) {
            throw new IllegalArgumentException("게시글 id는 필수입니다.");
        }
    }
}