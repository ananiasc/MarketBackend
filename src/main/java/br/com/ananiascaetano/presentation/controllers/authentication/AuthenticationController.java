package br.com.ananiascaetano.presentation.controllers.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ananiascaetano.domain.entities.user.User;
import br.com.ananiascaetano.infrastructure.security.TokenService;
import br.com.ananiascaetano.presentation.dtos.authentication.AuthenticationDTO;
import br.com.ananiascaetano.presentation.dtos.user.LoginResponseDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	private final AuthenticationManager authenticationManager;
	
	private final TokenService tokenService;
	
	@PostMapping
	public LoginResponseDTO login(@RequestBody AuthenticationDTO authenticationDTO) {
		UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(), authenticationDTO.getPassword());
		Authentication auth = this.authenticationManager.authenticate(usernamePassword);
		String token = tokenService.generateToken((User) auth.getPrincipal());
		
		return new LoginResponseDTO(token);
	}
}
