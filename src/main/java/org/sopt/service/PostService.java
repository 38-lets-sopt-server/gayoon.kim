package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.PostNotFoundException;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // CREATE
    public CreatePostResponse createPost(CreatePostRequest request) {
        validateCreateRequest(request);

        Long id = postRepository.generateId();
        String createdAt = LocalDateTime.now().toString();

        Post post = new Post(
                id,
                request.title(),
                request.content(),
                request.author(),
                createdAt
        );

        postRepository.save(post);

        return new CreatePostResponse(id);
    }

    // READ - 전체
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    // READ - 단건
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id);

        if (post == null) {
            throw new PostNotFoundException(id);
        }

        return PostResponse.from(post);
    }

    // UPDATE
    public void updatePost(Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id);

        if (post == null) {
            throw new PostNotFoundException(id);
        }

        validateUpdateRequest(request);

        post.update(request.title(), request.content());
    }

    // DELETE
    public void deletePost(Long id) {
        Post post = postRepository.findById(id);

        if (post == null) {
            throw new PostNotFoundException(id);
        }

        postRepository.deleteById(id);
    }

    private void validateCreateRequest(CreatePostRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }

        if (request.content() == null || request.content().isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }

        if (request.author() == null || request.author().isBlank()) {
            throw new IllegalArgumentException("작성자는 필수입니다!");
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