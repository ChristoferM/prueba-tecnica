package com.fondos.inversion.controllers;

import com.fondos.inversion.DTO.ProductoDTO;
import com.fondos.inversion.builders.ProductoBuilder;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Producto;
import com.fondos.inversion.services.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetTodosProductos_Retorna200() throws Exception {
        Producto producto = ProductoBuilder.crear();
        when(productoService.findAll()).thenReturn(Arrays.asList(producto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true));

        verify(productoService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    void testGetProductoPorId_Retorna200() throws Exception {
        Producto producto = ProductoBuilder.crear();
        when(productoService.findById(ProductoBuilder.PRODUCTO_ID))
                .thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/productos/" + ProductoBuilder.PRODUCTO_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos.nombre").value(ProductoBuilder.NOMBRE));

        verify(productoService, times(1)).findById(ProductoBuilder.PRODUCTO_ID);
    }

    @Test
    @WithMockUser
    void testGetProductoPorIdNoExiste_Retorna404() throws Exception {
        when(productoService.findById(999))
                .thenThrow(new RecursoNoEncontradoException("Producto no encontrado"));

        mockMvc.perform(get("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testCrearProductoValido_Retorna201() throws Exception {
        Producto guardado = ProductoBuilder.crear();
        
        ProductoDTO dtoRequest = new ProductoDTO();
        dtoRequest.setNombre(ProductoBuilder.NOMBRE);
        dtoRequest.setTipoProducto(ProductoBuilder.TIPO_PRODUCTO);

        when(productoService.save(any())).thenReturn(guardado);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isCreated());

        verify(productoService, times(1)).save(any());
    }

    @Test
    @WithMockUser
    void testCrearProductoNombreNulo_Retorna400() throws Exception {
        ProductoDTO dtoRequest = new ProductoDTO();
        dtoRequest.setNombre(null);
        dtoRequest.setTipoProducto(ProductoBuilder.TIPO_PRODUCTO);

        when(productoService.save(any()))
                .thenThrow(new ValidacionException("Nombre requerido"));

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testUpdateProducto_Retorna200() throws Exception {
        Producto productoActualizado = ProductoBuilder.crear();
        productoActualizado.setNombre("Producto Actualizado");

        ProductoDTO dtoRequest = new ProductoDTO();
        dtoRequest.setNombre("Producto Actualizado");
        dtoRequest.setTipoProducto(ProductoBuilder.TIPO_PRODUCTO);

        when(productoService.findById(ProductoBuilder.PRODUCTO_ID))
                .thenReturn(Optional.of(productoActualizado));
        when(productoService.update(any())).thenReturn(productoActualizado);

        mockMvc.perform(put("/api/productos/" + ProductoBuilder.PRODUCTO_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isOk());
    }
}
