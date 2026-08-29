package com.davivienda.sv.app.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTService {
	public static final String AUTH_PREFIX = "Bearer ";

	private static final Logger LOGGER = LogManager.getLogger(JWTService.class);

	@Value("${com.davivienda.sv.app.jwt.key}")
	private String keyValue;

	@Value("${com.davivienda.sv.app.jwt.issuer}")
	private String issuer;

	@Value("${com.davivienda.sv.app.jwt.expiry}")
	private Integer expiry;

	@Deprecated
	public String generateToken(String sessionId) throws JwtException, IllegalArgumentException {
		Date expires = Date.from(LocalDateTime.now(ZoneOffset.UTC).plusSeconds(expiry).toInstant(ZoneOffset.UTC));
		SecretKey key = Keys.hmacShaKeyFor(keyValue.getBytes());

		return Jwts.builder().setSubject(sessionId).setExpiration(expires).setIssuer(issuer).signWith(key).compact();
	}

	public String generateToken(String sessionId, String username, String dni, String niu)
			throws JwtException, IllegalArgumentException {
		Date expires = Date.from(LocalDateTime.now(ZoneOffset.UTC).plusSeconds(expiry).toInstant(ZoneOffset.UTC));
		SecretKey key = Keys.hmacShaKeyFor(keyValue.getBytes());

		Map<String, Object> claims = new HashMap<String, Object>() {
			{
				put("user", username);
				put("dni", dni);
				put("niu", niu);
			}
		};

		return Jwts.builder().setSubject(sessionId).setExpiration(expires).setIssuer(issuer).signWith(key)
				.addClaims(claims).compact();
	}

	public Claims verifyToken(String tokenValue) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(keyValue.getBytes());

			Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(tokenValue);

			return claims.getBody();
		} catch (JwtException | IllegalArgumentException e) {
			LOGGER.error("Exception in verifyToken para el valor " + tokenValue + ": " + e.getMessage(), e);
			return null;
		}
	}

	public HttpHeaders generateHeaders(String jwt) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, JWTService.AUTH_PREFIX + jwt);
		headers.add("Access-Control-Expose-Headers", "Authorization");

		return headers;
	}
}
