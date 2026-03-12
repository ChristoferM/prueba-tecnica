package com.fondos.inversion.controller;

import com.fondos.inversion.DTO.VisitanDTO;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Visitan;
import com.fondos.inversion.model.Sucursal;
import com.fondos.inversion.model.Cliente;
import com.fondos.inversion.services.VisitanService;
import com.fondos.inversion.services.SucursalService;
import com.fondos.inversion.services.ClienteService;
import com.fondos.inversion.util.ApiResponse;
import com.fondos.inversion.util.ApiResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/visitas")
@CrossOrigin(origins = "*")
public class VisitanController {

    @Autowired
    private VisitanService visitanService;

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private ClienteService clienteService;

    /**
     * Obtener todas las visitas
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<VisitanDTO>>> obtenerTodos() {
        try {
            List<VisitanDTO> visitas = visitanService.findAll()
                    .stream()
                    .map(VisitanDTO::new)
                    .collect(Collectors.toList());
            
            return ApiResponseBuilder.ok("Visitas obtenidas exitosamente", visitas);
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener visitas: " + e.getMessage());
        }
    }

    @GetMapping("/{idSucursal}/{idCliente}/{fechaVisita}")
    public ResponseEntity<ApiResponse<VisitanDTO>> obtenerPorId(
            @PathVariable Integer idSucursal,
            @PathVariable Integer idCliente,
            @PathVariable String fechaVisita) {
        try {
            if (idSucursal == null || idSucursal <= 0 || idCliente == null || idCliente <= 0 || fechaVisita == null) {
                return ApiResponseBuilder.badRequest("Todos los parámetros son obligatorios");
            }
            
            // Convertir fechaVisita de String a Date (formato: yyyy-MM-dd)
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fecha = sdf.parse(fechaVisita);
            
            com.fondos.inversion.model.VisitanId id = new com.fondos.inversion.model.VisitanId();
            id.setIdSucursal(idSucursal);
            id.setIdCliente(idCliente);
            id.setFechaVisita(fecha);
            
            Visitan visita = visitanService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Visita no encontrada con ID: " + idSucursal + "-" + idCliente + "-" + fechaVisita)
            );
            
            return ApiResponseBuilder.ok("Visita obtenida exitosamente", new VisitanDTO(visita));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (java.text.ParseException e) {
            return ApiResponseBuilder.badRequest("Formato de fecha inválido. Use yyyy-MM-dd: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener visita: " + e.getMessage());
        }
    }

    /**
     * Crear nueva visita
     */
    @PostMapping
    public ResponseEntity<ApiResponse<VisitanDTO>> crearVisita(@RequestBody VisitanDTO visitanDTO) {
        try {
            if (visitanDTO == null || visitanDTO.getIdSucursal() == null) {
                throw new ValidacionException("El ID de la sucursal es obligatorio");
            }
            if (visitanDTO.getIdCliente() == null) {
                throw new ValidacionException("El ID del cliente es obligatorio");
            }
            
            // Validar que existan la sucursal y el cliente
            Sucursal sucursal = sucursalService.findById(visitanDTO.getIdSucursal()).orElseThrow(
                () -> new ValidacionException("Sucursal no encontrada con ID: " + visitanDTO.getIdSucursal())
            );
            
            Cliente cliente = clienteService.findById(visitanDTO.getIdCliente()).orElseThrow(
                () -> new ValidacionException("Cliente no encontrado con ID: " + visitanDTO.getIdCliente())
            );
            
            Visitan visita = new Visitan();
            com.fondos.inversion.model.VisitanId id = new com.fondos.inversion.model.VisitanId();
            id.setIdSucursal(sucursal.getId());
            id.setIdCliente(cliente.getId());
            id.setFechaVisita(visitanDTO.getFechaVisita());
            visita.setId(id);
            visita.setSucursal(sucursal);
            visita.setCliente(cliente);
            
            visitanService.validate(visita);
            Visitan visitaGuardada = visitanService.save(visita);
            
            return ApiResponseBuilder.created("Visita creada exitosamente", new VisitanDTO(visitaGuardada));
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al crear visita: " + e.getMessage());
        }
    }

