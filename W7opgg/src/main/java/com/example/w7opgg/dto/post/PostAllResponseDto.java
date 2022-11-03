package com.example.w7opgg.dto.post;

import com.example.w7opgg.entity.Likes;
import com.example.w7opgg.entity.Member;
import com.example.w7opgg.entity.Post;
import com.example.w7opgg.service.CustomTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostAllResponseDto {

    private boolean correctLike;
    private Integer postId;
    private String title;
    private String content;
    private String image;
    private String name;
    private String time;
    private Integer likeNum;
    private Integer commentsNum;

    public PostAllResponseDto toDto(Post post, int commentNum, Likes like) {
        return new PostAllResponseDto(like!=null, post.getId(), post.getTitle(), post.getContent(),
                post.getImgUrl(), post.getMember().getName(),
                CustomTime.displayTime(post.getWriteTime()),
                post.getLikes(), commentNum);
    }
}