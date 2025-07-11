package goorm.member_management.member.entity;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MemberDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String token;
    private final String[] roles;

    public MemberDetails(
        Long id,
        String email,
        String token,
        String[] roles) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(this.roles)
            .map(SimpleGrantedAuthority::new)
            .toList();
    }


    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;

    }

    @Override
    public String getPassword() {
        return this.token;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
