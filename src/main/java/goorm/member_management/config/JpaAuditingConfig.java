package goorm.member_management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Auditing 기능을 활성화하기 위한 설정 클래스
@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig {

}
