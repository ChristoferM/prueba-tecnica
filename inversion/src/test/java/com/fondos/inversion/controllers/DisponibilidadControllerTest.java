package com.fondos.inversion.controllers;

import com.fondos.inversion.DTO.DisponibilidadDTO;
import com.fondos.inversion.builders.DisponibilidadBuilder;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Disponibilidad;
import com.fondos.inversion.services.DisponibilidadService;
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
class DisponibilidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DisponibilidadService disponibilidadService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetTodasDisponibilidades_Retorna200() throws Exception {
        Disponibilidad disponibilidad = DisponibilidadBuilder.crear();
        when(disponibilidadService.findAll()).thenReturn(Arrays.asList(disponibilidad));

        mockMvc.perform(get("/api/disponibilidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true));

        verify(disponibilidadService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    void testGetDisponibilidadPorIdsCompuestas_Retorna200() throws Exception {
        Disponibilidad disponibilidad = DisponibilidadBuilder.crear();
        when(disponibilidadService.findById(any()))
                .thenReturn(Optional.of(disponibilidad));

        mockMvc.perform(get("/api/disponibilidades/" + DisponibilidadBuilder.SUCURSAL_ID + "/" + DisponibilidadBuilder.PRODUCTO_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true));

        verify(disponibilidadService, times(1)).findById(any());
    }

    @Test
    @WithMockUser
    void testGetDisponibilidadIdsNoExisten_Retorna404() throws Exception {
        when(disponibilidadService.findById(any()))
                .thenThrow(new RecursoNoEncontradoException("Disponibilidad no encontrada"));

        mockMvc.perform(get("/api/disponibilidades/999/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testCrearDisponibilidadValida_Retorna201() throws Exception {
        Disponibilidad disponibilidad = DisponibilidadBuilder.crear();
        
        DisponibilidadDTO dtoRequest = new DisponibilidadDTO();
        dtoRequest.setIdSucursal(DisponibilidadBuilder.SUCURSAL_ID);
        dtoRequest.setIdProducto(DisponibilidadBuilder.PRODUCTO_ID);

        when(disponibilidadService.save(any())).thenReturn(disponibilidad);

        mockMvc.perform(post("/api/disponibilidades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isCreated());

        verify(disponibilidadService, times(1)).save(any());
    }

    @Test
    @WithMockUser
    void testCrearDisponibilidadSinSucursal_Retorna400() throws Exception {
        DisponibilidadDTO dtoRequest = new DisponibilidadDTO();
        dtoRequest.setIdSucursal(null);
        dtoRequest.setIdProducto(DisponibilidadBuilder.PRODUCTO_ID);

        when(disponibilidadService.save(any()))
                .thenThrow(new ValidacionException("Sucursal requerida"));

        mockMvc.perform(post("/api/disponibilidades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testUpdateDisponibilidad_Retorna200() throws Exception {
        Disponibilidad disponibilidad = DisponibilidadBuilder.crear();
        when(disponibilidadService.findById(any()))
                .thenReturn(Optional.of(disponibilidad));
        when(disponibilidadService.update(any())).thenReturn(disponibilidad);

        DisponibilidadDTO dtoRequest = new DisponibilidadDTO();
        dtoRequest.setIdSucursal(DisponibilidadBuilder.SUCURSAL_ID);
        dtoRequest.setIdProducto(DisponibilidadBuilder.PRODUCTO_ID);

        mockMvc.perform(put("/api/disponibilidades/" + DisponibilidadBuilder.SUCURSAL_ID + "/" + DisponibilidadBuilder.PRODUCTO_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isOk());
    }
}
