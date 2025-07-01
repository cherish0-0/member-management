package goorm.member_management.member.entity;

import static jakarta.persistence.GenerationType.*;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Table(name = "member")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@GeneratedValue(strategy = IDENTITY)
	@Id
	@Column(name = "member_id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private RoleType role;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	public Member(String name, String email, String password, RoleType role) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public RoleType getRole() {
		return role;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateEmail(String email) {
		this.email = email;
	}

	public void updatePassword(String password) {
		this.password = password;
	}
}
