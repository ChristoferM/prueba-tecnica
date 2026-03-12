package com.fondos.inversion.controller;

import com.fondos.inversion.DTO.ProductoDTO;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Producto;
import com.fondos.inversion.services.ProductoService;
import com.fondos.inversion.util.ApiResponse;
import com.fondos.inversion.util.ApiResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> obtenerTodos() {
        try {
            List<ProductoDTO> productos = productoService.findAll()
                    .stream()
                    .map(ProductoDTO::new)
                    .collect(Collectors.toList());
            
            return ApiResponseBuilder.ok("Productos obtenidos exitosamente", productos);
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener productos: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                return ApiResponseBuilder.badRequest("ID debe ser mayor a 0");
            }
            
            Producto producto = productoService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id)
            );
            
            return ApiResponseBuilder.ok("Producto obtenido exitosamente", new ProductoDTO(producto));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al obtener producto: " + e.getMessage());
        }
    }


    @PostMapping
    public ResponseEntity<ApiResponse<ProductoDTO>> crearProducto(@RequestBody ProductoDTO productoDTO) {
        try {
            if (productoDTO == null || productoDTO.getNombre() == null || productoDTO.getNombre().isEmpty()) {
                throw new ValidacionException("El nombre del producto es obligatorio");
            }
            if (productoDTO.getTipoProducto() == null || productoDTO.getTipoProducto().isEmpty()) {
                throw new ValidacionException("El tipo de producto es obligatorio");
            }
            
            Producto producto = productoDTO.toEntity();
            productoService.validate(producto);
            Producto productoGuardado = productoService.save(producto);
            
            return ApiResponseBuilder.created("Producto creado exitosamente", new ProductoDTO(productoGuardado));
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al crear producto: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarProducto(@PathVariable Integer id, @RequestBody ProductoDTO productoDTO) {
        try {
            if (id == null || id <= 0) {
                return ApiResponseBuilder.badRequest("ID debe ser mayor a 0");
            }
            
            Producto productoExistente = productoService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id)
            );
            
            productoExistente.setNombre(productoDTO.getNombre());
            productoExistente.setTipoProducto(productoDTO.getTipoProducto());
            
            productoService.validate(productoExistente);
            Producto productoActualizado = productoService.update(productoExistente);
            
            return ApiResponseBuilder.ok("Producto actualizado exitosamente", new ProductoDTO(productoActualizado));
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (ValidacionException e) {
            return ApiResponseBuilder.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al actualizar producto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                return ApiResponseBuilder.badRequest("ID debe ser mayor a 0");
            }
            
            Producto producto = productoService.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id)
            );
            
            productoService.delete(producto);
            
            return ApiResponseBuilder.ok("Producto eliminado exitosamente", null);
        } catch (RecursoNoEncontradoException e) {
            return ApiResponseBuilder.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponseBuilder.internalServerError("Error al eliminar producto: " + e.getMessage());
        }
    }
}
