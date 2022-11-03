package com.example.w7opgg.repository;

import com.example.w7opgg.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPostId(int id);
    List<Comment> findByPostId(int id, Pageable pageable);

    Page<Comment> findByMemberId(Pageable pageable, int id);

    List<Comment> findAllByPostId(int id);
}
