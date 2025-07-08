package goorm.member_management.member.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class MemberDetails implements UserDetails {

    private final String email;
    private final String token;
    private final String[] roles;

    public MemberDetails(String email,
                         String token,
                         String[] roles) {
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

    @Override
    public String getPassword() {
        return this.token;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
