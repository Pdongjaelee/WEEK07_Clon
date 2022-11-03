package com.example.w7opgg.controller;

import com.example.w7opgg.dto.CommonResponseDto;
import com.example.w7opgg.dto.member.*;
import com.example.w7opgg.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public CommonResponseDto<Object> signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return new CommonResponseDto(true , 200, null);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto<Object> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return new CommonResponseDto(true , 200, authService.login(loginRequestDto,response));
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reissue")
    public CommonResponseDto<TokenResponseDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return new CommonResponseDto(true , 200, authService.reissue(tokenRequestDto));
    }

    // @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/id-duplicate")
    public CommonResponseDto<Object> idDuplicate(@RequestBody CheckIdDto checkIdDto) {
        authService.idDuplicate(checkIdDto.getEmail());
        return new CommonResponseDto(true , 200, null);

    }

    // @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/name-duplicate")
    public CommonResponseDto<Object> nameDuplicate(@RequestBody CheckNameDto checkNameDto) {
        authService.nameDuplicate(checkNameDto.getName());
        return new CommonResponseDto(true , 200, null);

    }
}