    /**
     * Actualizar visita
     */
    @PutMapping("/{idSucursal}/{idCliente}/{fechaVisita}")
    public ResponseEntity<ApiResponse<VisitanDTO>> actualizarVisita(
            @PathVariable Integer idSucursal,
            @PathVariable Integer idCliente,
            @PathVariable String fechaVisita,
            @RequestBody VisitanDTO visitanDTO) {
        try {
            if (idSucursal == null || idSucursal <= 0 || idCliente == null || idCliente <= 0 || fechaVisita == null) {
                return ApiResponseBuilder.badRequest("Todos los parámetros son obligatorios");
            }
            
            // Convertir fechaVisita de String a Date (formato: yyyy-MM-dd)
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fecha = sdf.parse(fechaVisita);
            
            com.fondos.inversion.model.VisitanId id = new com.fondos.inversion.model.VisitanId();
            id.setIdSucursal(idSucursal);
            id.setIdCliente(idCliente);
            id.setFechaVisita(fecha);
            
            Visitan visitaExistente = visitanService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Visita no encontrada con ID: " + idSucursal + "-" + idCliente + "-" + fechaVisita)
            );
            
            if (visitanDTO.getIdSucursal() != null) {
                Sucursal sucursal = sucursalService.findById(visitanDTO.getIdSucursal()).orElseThrow(
                    () -> new ValidacionException("Sucursal no encontrada con ID: " + visitanDTO.getIdSucursal())
                );
                visitaExistente.setSucursal(sucursal);
            }
            
            if (visitanDTO.getIdCliente() != null) {
                Cliente cliente = clienteService.findById(visitanDTO.getIdCliente()).orElseThrow(
                    () -> new ValidacionException("Cliente no encontrado con ID: " + visitanDTO.getIdCliente())
                );
                visitaExistente.setCliente(cliente);
            }
            
            if (visitanDTO.getFechaVisita() != null) {
                visitaExistente.setFechaVisita(visitanDTO.getFechaVisita());
            }
            
            visitanService.validate(visitaExistente);
            Visitan visitaActualizada = visitanService.update(visitaExistente);
            
            return ApiResponseBuilder.ok("Visita actualizada exitosamente", new VisitanDTO(visitaActualizada));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (java.text.ParseException e) {
            return ApiResponseBuilder.badRequest("Formato de fecha inválido. Use yyyy-MM-dd: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al actualizar visita: " + e.getMessage());
        }
    }

    /**
     * Eliminar visita por IDs compuestos
     */
    @DeleteMapping("/{idSucursal}/{idCliente}/{fechaVisita}")
    public ResponseEntity<ApiResponse<Void>> eliminarVisita(
            @PathVariable Integer idSucursal,
            @PathVariable Integer idCliente,
            @PathVariable String fechaVisita) {
        try {
            if (idSucursal == null || idSucursal <= 0 || idCliente == null || idCliente <= 0 || fechaVisita == null) {
                return ApiResponseBuilder.badRequest("Todos los parámetros son obligatorios");
            }
            
            // Convertir fechaVisita de String a Date (formato: yyyy-MM-dd)
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fecha = sdf.parse(fechaVisita);
            
            com.fondos.inversion.model.VisitanId id = new com.fondos.inversion.model.VisitanId();
            id.setIdSucursal(idSucursal);
            id.setIdCliente(idCliente);
            id.setFechaVisita(fecha);
            
            Visitan visita = visitanService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Visita no encontrada con ID: " + idSucursal + "-" + idCliente + "-" + fechaVisita)
            );
            
            visitanService.delete(visita);
            
            return ApiResponseBuilder.ok("Visita eliminada exitosamente", null);
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al eliminar visita: " + e.getMessage());
        }
    }

}
