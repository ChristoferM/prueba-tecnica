package com.fondos.inversion.controller;

import com.fondos.inversion.DTO.InscripcionDTO;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Inscripcion;
import com.fondos.inversion.model.Producto;
import com.fondos.inversion.model.Cliente;
import com.fondos.inversion.services.InscripcionService;
import com.fondos.inversion.services.ProductoService;
import com.fondos.inversion.services.ClienteService;
import com.fondos.inversion.util.ApiResponse;
import com.fondos.inversion.util.ApiResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inscripciones")
@CrossOrigin(origins = "*")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InscripcionDTO>>> obtenerTodos() {
        try {
            List<InscripcionDTO> inscripciones = inscripcionService.findAll()
                    .stream()
                    .map(InscripcionDTO::new)
                    .collect(Collectors.toList());
            
            return ApiResponseBuilder.ok("Inscripciones obtenidas exitosamente", inscripciones);
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener inscripciones: " + e.getMessage());
        }
    }

    @GetMapping("/{idProducto}/{idCliente}")
    public ResponseEntity<ApiResponse<InscripcionDTO>> obtenerPorId(
            @PathVariable Integer idProducto,
            @PathVariable Integer idCliente) {
        try {
            if (idProducto == null || idProducto <= 0 || idCliente == null || idCliente <= 0) {
                return ApiResponseBuilder.badRequest("IDs deben ser mayores a 0");
            }
            
            com.fondos.inversion.model.InscripcionId id = new com.fondos.inversion.model.InscripcionId();
            id.setIdProducto(idProducto);
            id.setIdCliente(idCliente);
            
            Inscripcion inscripcion = inscripcionService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Inscripción no encontrada con ID: " + idProducto + "-" + idCliente)
            );
            
            return ApiResponseBuilder.ok("Inscripción obtenida exitosamente", new InscripcionDTO(inscripcion));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener inscripción: " + e.getMessage());
        }
    }

    /**
     * Crear nueva inscripción
     */
    @PostMapping
    public ResponseEntity<ApiResponse<InscripcionDTO>> crearInscripcion(@RequestBody InscripcionDTO inscripcionDTO) {
        try {
            if (inscripcionDTO == null || inscripcionDTO.getIdProducto() == null) {
                throw new ValidacionException("El ID del producto es obligatorio");
            }
            if (inscripcionDTO.getIdCliente() == null) {
                throw new ValidacionException("El ID del cliente es obligatorio");
            }
            
            // Validar que existan el producto y cliente
            Producto producto = productoService.findById(inscripcionDTO.getIdProducto()).orElseThrow(
                () -> new ValidacionException("Producto no encontrado con ID: " + inscripcionDTO.getIdProducto())
            );
            
            Cliente cliente = clienteService.findById(inscripcionDTO.getIdCliente()).orElseThrow(
                () -> new ValidacionException("Cliente no encontrado con ID: " + inscripcionDTO.getIdCliente())
            );
            
            Inscripcion inscripcion = new Inscripcion();
            com.fondos.inversion.model.InscripcionId id = new com.fondos.inversion.model.InscripcionId();
            id.setIdProducto(producto.getId());
            id.setIdCliente(cliente.getId());
            inscripcion.setId(id);
            inscripcion.setProducto(producto);
            inscripcion.setCliente(cliente);
            
            inscripcionService.validate(inscripcion);
            Inscripcion inscripcionGuardada = inscripcionService.save(inscripcion);
            
            return ApiResponseBuilder.created("Inscripción creada exitosamente", new InscripcionDTO(inscripcionGuardada));
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al crear inscripción: " + e.getMessage());
        }
    }

    /**
     * Actualizar inscripción
     */
    @PutMapping("/{idProducto}/{idCliente}")
    public ResponseEntity<ApiResponse<InscripcionDTO>> actualizarInscripcion(
            @PathVariable Integer idProducto,
            @PathVariable Integer idCliente,
            @RequestBody InscripcionDTO inscripcionDTO) {
        try {
            if (idProducto == null || idProducto <= 0 || idCliente == null || idCliente <= 0) {
                return ApiResponseBuilder.badRequest("IDs deben ser mayores a 0");
            }
            
            com.fondos.inversion.model.InscripcionId id = new com.fondos.inversion.model.InscripcionId();
            id.setIdProducto(idProducto);
            id.setIdCliente(idCliente);
            
            Inscripcion inscripcionExistente = inscripcionService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Inscripción no encontrada con ID: " + idProducto + "-" + idCliente)
            );
            
            if (inscripcionDTO.getIdProducto() != null) {
                Producto producto = productoService.findById(inscripcionDTO.getIdProducto()).orElseThrow(
                    () -> new ValidacionException("Producto no encontrado con ID: " + inscripcionDTO.getIdProducto())
                );
                inscripcionExistente.setProducto(producto);
            }
            
            if (inscripcionDTO.getIdCliente() != null) {
                Cliente cliente = clienteService.findById(inscripcionDTO.getIdCliente()).orElseThrow(
                    () -> new ValidacionException("Cliente no encontrado con ID: " + inscripcionDTO.getIdCliente())
                );
                inscripcionExistente.setCliente(cliente);
            }
            
            inscripcionService.validate(inscripcionExistente);
            Inscripcion inscripcionActualizada = inscripcionService.update(inscripcionExistente);
            
            return ApiResponseBuilder.ok("Inscripción actualizada exitosamente", new InscripcionDTO(inscripcionActualizada));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al actualizar inscripción: " + e.getMessage());
        }
    }

    /**
     * Eliminar inscripción por IDs compuestos
     */
    @DeleteMapping("/{idProducto}/{idCliente}")
    public ResponseEntity<ApiResponse<Void>> eliminarInscripcion(
            @PathVariable Integer idProducto,
            @PathVariable Integer idCliente) {
        try {
            if (idProducto == null || idProducto <= 0 || idCliente == null || idCliente <= 0) {
                return ApiResponseBuilder.badRequest("IDs deben ser mayores a 0");
            }
            
            com.fondos.inversion.model.InscripcionId id = new com.fondos.inversion.model.InscripcionId();
            id.setIdProducto(idProducto);
            id.setIdCliente(idCliente);
            
            Inscripcion inscripcion = inscripcionService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Inscripción no encontrada con ID: " + idProducto + "-" + idCliente)
            );
            
            inscripcionService.delete(inscripcion);
            
            return ApiResponseBuilder.ok("Inscripción eliminada exitosamente", null);
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al eliminar inscripción: " + e.getMessage());
        }
    }
}
