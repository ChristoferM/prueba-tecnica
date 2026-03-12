package com.fondos.inversion.controller;

import com.fondos.inversion.DTO.ClienteDTO;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Cliente;
import com.fondos.inversion.services.ClienteService;
import com.fondos.inversion.util.ApiResponse;
import com.fondos.inversion.util.ApiResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteDTO>>> obtenerTodos() {
        try {
            List<ClienteDTO> clientes = clienteService.findAll()
                    .stream()
                    .map(ClienteDTO::new)
                    .collect(Collectors.toList());
            
            return ApiResponseBuilder.ok("Clientes obtenidos exitosamente", clientes);
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener clientes: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                return ApiResponseBuilder.badRequest("ID debe ser mayor a 0");
            }
            
            Cliente cliente = clienteService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id)
            );
            
            return ApiResponseBuilder.ok("Cliente obtenido exitosamente", new ClienteDTO(cliente));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener cliente: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteDTO>> crearCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            if (clienteDTO == null || clienteDTO.getNombre() == null || clienteDTO.getNombre().isEmpty()) {
                throw new ValidacionException("El nombre del cliente es obligatorio");
            }
            
            Cliente cliente = clienteDTO.toEntity();
            clienteService.validate(cliente);
            Cliente clienteGuardado = clienteService.save(cliente);
            
            return ApiResponseBuilder.created("Cliente creado exitosamente", new ClienteDTO(clienteGuardado));
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al crear cliente: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> actualizarCliente(@PathVariable Integer id, @RequestBody ClienteDTO clienteDTO) {
        try {
            if (id == null || id <= 0) {
                return ApiResponseBuilder.badRequest("ID debe ser mayor a 0");
            }
            
            Cliente clienteExistente = clienteService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id)
            );
            
            clienteExistente.setNombre(clienteDTO.getNombre());
            clienteExistente.setApellidos(clienteDTO.getApellidos());
            clienteExistente.setCiudad(clienteDTO.getCiudad());
            
            clienteService.validate(clienteExistente);
            Cliente clienteActualizado = clienteService.update(clienteExistente);
            
            return ApiResponseBuilder.ok("Cliente actualizado exitosamente", new ClienteDTO(clienteActualizado));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al actualizar cliente: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCliente(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                return ApiResponseBuilder.badRequest("ID debe ser mayor a 0");
            }
            
            Cliente cliente = clienteService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id)
            );
            
            clienteService.delete(cliente);
            
            return ApiResponseBuilder.ok("Cliente eliminado exitosamente", null);
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al eliminar cliente: " + e.getMessage());
        }
    }
}
