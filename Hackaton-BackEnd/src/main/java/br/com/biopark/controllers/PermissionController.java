package br.com.biopark.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.biopark.services.PermissionService;
import br.com.biopark.util.MediaType;
import br.com.biopark.vo.PermissionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/permission")
@Tag(name = "Permissões", description = "Endpoints para gerenciamento das Permissões dos Administradores.")
public class PermissionController {

	@Autowired
	PermissionService service;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Encontrar todas as permissões", description = "Encontrar todas as permissões dos administradores", tags = {"Permissões"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200",
					content = {
						@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PermissionVO.class))),
						@Content(mediaType = "application/xml", array = @ArraySchema(schema = @Schema(implementation = PermissionVO.class))),
						@Content(mediaType = "application/x-yaml", array = @ArraySchema(schema = @Schema(implementation = PermissionVO.class)))
					}
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public List<PermissionVO> findAll(){
		return service.findAll();
	}
	
	@GetMapping(value = "/{id}",
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Encontrar uma permissão", description = "Encontrar uma permissão pelo seu ID", tags = {"Permissões"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PermissionVO.class)),
					@Content(mediaType = "application/xml", schema = @Schema(implementation = PermissionVO.class)),
					@Content(mediaType = "application/x-yaml", schema = @Schema(implementation = PermissionVO.class))
			}),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public PermissionVO findById(@PathVariable(value = "id") Long id) {
		return service.findById(id);
	}
}
