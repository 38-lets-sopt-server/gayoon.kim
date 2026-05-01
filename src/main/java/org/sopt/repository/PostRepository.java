package org.sopt.repository;

import org.sopt.domain.Post;

import java.util.List;

public interface PostRepository {

    Post save(Post post);

    Long generateId();

    List<Post> findAll();

    Post findById(Long id);

    boolean deleteById(Long id);
}