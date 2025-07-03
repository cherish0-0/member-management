package goorm.member_management.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import goorm.member_management.member.dto.request.MemberCreateRequest;
import goorm.member_management.member.entity.Member;
import goorm.member_management.member.entity.RoleType;
import goorm.member_management.member.repository.MemberRepository;
import goorm.member_management.member.service.AuthService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/account")
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;


	@PostMapping("/create")
	public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest request) {
		authService.createMember(request);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
