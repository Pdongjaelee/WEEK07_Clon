package com.example.w7opgg.dto.member;

import com.example.w7opgg.entity.Authority;
import com.example.w7opgg.entity.Member;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDto {

    @NotBlank(message = "아이디를 입력해주세요")
    @Email(message = "이메일 형식만 가능합니다")
    private String email;


    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .authority(Authority.ROLE_USER)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
