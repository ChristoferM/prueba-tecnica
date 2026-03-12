package com.fondos.inversion.controller;

import com.fondos.inversion.DTO.DisponibilidadDTO;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Disponibilidad;
import com.fondos.inversion.model.Sucursal;
import com.fondos.inversion.model.Producto;
import com.fondos.inversion.services.DisponibilidadService;
import com.fondos.inversion.services.SucursalService;
import com.fondos.inversion.services.ProductoService;
import com.fondos.inversion.util.ApiResponse;
import com.fondos.inversion.util.ApiResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/disponibilidades")
@CrossOrigin(origins = "*")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DisponibilidadDTO>>> obtenerTodos() {
        try {
            List<DisponibilidadDTO> disponibilidades = disponibilidadService.findAll()
                    .stream()
                    .map(DisponibilidadDTO::new)
                    .collect(Collectors.toList());
            
            return ApiResponseBuilder.ok("Disponibilidades obtenidas exitosamente", disponibilidades);
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener disponibilidades: " + e.getMessage());
        }
    }

    @GetMapping("/{idSucursal}/{idProducto}")
    public ResponseEntity<ApiResponse<DisponibilidadDTO>> obtenerPorId(
            @PathVariable Integer idSucursal,
            @PathVariable Integer idProducto) {
        try {
            if (idSucursal == null || idSucursal <= 0 || idProducto == null || idProducto <= 0) {
                return ApiResponseBuilder.badRequest("IDs deben ser mayores a 0");
            }
            
            com.fondos.inversion.model.DisponibilidadId id = new com.fondos.inversion.model.DisponibilidadId();
            id.setIdSucursal(idSucursal);
            id.setIdProducto(idProducto);
            
            Disponibilidad disponibilidad = disponibilidadService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Disponibilidad no encontrada con ID: " + idSucursal + "-" + idProducto)
            );
            
            return ApiResponseBuilder.ok("Disponibilidad obtenida exitosamente", new DisponibilidadDTO(disponibilidad));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener disponibilidad: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DisponibilidadDTO>> crearDisponibilidad(@RequestBody DisponibilidadDTO disponibilidadDTO) {
        try {
            if (disponibilidadDTO == null || disponibilidadDTO.getIdSucursal() == null) {
                throw new ValidacionException("El ID de la sucursal es obligatorio");
            }
            if (disponibilidadDTO.getIdProducto() == null) {
                throw new ValidacionException("El ID del producto es obligatorio");
            }
            
            // Validar que existan la sucursal y el producto
            Sucursal sucursal = sucursalService.findById(disponibilidadDTO.getIdSucursal()).orElseThrow(
                () -> new ValidacionException("Sucursal no encontrada con ID: " + disponibilidadDTO.getIdSucursal())
            );
            
            Producto producto = productoService.findById(disponibilidadDTO.getIdProducto()).orElseThrow(
                () -> new ValidacionException("Producto no encontrado con ID: " + disponibilidadDTO.getIdProducto())
            );
            
            Disponibilidad disponibilidad = new Disponibilidad();
            com.fondos.inversion.model.DisponibilidadId id = new com.fondos.inversion.model.DisponibilidadId();
            id.setIdSucursal(sucursal.getId());
            id.setIdProducto(producto.getId());
            disponibilidad.setId(id);
            disponibilidad.setSucursal(sucursal);
            disponibilidad.setProducto(producto);
            
            disponibilidadService.validate(disponibilidad);
            Disponibilidad disponibilidadGuardada = disponibilidadService.save(disponibilidad);
            
            return ApiResponseBuilder.created("Disponibilidad creada exitosamente", new DisponibilidadDTO(disponibilidadGuardada));
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al crear disponibilidad: " + e.getMessage());
        }
    }

    
    @PutMapping("/{idSucursal}/{idProducto}")
    public ResponseEntity<ApiResponse<DisponibilidadDTO>> actualizarDisponibilidad(
            @PathVariable Integer idSucursal,
            @PathVariable Integer idProducto,
            @RequestBody DisponibilidadDTO disponibilidadDTO) {
        try {
            if (idSucursal == null || idSucursal <= 0 || idProducto == null || idProducto <= 0) {
                return ApiResponseBuilder.badRequest("IDs deben ser mayores a 0");
            }
            
            com.fondos.inversion.model.DisponibilidadId id = new com.fondos.inversion.model.DisponibilidadId();
            id.setIdSucursal(idSucursal);
            id.setIdProducto(idProducto);
            
            Disponibilidad disponibilidadExistente = disponibilidadService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Disponibilidad no encontrada con ID: " + idSucursal + "-" + idProducto)
            );
            
            if (disponibilidadDTO.getIdSucursal() != null) {
                Sucursal sucursal = sucursalService.findById(disponibilidadDTO.getIdSucursal()).orElseThrow(
                    () -> new ValidacionException("Sucursal no encontrada con ID: " + disponibilidadDTO.getIdSucursal())
                );
                disponibilidadExistente.setSucursal(sucursal);
            }
            
            if (disponibilidadDTO.getIdProducto() != null) {
                Producto producto = productoService.findById(disponibilidadDTO.getIdProducto()).orElseThrow(
                    () -> new ValidacionException("Producto no encontrado con ID: " + disponibilidadDTO.getIdProducto())
                );
                disponibilidadExistente.setProducto(producto);
            }
            
            disponibilidadService.validate(disponibilidadExistente);
            Disponibilidad disponibilidadActualizada = disponibilidadService.update(disponibilidadExistente);
            
            return ApiResponseBuilder.ok("Disponibilidad actualizada exitosamente", new DisponibilidadDTO(disponibilidadActualizada));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al actualizar disponibilidad: " + e.getMessage());
        }
    }

    @DeleteMapping("/{idSucursal}/{idProducto}")
    public ResponseEntity<ApiResponse<Void>> eliminarDisponibilidad(
            @PathVariable Integer idSucursal,
            @PathVariable Integer idProducto) {
        try {
            if (idSucursal == null || idSucursal <= 0 || idProducto == null || idProducto <= 0) {
                return ApiResponseBuilder.badRequest("IDs deben ser mayores a 0");
            }
            
            com.fondos.inversion.model.DisponibilidadId id = new com.fondos.inversion.model.DisponibilidadId();
            id.setIdSucursal(idSucursal);
            id.setIdProducto(idProducto);
            
            Disponibilidad disponibilidad = disponibilidadService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Disponibilidad no encontrada con ID: " + idSucursal + "-" + idProducto)
            );
            
            disponibilidadService.delete(disponibilidad);
            
            return ApiResponseBuilder.ok("Disponibilidad eliminada exitosamente", null);
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al eliminar disponibilidad: " + e.getMessage());
        }
    }
}
