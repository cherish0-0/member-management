package goorm.member_management.member.controller;

import static goorm.member_management.security.JwtFilter.*;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import goorm.member_management.member.dto.MemberInfo;
import goorm.member_management.member.dto.Tokens;
import goorm.member_management.member.dto.request.MemberSignInRequest;
import goorm.member_management.member.dto.request.MemberSignUpRequest;
import goorm.member_management.member.dto.response.MemberSignInResponse;
import goorm.member_management.member.entity.MemberDetails;
import goorm.member_management.member.entity.RoleType;
import goorm.member_management.member.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody MemberSignUpRequest request) {
        authService.signUp(request, RoleType.USER);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-up/admin")
    public ResponseEntity<Void> adminSignUp(@Valid @RequestBody MemberSignUpRequest request) {
        authService.signUp(request, RoleType.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<MemberSignInResponse> signIn(@Valid @RequestBody MemberSignInRequest request) {
        final MemberInfo memberInfo = authService.signIn(request.email(), request.password());
        final Tokens tokens = memberInfo.tokens();
        ResponseCookie cookie = createRefreshTokenCookie(tokens.refreshToken());

        return ResponseEntity
            .status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new MemberSignInResponse(tokens.accessToken()));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal MemberDetails memberDetails) {
        final String email = memberDetails.getEmail();
        authService.signOut(email);

        return ResponseEntity
            .status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, createRefreshTokenCookie("").toString())
            .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {
        final Tokens tokens = authService.refresh(refreshToken);
        ResponseCookie cookie = createRefreshTokenCookie(tokens.refreshToken());

        return ResponseEntity
            .status(HttpStatus.OK)
            .header(HttpHeaders.AUTHORIZATION, BEARER + tokens.accessToken())
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build();
    }

    private ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie
            .from("refreshToken", refreshToken)
            .sameSite("Lax")
            .secure(true)
            .httpOnly(true)
            .path("/")
            .maxAge(Duration.ofDays(30))
            .build();
    }

}
