package goorm.member_management.member.controller;

import java.time.Duration;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import goorm.member_management.member.dto.MemberInfo;
import goorm.member_management.member.dto.request.MemberSignInRequest;
import goorm.member_management.member.dto.request.MemberSignUpRequest;
import goorm.member_management.member.dto.response.MemberSignInResponse;
import goorm.member_management.member.service.AuthService;
import goorm.member_management.security.JwtProvider;
import goorm.member_management.security.dto.RefreshTokenInfo;
import goorm.member_management.security.repository.TokenRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody MemberSignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<MemberSignInResponse> signIn(@Valid @RequestBody MemberSignInRequest request) {
        final MemberInfo memberInfo = authService.signIn(request.email(), request.password());
        final String accessToken = jwtProvider.createAccessToken(memberInfo.email(), memberInfo.role());
        final RefreshTokenInfo refreshTokenInfo = jwtProvider.createRefreshToken(memberInfo.email());

        final String refreshToken = refreshTokenInfo.getRefreshToken();
        final Date expiration = refreshTokenInfo.getExpiration();

        tokenRepository.save(new RefreshTokenInfo(request.email(), refreshToken, expiration));

        ResponseCookie cookie = ResponseCookie
            .from("refreshToken", refreshToken)
            .sameSite("Lax")
            .secure(true)
            .httpOnly(true)
            .path("/")
            .maxAge(Duration.ofDays(30))
            .build();

        return ResponseEntity
            .status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new MemberSignInResponse(accessToken));
    }

}
