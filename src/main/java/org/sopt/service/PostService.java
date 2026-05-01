package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.ErrorCode;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.PostNotFoundException;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    // CREATE
    public CreatePostResponse createPost(CreatePostRequest request) {
        validateCreateRequest(request);

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        String createdAt = LocalDateTime.now().toString();

        Post post = new Post(
                user,
                request.title(),
                request.content(),
                createdAt
        );

        postRepository.save(post);

        return new CreatePostResponse(post.getId());
    }

    // READ - 전체 + Pagination
    @Transactional(readOnly = true)  // 조회 전용 → 더티 체킹 안 함 → 성능 최적화
    public List<PostResponse> getAllPosts(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("page는 0 이상이어야 합니다.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("size는 1 이상이어야 합니다.");
        }

        return postRepository.findAll()
                .stream()
                .skip((long) page * size)
                .limit(size)
                .map(PostResponse::from)
                .toList();
    }

    // READ - 단건
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        return PostResponse.from(post);
    }

    // UPDATE
    @Transactional  // 변경 → 더티 체킹으로 save() 없이 자동 UPDATE
    public void updatePost(Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        validateUpdateRequest(request);

        post.update(request.title(), request.content());
    }

    // DELETE
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        postRepository.delete(post);
    }

    private void validateCreateRequest(CreatePostRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }

        if (request.content() == null || request.content().isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }

        if (request.userId() == null) {
            throw new IllegalArgumentException("작성자 id는 필수입니다!");
        }
    }

    private void validateUpdateRequest(UpdatePostRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }

        if (request.content() == null || request.content().isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }
    }
}