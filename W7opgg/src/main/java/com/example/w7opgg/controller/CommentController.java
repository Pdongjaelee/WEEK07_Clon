package com.example.w7opgg.controller;

import com.example.w7opgg.dto.CommentRequestDto;
import com.example.w7opgg.dto.CommonResponseDto;
import com.example.w7opgg.entity.Comment;
import com.example.w7opgg.entity.Member;
import com.example.w7opgg.exception.RequestException;
import com.example.w7opgg.repository.MemberRepository;
import com.example.w7opgg.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.w7opgg.exception.ExceptionType.NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;

    private final MemberRepository memberRepository;

    //댓글 작성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponseDto<?> createComment(@Valid @RequestBody CommentRequestDto commentRequestDto,
                                           @PathVariable int postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        return new CommonResponseDto<>(true, 200, commentService.createComment(commentRequestDto, member, postId));
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public CommonResponseDto<?> updateComment(@Valid @RequestBody CommentRequestDto commentRequestDto,
                                              @PathVariable int postId, @PathVariable int commentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        return new CommonResponseDto<>(true, 200, commentService.updateComment(commentRequestDto, postId, commentId, member));
    }


    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public CommonResponseDto<?> deleteComment(@PathVariable int postId, @PathVariable int commentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        return new CommonResponseDto<>(true, 200, commentService.deleteComment(postId, commentId, member));
    }
}
