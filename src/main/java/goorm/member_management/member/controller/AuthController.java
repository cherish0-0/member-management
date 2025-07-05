package goorm.member_management.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import goorm.member_management.member.dto.request.MemberCreateRequest;
import goorm.member_management.member.dto.request.MemberLoginRequest;
import goorm.member_management.member.dto.response.MemberLoginResponse;
import goorm.member_management.member.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/account")
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;

	@PostMapping("/create")
	public ResponseEntity<Void> createMember(@Valid @RequestBody MemberCreateRequest request) {
		authService.createMember(request);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/login")
	public ResponseEntity<MemberLoginResponse> loginMember(@Valid @RequestBody MemberLoginRequest request) {
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
