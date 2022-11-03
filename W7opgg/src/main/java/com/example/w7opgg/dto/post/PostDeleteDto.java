package com.example.w7opgg.dto.post;

import com.example.w7opgg.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDeleteDto {

    private Integer id;
    private String title;
    private String content;
    private String image;
    private String name;
    private String time;
    private Integer likeNum;
    private Integer commentsNum;

    public PostDeleteDto toDto(Post post, int commentNum) {
        return new PostDeleteDto(post.getId(), post.getTitle(), post.getContent(),
                post.getImgUrl(), post.getMember().getName(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(post.getWriteTime()),
                post.getLikes(), commentNum);
    }
}