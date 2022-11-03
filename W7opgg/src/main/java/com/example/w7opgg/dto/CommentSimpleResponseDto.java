package com.example.w7opgg.dto;

//import crud.team.entity.comment.Comment;
import com.example.w7opgg.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentSimpleResponseDto {
    private String content;
    private String writer;

    public static CommentSimpleResponseDto toDto(Comment comment) {
        return new CommentSimpleResponseDto(comment.getContent(), comment.getName());
    }
}