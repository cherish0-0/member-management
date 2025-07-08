package goorm.member_management.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberCreateRequest(

        @Size(max = 20, message = "이름은 최대 {max}자까지 입력할 수 있습니다.")
        @NotBlank(message = "이름은 필수 입력값입니다.")
        String name,

        @Email(message = "잘못된 이메일 형식입니다.")
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        String email,

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%&*?]{8,}$", message = "비밀번호는 최소 8자 이상, 영문자와 숫자를 포함해야 하며, 허용된 특수문자만 사용할 수 있습니다.")
        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        String password

) {
}
