package com.sandy.project.security.util;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import com.sandy.project.security.model.AccessJWTToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JWTTokenFactory {
	
	private final Key secret;

	public AccessJWTToken createAccessJWTToken(String username, Collection<? extends GrantedAuthority> authorities) {
		Claims claims = Jwts.claims().subject(username)
		.add("scopes", authorities.stream().map(a->a.getAuthority()).collect(Collectors.toList())).build();
		
		// waktu kapan token dibuat (berdasarkan instant saat ini, timezone-agnostic)
		java.time.Instant now = java.time.Instant.now();
		Date currentTimeDate = Date.from(now);
		
		// waktu kapan token expired (15 menit dari sekarang)
		java.time.Instant expired = now.plus(15, java.time.temporal.ChronoUnit.MINUTES);
		Date expiredTimeDate = Date.from(expired);

		String token = Jwts.builder().claims(claims)
				.issuer("https://subrutin.com")
				.issuedAt(currentTimeDate)
				.expiration(expiredTimeDate)
				.signWith(secret).compact();
		
		return new AccessJWTToken(token, claims);
	}
}
