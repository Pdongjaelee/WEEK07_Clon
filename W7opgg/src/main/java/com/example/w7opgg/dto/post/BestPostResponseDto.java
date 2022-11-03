package com.example.w7opgg.dto.post;

import com.example.w7opgg.entity.Post;
import com.example.w7opgg.service.CustomTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@AllArgsConstructor
public class BestPostResponseDto {
    private Integer postId;
    private String title;
    private String img;
    private String name;
    private Integer commentsNum;
    private String time;

    public BestPostResponseDto(Post post, int commentsNum) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.img = post.getImgUrl();
        this.name = post.getName();
        this.commentsNum = commentsNum;
        this.time = CustomTime.displayTime(post.getWriteTime());
    }
}
