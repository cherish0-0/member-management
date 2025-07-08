package goorm.member_management.member.controller;

import goorm.member_management.member.dto.MemberInfo;
import goorm.member_management.member.dto.request.MemberCreateRequest;
import goorm.member_management.member.dto.request.MemberLoginRequest;
import goorm.member_management.member.dto.response.MemberLoginResponse;
import goorm.member_management.member.service.AuthService;
import goorm.member_management.security.JwtProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/account")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/create")
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberCreateRequest request) {
        authService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(@Valid @RequestBody MemberLoginRequest request) {
        final MemberInfo memberInfo = authService.loginMember(request.email(), request.password());
        final String token = jwtProvider.createAccessToken(memberInfo.email(), memberInfo.role());

        return ResponseEntity.status(HttpStatus.OK).body(new MemberLoginResponse(token));
    }

}
