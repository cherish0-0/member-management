package goorm.member_management.error.dto;

import java.util.List;

public record ErrorResponse(
        List<String> messages
) {
}
