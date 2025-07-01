package goorm.member_management.member.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import goorm.member_management.member.dto.PrincipalDetails;
import goorm.member_management.member.dto.request.MemberUpdateRequest;
import goorm.member_management.member.dto.response.MemberFindResponse;
import goorm.member_management.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	// 회원 리스트 조회 (어드민)
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<MemberFindResponse>> findAllMembers() {
		List<MemberFindResponse> AllMembersList = memberService.findAllMembers();
		return ResponseEntity.status(HttpStatus.OK).body(AllMembersList);
	}

	// 특정 회원 정보 조회 (어드민)
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MemberFindResponse> findMemberById(@PathVariable Long id) {
		MemberFindResponse member = memberService.findMemberById(id);
		return ResponseEntity.status(HttpStatus.OK).body(member);
	}

	// 내 정보 조회
	// TODO 조회 안 됨, 수정 필요
	@GetMapping("/me")
	public ResponseEntity<MemberFindResponse> findMe(@AuthenticationPrincipal PrincipalDetails loginMember) {
		MemberFindResponse member = memberService.findMe(loginMember);
		return ResponseEntity.status(HttpStatus.OK).body(member);
	}

	// 특정 회원 정보 수정 (어드민)
	// TODO 수정 안 됨, 수정 필요
	@PatchMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> updateMember(@PathVariable Long id, MemberUpdateRequest request) {
		memberService.updateMemberById(id, request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	// 내 정보 수정
	// TODO 수정 안 됨, 수정 필요
	@PatchMapping("/me")
	public ResponseEntity<Void> updateMe(@AuthenticationPrincipal PrincipalDetails loginMember,
			MemberUpdateRequest request) {
		memberService.updateMemberById(loginMember.id(), request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	// 특정 회원 탈퇴 처리 (어드민)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
		memberService.deleteMemberById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	// 회원 탈퇴
	// TODO 탈퇴 안 됨, 수정 필요
	@DeleteMapping("/me")
	public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal PrincipalDetails loginMember) {
		memberService.deleteMe(loginMember);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
