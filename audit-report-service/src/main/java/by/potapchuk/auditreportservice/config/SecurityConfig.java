package by.potapchuk.auditreportservice.config;

import by.potapchuk.auditreportservice.controller.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.HEAD;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter filter) throws Exception {
        http = http.cors().and().csrf().disable();
        http = http.sessionManagement().sessionCreationPolicy(STATELESS).and();
        http = http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, ex) -> response.setStatus(SC_UNAUTHORIZED))
                .accessDeniedHandler((request, response, ex) -> response.setStatus(SC_FORBIDDEN))
                .and();
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(GET, "/audit").hasAnyRole("ADMIN")
                .requestMatchers(GET, "/audit/{id}").hasAnyRole("ADMIN")
                .requestMatchers(POST, "/report/{type}").hasAnyRole("ADMIN")
                .requestMatchers(GET, "/report/{UUID}/export").hasAnyRole("ADMIN")
                .requestMatchers(HEAD, "/report/{id}/export").hasAnyRole("ADMIN")
                .requestMatchers(GET, "/report").hasAnyRole("ADMIN")
                .requestMatchers(POST, "/audit").hasAnyRole("SYSTEM")
        );
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}