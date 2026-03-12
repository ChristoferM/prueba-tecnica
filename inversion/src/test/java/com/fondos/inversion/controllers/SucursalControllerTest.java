package com.fondos.inversion.controllers;

import com.fondos.inversion.DTO.SucursalDTO;
import com.fondos.inversion.builders.SucursalBuilder;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Sucursal;
import com.fondos.inversion.services.SucursalService;
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
class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SucursalService sucursalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetTodasSucursales_Retorna200() throws Exception {
        Sucursal sucursal = SucursalBuilder.crear();
        when(sucursalService.findAll()).thenReturn(Arrays.asList(sucursal));

        mockMvc.perform(get("/api/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true));

        verify(sucursalService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    void testGetSucursalPorId_Retorna200() throws Exception {
        Sucursal sucursal = SucursalBuilder.crear();
        when(sucursalService.findById(SucursalBuilder.SUCURSAL_ID))
                .thenReturn(Optional.of(sucursal));

        mockMvc.perform(get("/api/sucursales/" + SucursalBuilder.SUCURSAL_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos.nombre").value(SucursalBuilder.NOMBRE));

        verify(sucursalService, times(1)).findById(SucursalBuilder.SUCURSAL_ID);
    }

    @Test
    @WithMockUser
    void testGetSucursalPorIdNoExiste_Retorna404() throws Exception {
        when(sucursalService.findById(999))
                .thenThrow(new RecursoNoEncontradoException("Sucursal no encontrada"));

        mockMvc.perform(get("/api/sucursales/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testCrearSucursalValida_Retorna201() throws Exception {
        Sucursal guardada = SucursalBuilder.crear();
        
        SucursalDTO dtoRequest = new SucursalDTO();
        dtoRequest.setNombre(SucursalBuilder.NOMBRE);
        dtoRequest.setCiudad(SucursalBuilder.CIUDAD);

        when(sucursalService.save(any())).thenReturn(guardada);

        mockMvc.perform(post("/api/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isCreated());

        verify(sucursalService, times(1)).save(any());
    }

    @Test
    @WithMockUser
    void testCrearSucursalNombreNulo_Retorna400() throws Exception {
        SucursalDTO dtoRequest = new SucursalDTO();
        dtoRequest.setNombre(null);
        dtoRequest.setCiudad(SucursalBuilder.CIUDAD);

        when(sucursalService.save(any()))
                .thenThrow(new ValidacionException("Nombre requerido"));

        mockMvc.perform(post("/api/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isBadRequest());
    }
}
