package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostService {
    private final PostRepository postRepository = new PostRepository();

    // CREATE
    public CreatePostResponse createPost(CreatePostRequest request) {
        if (request.title == null || request.title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }
        if (request.content == null || request.content.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }

        Long id = postRepository.generateId();
        String createdAt = LocalDateTime.now().toString();

        Post post = new Post(id, request.title, request.content, request.author, createdAt);
        postRepository.save(post);

        return new CreatePostResponse(id, "✅ 게시글 등록 완료!");
    }

    // READ - 전체
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponse> responses = new ArrayList<>();

        for (Post post : posts) {
            responses.add(new PostResponse(
                    post.getId(),
                    post.getTitle(),
                    post.getContent()
            ));
        }

        return responses;
    }

    // READ - 단건
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id);

        if (post == null) {
            throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
        }

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent()
        );
    }

    // UPDATE
    public void updatePost(Long id, String newTitle, String newContent) {
        Post post = postRepository.findById(id);

        if (post == null) {
            throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
        }
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }
        if (newContent == null || newContent.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }

        post.update(newTitle, newContent);
    }

    // DELETE
    public void deletePost(Long id) {
        Post post = postRepository.findById(id);

        if (post == null) {
            throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
        }

        postRepository.delete(post);
    }
}