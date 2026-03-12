package com.fondos.inversion.controller;

import com.fondos.inversion.DTO.SucursalDTO;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Sucursal;
import com.fondos.inversion.services.SucursalService;
import com.fondos.inversion.util.ApiResponse;
import com.fondos.inversion.util.ApiResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sucursales")
@CrossOrigin(origins = "*")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SucursalDTO>>> obtenerTodos() {
        try {
            List<SucursalDTO> sucursales = sucursalService.findAll()
                    .stream()
                    .map(SucursalDTO::new)
                    .collect(Collectors.toList());
            
            return ApiResponseBuilder.ok("Sucursales obtenidas exitosamente", sucursales);
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener sucursales: " + e.getMessage());
        }
    }

    /**
     * Obtener sucursal por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SucursalDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                return ApiResponseBuilder.badRequest("ID debe ser mayor a 0");
            }
            
            Sucursal sucursal = sucursalService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Sucursal no encontrada con ID: " + id)
            );
            
            return ApiResponseBuilder.ok("Sucursal obtenida exitosamente", new SucursalDTO(sucursal));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener sucursal: " + e.getMessage());
        }
    }

    /**
     * Crear nueva sucursal
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SucursalDTO>> crearSucursal(@RequestBody SucursalDTO sucursalDTO) {
        try {
            if (sucursalDTO == null || sucursalDTO.getNombre() == null || sucursalDTO.getNombre().isEmpty()) {
                throw new ValidacionException("El nombre de la sucursal es obligatorio");
            }
            
            Sucursal sucursal = sucursalDTO.toEntity();
            sucursalService.validate(sucursal);
            Sucursal sucursalGuardada = sucursalService.save(sucursal);
            
            return ApiResponseBuilder.created("Sucursal creada exitosamente", new SucursalDTO(sucursalGuardada));
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al crear sucursal: " + e.getMessage());
        }
    }

    /**
     * Actualizar sucursal
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SucursalDTO>> actualizarSucursal(@PathVariable Integer id, @RequestBody SucursalDTO sucursalDTO) {
        try {
            if (id == null || id <= 0) {
                return ApiResponseBuilder.badRequest("ID debe ser mayor a 0");
            }
            
            Sucursal sucursalExistente = sucursalService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Sucursal no encontrada con ID: " + id)
            );
            
            sucursalExistente.setNombre(sucursalDTO.getNombre());
            sucursalExistente.setCiudad(sucursalDTO.getCiudad());
            
            sucursalService.validate(sucursalExistente);
            Sucursal sucursalActualizada = sucursalService.update(sucursalExistente);
            
            return ApiResponseBuilder.ok("Sucursal actualizada exitosamente", new SucursalDTO(sucursalActualizada));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al actualizar sucursal: " + e.getMessage());
        }
    }

    /**
     * Eliminar sucursal por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarSucursal(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                return ApiResponseBuilder.badRequest("ID debe ser mayor a 0");
            }
            
            Sucursal sucursal = sucursalService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Sucursal no encontrada con ID: " + id)
            );
            
            sucursalService.delete(sucursal);
            
            return ApiResponseBuilder.ok("Sucursal eliminada exitosamente", null);
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al eliminar sucursal: " + e.getMessage());
        }
    }
}
