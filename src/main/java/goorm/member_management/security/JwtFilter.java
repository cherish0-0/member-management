package goorm.member_management.security;

import java.io.IOException;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import goorm.member_management.error.exception.CustomException;
import goorm.member_management.member.entity.MemberDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = resolveToken(request);

        if (StringUtils.hasText(accessToken)) {
            try {
                final Claims validationResult = jwtProvider.validateToken(accessToken, false);
                handleValidToken(accessToken, validationResult);
            } catch (CustomException e) {
                handleInvalidToken(accessToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(7);
        }
        return Strings.EMPTY;
    }

    private void handleValidToken(String token, Claims validationResult) {
        final var userDetails = new MemberDetails(
            validationResult.get("id", Long.class),
            validationResult.getAudience(),
            token,
            validationResult.get("role", String.class).split(",")
        );
        final var authentication = UsernamePasswordAuthenticationToken.authenticated(userDetails,
            userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("AUTH SUCCEED: {}", authentication.getName());
    }

    private void handleInvalidToken(String token) {
        final var authentication = UsernamePasswordAuthenticationToken.unauthenticated(
            Strings.EMPTY, Strings.EMPTY);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("AUTH FAILED: {}", token);
    }

}
