package com.example.w7opgg.repository;

import com.example.w7opgg.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllPageBy(Pageable pageable);
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);
    Page<Post> findByMemberId(Pageable pageable, int id);
    Page<Post> findByLikesGreaterThanEqual(Pageable pageable, int i);

    List<Post> findAllPostBy(Pageable pageable);

}

