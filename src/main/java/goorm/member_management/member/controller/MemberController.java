package goorm.member_management.member.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import goorm.member_management.member.dto.request.MemberUpdateRequest;
import goorm.member_management.member.dto.response.MemberResponse;
import goorm.member_management.member.dto.response.PageResponse;
import goorm.member_management.member.entity.MemberDetails;
import goorm.member_management.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponse<MemberResponse>> getMembers(Pageable pageable) {
        final PageResponse<MemberResponse> members = memberService.getMembers(pageable);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(members);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id,
        @RequestBody MemberUpdateRequest request) {
        MemberResponse memberResponse = memberService.updateMember(id, request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberResponse);
    }

    @PatchMapping("/me")
    public ResponseEntity<MemberResponse> updateMember(
        @AuthenticationPrincipal MemberDetails memberDetails,
        @RequestBody MemberUpdateRequest request) {
        MemberResponse memberResponse = memberService.updateMember(memberDetails.getId(), request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberResponse);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal MemberDetails memberDetails) {
        memberService.deleteMember(memberDetails.getId());
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

}
