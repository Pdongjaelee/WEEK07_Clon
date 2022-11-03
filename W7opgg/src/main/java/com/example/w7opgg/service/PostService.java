package com.example.w7opgg.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;

import com.example.w7opgg.dto.*;
//import com.example.w7opgg.dto.member.post.*;
import com.example.w7opgg.dto.post.*;
import com.example.w7opgg.entity.*;
import com.example.w7opgg.exception.ExceptionType;
import com.example.w7opgg.exception.RequestException;
import com.example.w7opgg.repository.CommentRepository;
import com.example.w7opgg.repository.LikesRepository;
import com.example.w7opgg.entity.Post;
import com.example.w7opgg.entity.Member;
import com.example.w7opgg.repository.PostRepository;
import com.example.w7opgg.s3.CommonUtils;
import com.example.w7opgg.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.w7opgg.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static com.example.w7opgg.exception.ExceptionType.NOT_FOUND_EXCEPTION;


@RequiredArgsConstructor
@Service
public class PostService<member> {

    private final PostRepository postRepository;
    private final AmazonS3Client amazonS3Client;
    private final TokenProvider tokenProvider;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;

    //사용자 유효성 검사
    public Member validateMember(HttpServletRequest request){
        if(!tokenProvider.validateToken(request.getHeader("Refresh-Token"))){
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
    //게시판이 있는지 없는지
    public Post isPresentPost(Integer id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }
        //url받아오기
    public String getImageUrlByPost(Post post) {

        return post.getImgUrl();
    }

    // 게시글 작성
    @Transactional
    public PostCreateResponseDto create(PostCreateRequestDto postCreateRequestDto, Member member, MultipartFile multipartFile) throws IOException {

        String imgurl = "";
        String bucket = "woods0611";


        if (!multipartFile.isEmpty()) {
            String fileName = CommonUtils.buildFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());

            byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, byteArrayIs, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            imgurl = amazonS3Client.getUrl(bucket, fileName).toString();
        }

        Post post = postRepository.save(new Post(postCreateRequestDto.getTitle(), postCreateRequestDto.getContent(), member, imgurl)); // + imgUrl
        return PostCreateResponseDto.toDto(post, member.getName().equals(post.getName()), new ArrayList(), false);
    }

    //게시글 전체 조회
    @Transactional(readOnly = true)
    public List<PostAllResponseDto> AllShow(Pageable pageable, Member member) {
        List<Post> postList = postRepository.findAllPostBy(pageable);
        List<PostAllResponseDto> postAllList = new ArrayList<>();

        postList.stream().forEach(i -> postAllList.add(new PostAllResponseDto().toDto(i,commentRepository.findByPostId(i.getId()).size(), likesRepository.findByPostAndMember(i,member))));
        return postAllList;
    }
    // 인기 게시글 조회
//    @Transactional(readOnly = true)
//    public List<PostSimpleDto> PopularPost(Pageable pageable) {
//        Page<Post> posts = postRepository.findByLikesGreaterThanEqual(pageable, 5);
//        List<PostSimpleDto> postSimpleDtos = new ArrayList<>();
//        posts.stream().forEach(i -> postSimpleDtos.add(new PostSimpleDto().toDto(i, commentRepository.findByPostId(i.getId()).size())));
//        return postSimpleDtos;
//    }

    // 인기 게시글 조회
    @Transactional(readOnly = true)
    public List<BestPostResponseDto> PopularPost(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "likes"));
        Page<Post> all = postRepository.findAll(pageRequest);
        List<BestPostResponseDto> postSimpleDtos = new ArrayList<>();
        all.stream().forEach(
                i -> postSimpleDtos.add(
                        new BestPostResponseDto(i,commentRepository.findByPostId(i.getId()).size())));
        return postSimpleDtos;
    }
    //게시글 상세 조회
    @Transactional(readOnly = true)
    public PostCreateResponseDto DetailShow(int id, Member member) {
//        List<Comment> comment = commentRepository.findByPostId(id);
//        List<CommentSimpleResponseDto> commentSimpleResponseDtos = new ArrayList<>();
//        comment.forEach(i -> commentSimpleResponseDtos.add(CommentSimpleResponseDto.toDto(i)));
        Post post = postRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        List<Comment> byPostId = commentRepository.findByPostId(post.getId());
        List<CommentListDto> commentList = new ArrayList<>();

        for (Comment comment : byPostId) {
            commentList.add(new CommentListDto(comment, comment.getName().equals(member.getName())));
        }

        return PostCreateResponseDto.toDto(post, post.getName().equals(member.getName()),commentList,null != likesRepository.findByPostAndMember(post,member));
//        return PostDetailSearchDto.toDto(postRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION)), commentSimpleResponseDtos);
    }

    //게시글 수정
    @Transactional
    public PostCreateResponseDto UpdatePost (Integer id, PostRequestDto postRequestDto, Member member){
        Post post = postRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        if (!member.equals(post.getMember())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }
        post.updatePost(postRequestDto);
        List<Comment> byPostId = commentRepository.findByPostId(post.getId());
        List<CommentListDto> commentList = new ArrayList<>();


        for (Comment comment : byPostId) {
            commentList.add(new CommentListDto(comment, comment.getName().equals(member.getName())));
        }

        return PostCreateResponseDto.toDto(post, post.getName().equals(member.getName()),commentList,null != likesRepository.findByPostAndMember(post,member));
    }

    //게시글 삭제
    @Transactional
    public void DeletePost(Integer id, Member member) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        if (!member.equals(post.getMember())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }
        likesRepository.deleteAllByPost(post);
        postRepository.delete(post);
    }
    // 게시글 좋아요
    @Transactional
    public LikeResponseDto likes(int id, Member member) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        boolean correct;
        if (likesRepository.findByPostAndMember(post, member) == null) {
            post.PlusLike();
            Likes likes = new Likes(post, member);
            likesRepository.save(likes);
            correct = true;
        } else {
            Likes likes = likesRepository.findByPostAndMember(post, member);
            post.MinusLike();
            likesRepository.delete(likes);
            correct = false;
        }
        return LikeResponseDto.toDto(post.getLikes(), correct);
    }
}
