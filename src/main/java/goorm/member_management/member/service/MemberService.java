package goorm.member_management.member.service;

import org.springframework.data.domain.Pageable;

import goorm.member_management.member.dto.request.MemberUpdateRequest;
import goorm.member_management.member.dto.response.MemberResponse;
import goorm.member_management.member.dto.response.PageResponse;

public interface MemberService {

    PageResponse<MemberResponse> getMembers(Pageable pageable);

    MemberResponse updateMember(Long id, MemberUpdateRequest request);

}
