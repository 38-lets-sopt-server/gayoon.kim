package org.sopt.service;

import org.sopt.domain.Member;
import org.sopt.domain.Post;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.ErrorCode;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.PostNotFoundException;
import org.sopt.repository.MemberRepository;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostService(
            PostRepository postRepository,
            MemberRepository memberRepository
    ) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public CreatePostResponse createPost(Long memberId, CreatePostRequest request) {
        validateCreateRequest(request);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Post post = new Post(
                member,
                request.title(),
                request.content()
        );

        postRepository.save(post);

        return new CreatePostResponse(post.getId());
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        return PostResponse.from(post);
    }

    @Transactional
    public void updatePost(Long memberId, Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        validateUpdateRequest(request);
        validatePostOwner(post, memberId);

        post.update(request.title(), request.content());
    }

    @Transactional
    public void deletePost(Long memberId, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        validatePostOwner(post, memberId);

        postRepository.delete(post);
    }

    private void validateCreateRequest(CreatePostRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }

        if (request.content() == null || request.content().isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
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

    private void validatePostOwner(Post post, Long memberId) {
        if (!post.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("게시글 작성자만 수정/삭제할 수 있습니다.");
        }
    }
}