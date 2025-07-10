package goorm.member_management.member.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import goorm.member_management.member.dto.response.MemberResponse;
import goorm.member_management.member.dto.response.PageResponse;
import goorm.member_management.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<MemberResponse>> getMembers(Pageable pageable) {
        final PageResponse<MemberResponse> members = memberService.getMembers(pageable);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(members);
    }

}
