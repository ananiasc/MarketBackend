package br.com.ananiascaetano.infrastructure.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.ananiascaetano.constants.ErrorMessages;
import br.com.ananiascaetano.domain.entities.user.User;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String generateToken(User user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create()
					.withIssuer("marketbackend")
					.withSubject(user.getUsername())
					.withExpiresAt(generateExpirationDate())
					.sign(algorithm);
			
			return token;
		} catch (JWTCreationException e) {
			throw new RuntimeException(ErrorMessages.JWT_GENERATE_TOKEN_FAIL, e);
		}
	}
	
	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			
			return JWT.require(algorithm)
					.withIssuer("marketbackend")
					.build()
					.verify(token)
					.getSubject();
		} catch (JWTVerificationException e) {
			throw new RuntimeException(ErrorMessages.JWT_VERIFICATION_TOKEN_FAIL, e);
		}
	}
	
	private Instant generateExpirationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}
