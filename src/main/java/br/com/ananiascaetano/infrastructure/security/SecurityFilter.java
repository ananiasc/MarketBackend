package br.com.ananiascaetano.infrastructure.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.ananiascaetano.application.services.authorization.AuthorizationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SecurityFilter extends OncePerRequestFilter {
	
	private final TokenService tokenService;
	
	private final AuthorizationService authorizationService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = recoverToken(request);
		
		if(token != null) {
			try {
				String username = tokenService.validateToken(token);
				UserDetails user = authorizationService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
				return;
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String recoverToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if(authorization == null) {
			return null;
		}
		
		return authorization.replace("Bearer ", "");
	}
	
}
