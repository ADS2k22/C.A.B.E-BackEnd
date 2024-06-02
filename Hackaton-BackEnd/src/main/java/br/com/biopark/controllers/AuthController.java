package br.com.biopark.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.biopark.vo.security.AccountCredentialsVO;
import br.com.biopark.services.AuthService;
import br.com.biopark.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Endpoint", description = "Endpoints para autenticação")
public class AuthController {

	@Autowired
	AuthService service;
	
	@PostMapping(value = "/login", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Autentica o usuário", description = "Autentica o usuário e retorna um token", tags = { "Authentication Endpoint" })
	public ResponseEntity<?> login(@RequestBody AccountCredentialsVO data) {
		if(checkIfDataIsNull(data)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		var token = service.login(data);
		if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		return token;
	}
	
	@PutMapping(value = "/refresh/{username}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Atualiza token para o usuário autenticado", description = "Atualiza token para o usuário autenticado e retorna um token", tags = { "Authentication Endpoint" })
	public ResponseEntity<?> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
		if(checkIfRefreshTokenIsNull(username, refreshToken)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		var token = service.refreshToken(username, refreshToken);
		if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		return token;
	}

	private boolean checkIfRefreshTokenIsNull(String username, String refreshToken) {
		return refreshToken == null || refreshToken.isBlank() ||
				username == null || username.isBlank();
	}

	private boolean checkIfDataIsNull(AccountCredentialsVO data) {
		return data == null ||
				data.getUsername() == null ||
				data.getUsername().isBlank() ||
				data.getPassword() == null ||
				data.getPassword().isBlank();
	}
}
