package com.example.w7opgg.controller;

import com.amazonaws.Response;
import com.example.w7opgg.dto.CommonResponseDto;
import com.example.w7opgg.dto.post.*;
import com.example.w7opgg.entity.Member;
import com.example.w7opgg.exception.RequestException;
import com.example.w7opgg.repository.MemberRepository;
import com.example.w7opgg.service.PostService;
//import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.example.w7opgg.exception.ExceptionType.*;
//import static jdk.internal.org.jline.reader.impl.LineReaderImpl.CompletionType.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final MemberRepository userRepository;

    // 게시물 작성
    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
//    public Response create(@Valid @RequestBody PostCreateRequestDto postCreateRequestDto) {
    public CommonResponseDto create(@RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                    @Valid @RequestPart PostCreateRequestDto postCreateRequestDto) throws IOException {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return new CommonResponseDto<PostCreateResponseDto>(true, 200, postService.create(postCreateRequestDto, member, multipartFile));
    }

    // 게시글 전체 조회
    @GetMapping("/post")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto AllShow(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = userRepository.findByEmail(authentication.getName()).orElse(new Member(-1,"","","",null,null));


        return new CommonResponseDto(true, 200, postService.AllShow(pageable, member));


    }
    //게시글 상세 조회
    @GetMapping("/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto DetailShow(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = userRepository.findByEmail(authentication.getName()).orElse(new Member(-1,"","","",null,null));
        return new CommonResponseDto<PostCreateResponseDto>(true, 200, postService.DetailShow(id, member));
    }
        //게시글 수정
    @PutMapping("/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto UpdatePost(@PathVariable Integer id,@RequestBody PostRequestDto postRequestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));
        return new CommonResponseDto(true, 200, postService.UpdatePost(id, postRequestDto,member));
    }

    //게시글 삭제
    @DeleteMapping("/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto DeletePost(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));
        postService.DeletePost(id, member);
        return new CommonResponseDto(true, 200, "");
    }


    // 게시글 좋아요
    @PostMapping("/post/{id}/like")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto likes(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return new CommonResponseDto(true, 200, postService.likes(id, member) );
        }

    // 인기 게시글 조회
    @GetMapping("/best")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto PopularPost(@PageableDefault(size = 5, sort = "likes", direction = Sort.Direction.DESC) Pageable pageable) {
        return new CommonResponseDto(true, 200, postService.PopularPost(pageable));
    }
}



