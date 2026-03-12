package com.fondos.inversion.controller;

import com.fondos.inversion.DTO.UsuariosDTO;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Usuarios;
import com.fondos.inversion.services.UsuariosService;
import com.fondos.inversion.util.ApiResponse;
import com.fondos.inversion.util.ApiResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuariosDTO>>> obtenerTodos() {
        try {
            List<UsuariosDTO> usuarios = usuariosService.findAll()
                    .stream()
                    .map(UsuariosDTO::new)
                    .collect(Collectors.toList());
            
            return ApiResponseBuilder.ok("Usuarios obtenidos exitosamente", usuarios);
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener usuarios: " + e.getMessage());
        }
    }
    @PreAuthorize("authenticated")
    @GetMapping("/{correo}")
    public ResponseEntity<ApiResponse<UsuariosDTO>> obtenerPorCorreo(@PathVariable String correo) {
        try {
            if (correo == null || correo.isEmpty()) {
                return ApiResponseBuilder.badRequest("El correo es obligatorio");
            }
            
            Usuarios usuario = usuariosService.findById(correo).orElseThrow(
                () -> new RecursoNoEncontradoException("Usuario no encontrado con correo: " + correo)
            );
            
            return ApiResponseBuilder.ok("Usuario obtenido exitosamente", new UsuariosDTO(usuario));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener usuario: " + e.getMessage());
        }
    }

    /**
     * Registrar nuevo usuario (SIN AUTENTICACION - publico)
     * POST /api/usuarios/registro
     */
    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<UsuariosDTO>> registroUsuario(@RequestBody UsuariosDTO usuariosDTO) {
        try {
            if (usuariosDTO == null || usuariosDTO.getCorreo() == null || usuariosDTO.getCorreo().isEmpty()) {
                throw new ValidacionException("El correo del usuario es obligatorio");
            }
            if (usuariosDTO.getNombre() == null || usuariosDTO.getNombre().isEmpty()) {
                throw new ValidacionException("El nombre del usuario es obligatorio");
            }
            if (usuariosDTO.getContrasena() == null || usuariosDTO.getContrasena().isEmpty()) {
                throw new ValidacionException("La contraseña del usuario es obligatoria");
            }
            if (usuariosDTO.getRole() == null || usuariosDTO.getRole().isEmpty()) {
                throw new ValidacionException("El role del usuario es obligatorio");
            }
            
            Usuarios usuario = usuariosDTO.toEntity();
            usuariosService.validate(usuario);
            Usuarios usuarioGuardado = usuariosService.save(usuario);
            
            return ApiResponseBuilder.created("Usuario registrado exitosamente", new UsuariosDTO(usuarioGuardado));
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al registrar usuario: " + e.getMessage());
        }
    }

    @PreAuthorize("authenticated")
    @PutMapping("/{correo}")
    public ResponseEntity<ApiResponse<UsuariosDTO>> actualizarUsuario(@PathVariable String correo, @RequestBody UsuariosDTO usuariosDTO) {
        try {
            if (correo == null || correo.isEmpty()) {
                return ApiResponseBuilder.badRequest("El correo es obligatorio");
            }
            
            Usuarios usuarioExistente = usuariosService.findById(correo).orElseThrow(
                () -> new RecursoNoEncontradoException("Usuario no encontrado con correo: " + correo)
            );
            
            usuarioExistente.setNombre(usuariosDTO.getNombre());
            usuarioExistente.setContrasena(usuariosDTO.getContrasena());
            usuarioExistente.setRole(usuariosDTO.getRole());
            usuarioExistente.setActivo(usuariosDTO.getActivo());
            
            usuariosService.validate(usuarioExistente);
            Usuarios usuarioActualizado = usuariosService.update(usuarioExistente);
            
            return ApiResponseBuilder.ok("Usuario actualizado exitosamente", new UsuariosDTO(usuarioActualizado));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{correo}")
    public ResponseEntity<ApiResponse<Void>> eliminarUsuario(@PathVariable String correo) {
        try {
            if (correo == null || correo.isEmpty()) {
                return ApiResponseBuilder.badRequest("El correo es obligatorio");
            }
            
            Usuarios usuario = usuariosService.findById(correo).orElseThrow(
                () -> new RecursoNoEncontradoException("Usuario no encontrado con correo: " + correo)
            );
            
            usuariosService.delete(usuario);
            
            return ApiResponseBuilder.ok("Usuario eliminado exitosamente", null);
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al eliminar usuario: " + e.getMessage());
        }
    }
}
