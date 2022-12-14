package com.example.w7opgg.service;


import com.example.w7opgg.dto.TokenDto;
import com.example.w7opgg.dto.member.LoginRequestDto;
import com.example.w7opgg.dto.member.SignUpRequestDto;
import com.example.w7opgg.dto.member.TokenRequestDto;
import com.example.w7opgg.dto.member.TokenResponseDto;
import com.example.w7opgg.entity.Authority;
import com.example.w7opgg.entity.Member;
import com.example.w7opgg.entity.RefreshToken;
import com.example.w7opgg.exception.ExceptionType;
import com.example.w7opgg.exception.RequestException;
import com.example.w7opgg.repository.MemberRepository;
import com.example.w7opgg.repository.RefreshTokenRepository;
import com.example.w7opgg.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static com.example.w7opgg.exception.ExceptionType.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void signup(SignUpRequestDto signUpRequestDto) {
        validateSignUpInfo(signUpRequestDto);
        validateNameInfo(signUpRequestDto);

        Member user = new Member();
        user.setEmail(signUpRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        user.setName(signUpRequestDto.getName());
        user.setAuthority(Authority.ROLE_USER);
        userRepository.save(user);
    }


    @Transactional
    public TokenResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Member user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new RequestException(LOGIN_FAIL_EXCEPTION));

        validatePassword(loginRequestDto, user);

        // 1. Login ID/PW ??? ???????????? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

        // 2. ????????? ?????? (????????? ???????????? ??????) ??? ??????????????? ??????
        //    authenticate ???????????? ????????? ??? ??? CustomUserDetailsService ?????? ???????????? loadUserByUsername ???????????? ?????????
        log.info("before={}",authenticationManagerBuilder);
        log.info("after={}",authenticationManagerBuilder.getObject());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. ?????? ????????? ???????????? JWT ?????? ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken ??????
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        response.addHeader("accessToken", tokenDto.getAccessToken());
        response.addHeader("refreshToken", tokenDto.getRefreshToken());

        // 5. ?????? ??????
        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken(),user.getEmail(),user.getName());
    }


    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token ??????
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token ??? ???????????? ????????????.");
        }

        // 2. Access Token ?????? Member ID ????????????
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. ??????????????? Member ID ??? ???????????? Refresh Token ??? ?????????
        RefreshToken refreshToken = refreshTokenRepository.findByK(authentication.getName())
                .orElseThrow(() -> new RuntimeException("???????????? ??? ??????????????????."));

        // 4. Refresh Token ??????????????? ??????
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("????????? ?????? ????????? ???????????? ????????????.");
        }

        // 5. ????????? ?????? ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. ????????? ?????? ????????????
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // ?????? ??????
        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

    }


    private void validateSignUpInfo(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByEmail(signUpRequestDto.getEmail()))
            throw new RequestException(ALREADY_EXISTS_EXCEPTION);
    }

    private void validateNameInfo(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByName(signUpRequestDto.getEmail()))
            throw new RequestException(ALREADY_EXISTS_EXCEPTION);
    }

    private void validatePassword(LoginRequestDto loginRequestDto, Member user) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new RequestException(LOGIN_FAIL_EXCEPTION);
        }
    }

    public void idDuplicate(String email) {
        if(userRepository.existsByEmail(email))
            throw new RequestException(ALREADY_EXISTS_EXCEPTION);
    }

    public void nameDuplicate(String name) {
        if(userRepository.existsByName(name))
            throw new RequestException(ALREADY_EXISTS_EXCEPTION);
    }
}
