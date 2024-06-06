package br.com.biopark.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.biopark.vo.UserVO;
import br.com.biopark.dtos.PasswordDTO;
import br.com.biopark.services.UserService;
import br.com.biopark.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Administradores", description = "Endpoints para gerenciamento dos Administradores")
public class UserController {

	@Autowired
	UserService service;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Encontrar todos os administradores", description = "Encontrar todos os administradores", tags = {"Administradores"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200",
					content = {
						@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserVO.class))),
						@Content(mediaType = "application/xml", array = @ArraySchema(schema = @Schema(implementation = UserVO.class))),
						@Content(mediaType = "application/x-yaml", array = @ArraySchema(schema = @Schema(implementation = UserVO.class)))
					}
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public List<UserVO> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page){
		return service.findAll(page);
	}
	
	@GetMapping(value = "/{id}",
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Encontrar um administrador", description = "Encontrar um administrador pelo seu ID", tags = {"Administradores"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserVO.class)),
					@Content(mediaType = "application/xml", schema = @Schema(implementation = UserVO.class)),
					@Content(mediaType = "application/x-yaml", schema = @Schema(implementation = UserVO.class))
			}),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public UserVO findById(@PathVariable(value = "id") Long id) {
		return service.findById(id);
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Cadastrar um administrador", description = "Cadastrar um administrador por JSON, XML ou YAML", tags = {"Administradores"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserVO.class)),
					@Content(mediaType = "application/xml", schema = @Schema(implementation = UserVO.class)),
					@Content(mediaType = "application/x-yaml", schema = @Schema(implementation = UserVO.class))
			}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public UserVO save(@RequestBody UserVO admin) {
		return service.save(admin);
	}
	
	@PutMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Atualizar um administrador", description = "Atualizar um administrador por JSON, XML ou YAML", tags = {"Administradores"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserVO.class)),
					@Content(mediaType = "application/xml", schema = @Schema(implementation = UserVO.class)),
					@Content(mediaType = "application/x-yaml", schema = @Schema(implementation = UserVO.class))
			}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public UserVO update(@RequestBody UserVO admin) {
		return service.update(admin);
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletar um administrador", description = "Deletar um administrador pelo seu ID", tags = {"Administradores"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public void delete(@PathVariable(value = "id") Long id){
		service.delete(id);
	}
	
	@PatchMapping("/{id}/desable")
	@Operation(summary = "Desativar ou ativar um administrador", description = "Desativar ou ativar um administrador pelo seu ID", tags = {"Administradores"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserVO.class)),
					@Content(mediaType = "application/xml", schema = @Schema(implementation = UserVO.class)),
					@Content(mediaType = "application/x-yaml", schema = @Schema(implementation = UserVO.class))
			}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public UserVO desableUser(@PathVariable(value = "id") Long id){
		return service.desableUser(id);
	}
	
	@PatchMapping("/changepassword/{id}")
	@Operation(summary = "Alterar a senha de um administrador", description = "Alterar a senha de um administrador pelo seu ID e senha", tags = {"Administradores"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public void changePassword(@PathVariable(value = "id") Long id, @RequestBody PasswordDTO password){
		service.changePassword(id, password.getPassword());
	}
	
	@GetMapping(value = "/username/{username}",
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Encontrar um administrador pelo username", description = "Encontrar um administrador pelo seu username", tags = {"Administradores"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserVO.class)),
					@Content(mediaType = "application/xml", schema = @Schema(implementation = UserVO.class)),
					@Content(mediaType = "application/x-yaml", schema = @Schema(implementation = UserVO.class))
			}),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public UserVO findByUsername(@PathVariable(value = "username") String username) {
		return service.findByUsername(username);
	}
}
