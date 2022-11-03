package com.example.w7opgg.entity;

import com.example.w7opgg.dto.post.PostRequestDto;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Builder
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(nullable = false)
    private int likes;

    @Column(nullable = false)
    private int commentNum;

    @Column(nullable = false)
    private String imgUrl;

    @DateTimeFormat
    private LocalDateTime writeTime; // 날짜

    // 2022-10-21 14:41:30:15215412
    // 41분 전 작성 - 업데이트
    @PrePersist
    public void createDate() {
        this.writeTime = LocalDateTime.now();
    }

    public Post(String title, String content, Member member, String imgUrl) {
        this.title = title;
        this.content = content;
        this.name = member.getName();
        this.member = member;
        this.imgUrl = imgUrl;
    }

    public void updatePost(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    public void deletePost(PostRequestDto postRequestDto) {
        this.title = title;
        this.content = content;
    }

    public void PlusLike() {
        this.likes += 1;
    }

    public void MinusLike() {
        this.likes -= 1;
    }

    public void PlusComment() {
        this.commentNum += 1;
    }

    public void MinusComment() {
        this.commentNum -= 1;
    }
}