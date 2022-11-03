package com.example.w7opgg.dto.post;

import com.example.w7opgg.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSimpleDto {
    private int postId;
    private String title;
    private String content;
    private String img;
    private String name;
    private String time;
    private int likeNum;
    private int commentNum;


    public PostSimpleDto toDto(Post post, int commentNum) {
        return new PostSimpleDto(post.getId() ,post.getTitle(), post.getContent(),
                post.getImgUrl(), post.getMember().getName(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(post.getWriteTime()),
                post.getLikes(), commentNum);
    }
}
