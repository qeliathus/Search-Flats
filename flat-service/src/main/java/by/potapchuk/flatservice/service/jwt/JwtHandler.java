package by.potapchuk.flatservice.service.jwt;

import by.potapchuk.flatservice.config.properties.JwtProperty;
import by.potapchuk.flatservice.core.dto.UserDetailsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtHandler {

    private final JwtProperty jwtProperty;
    private final ObjectMapper objectMapper;

    public JwtHandler(JwtProperty jwtProperty, ObjectMapper objectMapper) {
        this.jwtProperty = jwtProperty;
        this.objectMapper = objectMapper;
    }

    public UserDetailsDto getUserDetailsDtoFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperty.getSecret())
                .parseClaimsJws(token)
                .getBody();

        return convertDtoFromJson(claims.getSubject());
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperty.getSecret()).parseClaimsJws(token);
            return true;
        } catch (SignatureException exception) {
            log.error("Invalid JWT signature " + exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.error("Invalid JWT token " + exception.getMessage());
        } catch (ExpiredJwtException exception) {
            log.error("Expired JWT token " + exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.error("Unsupported JWT token " + exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.error("JWT claims string is empty " + exception.getMessage());
        }
        return false;
    }

    private UserDetailsDto convertDtoFromJson(String json) {
        try {
            return objectMapper.readValue(json, UserDetailsDto.class);
        } catch (Exception exception) {
            throw new RuntimeException("Object mapper filed while reading value: " + exception.getMessage());
        }
    }
}
