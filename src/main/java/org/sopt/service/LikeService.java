package org.sopt.service;

import org.springframework.transaction.annotation.Transactional;
import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.LikeRequest;
import org.sopt.exception.AlreadyLikedException;
import org.sopt.exception.ErrorCode;
import org.sopt.exception.LikeNotFoundException;
import org.sopt.exception.NotFoundException;
import org.sopt.repository.LikeRepository;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeService(
            LikeRepository likeRepository,
            UserRepository userRepository,
            PostRepository postRepository
    ) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public void addLike(Long postId, LikeRequest request){
        validateLikeRequest(postId, request);

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        boolean alreadyLiked = likeRepository.existsByUserAndPost(user, post);

        if (alreadyLiked) {
            throw new AlreadyLikedException();
        }

        Like like = new Like(user, post);
        likeRepository.save(like);
    }

    @Transactional
    public void cancelLike(Long postId, LikeRequest request){
        validateLikeRequest(postId, request);

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        Like like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(LikeNotFoundException::new);

        likeRepository.delete(like);
    }

    private void validateLikeRequest(Long postId, LikeRequest request) {
        if (postId == null) {
            throw new IllegalArgumentException("게시글 id는 필수입니다.");
        }

        if (request == null || request.userId() == null) {
            throw new IllegalArgumentException("사용자 id는 필수입니다.");
        }
    }

}
