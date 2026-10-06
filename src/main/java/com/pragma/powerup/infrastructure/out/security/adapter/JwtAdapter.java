package com.pragma.powerup.infrastructure.out.security.adapter;

import com.pragma.powerup.domain.spi.ITokenPersistencePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAdapter implements ITokenPersistencePort {

    private final String secret;
    private final long expiration;

    public JwtAdapter(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generarToken(Long id, String correo, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("rol", rol);

        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(correo)
                .setIssuedAt(ahora)
                .setExpiration(fechaExpiracion)
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extraerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean esTokenValido(String token) {
        try {
            Claims claims = extraerClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
