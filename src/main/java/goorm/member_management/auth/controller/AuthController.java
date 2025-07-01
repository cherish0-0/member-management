package goorm.member_management.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import goorm.member_management.auth.dto.request.MemberLoginRequest;
import goorm.member_management.auth.dto.request.MemberSignupRequest;
import goorm.member_management.auth.dto.response.MemberSignupResponse;
import goorm.member_management.member.dto.PrincipalDetails;
import goorm.member_management.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	// 회원 가입
	@PostMapping("/signup")
	public ResponseEntity<MemberSignupResponse> signup(@Valid @RequestBody MemberSignupRequest request) {
		MemberSignupResponse createdMember = authService.createMember(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
	}

	// 어드민 회원 가입
	@PostMapping("/signup/admin")
	public ResponseEntity<MemberSignupResponse> signupAsAdmin(@Valid @RequestBody MemberSignupRequest request) {
		MemberSignupResponse createdMember = authService.createMemberAsAdmin(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<PrincipalDetails> login(@Valid @RequestBody MemberLoginRequest request) {
		PrincipalDetails loginMember = authService.login(request);
		return ResponseEntity.status(HttpStatus.OK).body(loginMember);
	}

	// 로그아웃
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@AuthenticationPrincipal PrincipalDetails loginMember) {
		authService.logout(loginMember);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
