package goorm.member_management.member.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponse<MemberResponse>(
    List<MemberResponse> content,
    int pageNumber,
    long totalElements,
    int totalPages
) {
    public static <MemberResponse> PageResponse<MemberResponse> from(Page<MemberResponse> page) {
        return new PageResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}
