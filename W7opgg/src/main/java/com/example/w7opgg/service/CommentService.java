package com.example.w7opgg.service;

import com.example.w7opgg.dto.CommentRequestDto;
import com.example.w7opgg.dto.CommentResponseDto;
import com.example.w7opgg.dto.CommonResponseDto;
import com.example.w7opgg.entity.Comment;
import com.example.w7opgg.entity.Member;
import com.example.w7opgg.entity.Post;
import com.example.w7opgg.exception.RequestException;
import com.example.w7opgg.repository.CommentRepository;
import com.example.w7opgg.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.w7opgg.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static com.example.w7opgg.exception.ExceptionType.NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    //댓글 작성
    @Transactional
    public List<CommentResponseDto> createComment(CommentRequestDto commentRequestDto, Member member, int postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        Comment comment1 = commentRepository.save(new Comment(commentRequestDto.getContent(), post, member));


        List<Comment> commentList = commentRepository.findAllByPostId(postId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : commentList){
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            //댓글 작성자의 Name과 로그인한 사람의 Name비교해서 true, false로 반환해주기
                            .correctComment(comment.getMember().getName().equals(member.getName()))
                            .id(comment.getId())
                            .content(comment.getContent())
                            .name(comment.getName())
                            .time(CustomTime.displayTime(comment.getWriteTime()))
                            .build()
            );
        }
        return commentResponseDtoList;
    }

    //댓글 수정
    @Transactional
    public List<CommentResponseDto> updateComment(CommentRequestDto commentRequestDto, int postId, int commentId, Member member){
        //기존 데이터 가져오기
        Comment comment1 = commentRepository.findById(commentId).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        //작성자만 수정할 수 있도록 Member의 email과 comment의 email을 비교
        if(!member.getEmail().equals(comment1.getMember().getEmail())){
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        };

        //기존 데이터를 새로운 데이터로 수정
        comment1.update(commentRequestDto.getContent());

        List<Comment> commentList = commentRepository.findAllByPostId(postId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : commentList){
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .correctComment(comment.getMember().getName().equals(member.getName()))
                            .id(comment.getId())
                            .content(comment.getContent())
                            .name(comment.getName())
                            .time(CustomTime.displayTime(comment.getWriteTime()))
                            .build()
            );
        }
        return commentResponseDtoList;
    }

    //댓글 삭제
    @Transactional
    public CommonResponseDto<?> deleteComment(int postId, int commentId, Member member){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        //작성자만 삭제할 수 있도록 Member의 email과 comment의 email을 비교
        if(!member.getEmail().equals(comment.getMember().getEmail())){
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        };

        commentRepository.deleteById(commentId);

        return  new CommonResponseDto<>(true, 200,"");
    }


}
