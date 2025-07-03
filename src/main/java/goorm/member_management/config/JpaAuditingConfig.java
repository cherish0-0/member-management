package goorm.member_management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// @EnableJpaAuditing : Auditing 기능을 활성화하기 위한 어노테이션
@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig {

}
