package com.example.w7opgg.dto.post;

import com.example.w7opgg.dto.CommentSimpleResponseDto;
import com.example.w7opgg.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailSearchDto {
    private int postId;
    private String title;
    private String content;
    private String img;
    private String name;
    private String createTime;
    private int likeNum;
    private int commentNum;
    private List<CommentSimpleResponseDto> comments;

    public static PostDetailSearchDto toDto(Post post, List<CommentSimpleResponseDto> commentSimpleResponseDtos) {
        return new PostDetailSearchDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImgUrl(),
                post.getMember().getName(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(post.getWriteTime()),
                post.getLikes(),
                post.getCommentNum(),
                commentSimpleResponseDtos
        );
    }
}
