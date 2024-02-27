package by.potapchuk.auditreportservice.controller.filter;

import by.potapchuk.auditreportservice.core.dto.UserDetailsDto;
import by.potapchuk.auditreportservice.service.jwt.JwtHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isEmpty;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtHandler jwtHandler;

    public JwtFilter(JwtHandler jwtHandler) {
        this.jwtHandler = jwtHandler;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String header = request.getHeader(AUTHORIZATION);
        if (request.getRequestURI().contains("actuator")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            throw new RuntimeException("No token");
        }
        final String token = header.split(" ")[1].trim();
        if (!jwtHandler.validate(token)) {
            throw new RuntimeException("Not valid token");
        }

        UserDetailsDto userDetailsDtoDto = jwtHandler.getUserDetailsDtoFromJwt(token);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDetailsDtoDto.getRole()));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetailsDtoDto,
                null,
                authorities
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
