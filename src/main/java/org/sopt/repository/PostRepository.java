package org.sopt.repository;

import org.sopt.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepository {
    private final List<Post> postList = new ArrayList<>();
    private Long nextId = 1L;

    public Post save(Post post) {
        postList.add(post);
        return post;
    }

    public Long generateId() {
        return nextId++;
    }
    public List<Post> findAll() {return new ArrayList<>(postList);}
    public Post findById(Long id) {
        for (Post post : postList) {
            if (post.getId().equals(id)) {
                return post;
            }
        }
        return null;
    }
    public boolean deleteById(Long id) {
        return postList.removeIf(p -> p.getId().equals(id));
    }
}