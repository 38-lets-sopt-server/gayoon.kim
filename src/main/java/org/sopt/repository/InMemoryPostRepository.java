package org.sopt.repository;

import org.sopt.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryPostRepository implements PostRepository {

    private final List<Post> postList = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public Post save(Post post) {
        postList.add(post);
        return post;
    }

    @Override
    public Long generateId() {
        return nextId++;
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(postList);
    }

    @Override
    public Post findById(Long id) {
        for (Post post : postList) {
            if (post.getId().equals(id)) {
                return post;
            }
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return postList.removeIf(post -> post.getId().equals(id));
    }
}