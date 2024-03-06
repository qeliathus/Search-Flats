package by.potapchuk.flatservice.config;

import by.potapchuk.flatservice.controller.filter.JwtFilter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(SecurityProperties.IGNORED_ORDER)
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(GET, "/flats")
                .requestMatchers(GET, "/actuator/**")
                .requestMatchers(OPTIONS, "/**");
    }

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
                .requestMatchers(GET, "/flats").permitAll()
                .requestMatchers(POST, "/flats").hasAnyRole("ADMIN")
                .requestMatchers(POST, "/flats-scrapping/rent").hasAnyRole("ADMIN")
                .requestMatchers(POST, "/flats-scrapping/sale").hasAnyRole("ADMIN")
                .requestMatchers(GET, "/flats/{uuid}/bookmark").permitAll()
                .requestMatchers(DELETE, "/flats/{uuid}/bookmark").permitAll()
                .requestMatchers(GET, "/bookmark").authenticated()
        );

        http.addFilterBefore(
                filter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
