package com.example.w7opgg.repository;


import com.example.w7opgg.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Member findByEmailAndPassword(String email, String password);
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByName(String name);
}
