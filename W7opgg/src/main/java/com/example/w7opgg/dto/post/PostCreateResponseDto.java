package com.example.w7opgg.dto.post;

import com.example.w7opgg.entity.Post;
import com.example.w7opgg.service.CustomTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NotBlank
@Builder
public class PostCreateResponseDto {
    private boolean correctPost;
    private int postId;
    private String title;
    private String content;
    private String img;
    private String name;
    private String time;
    private int likeNum;
    private boolean correctLike;
    private List comments;

    public static PostCreateResponseDto toDto(Post post, boolean correctPost, List list,Boolean correctLike){

        return PostCreateResponseDto.builder()
                .correctPost(correctPost)
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .img(post.getImgUrl())
                .name(post.getName())
                .time(CustomTime.displayTime(post.getWriteTime()))
                .likeNum(post.getLikes())
                .correctLike(correctLike)
                .comments(list)
                .build();
    }
}
