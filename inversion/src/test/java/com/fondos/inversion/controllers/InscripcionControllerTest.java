package com.fondos.inversion.controllers;

import com.fondos.inversion.DTO.InscripcionDTO;
import com.fondos.inversion.builders.InscripcionBuilder;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Inscripcion;
import com.fondos.inversion.services.InscripcionService;
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
class InscripcionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InscripcionService inscripcionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetTodasInscripciones_Retorna200() throws Exception {
        Inscripcion inscripcion = InscripcionBuilder.crear();
        when(inscripcionService.findAll()).thenReturn(Arrays.asList(inscripcion));

        mockMvc.perform(get("/api/inscripciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true));

        verify(inscripcionService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    void testGetInscripcionPorIdsCompuestas_Retorna200() throws Exception {
        Inscripcion inscripcion = InscripcionBuilder.crear();
        when(inscripcionService.findById(any()))
                .thenReturn(Optional.of(inscripcion));

        mockMvc.perform(get("/api/inscripciones/" + InscripcionBuilder.PRODUCTO_ID + "/" + InscripcionBuilder.CLIENTE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true));

        verify(inscripcionService, times(1)).findById(any());
    }

    @Test
    @WithMockUser
    void testGetInscripcionIdsNoExisten_Retorna404() throws Exception {
        when(inscripcionService.findById(any()))
                .thenThrow(new RecursoNoEncontradoException("Inscripcion no encontrada"));

        mockMvc.perform(get("/api/inscripciones/999/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testCrearInscripcionValida_Retorna201() throws Exception {
        Inscripcion inscripcion = InscripcionBuilder.crear();
        
        InscripcionDTO dtoRequest = new InscripcionDTO();
        dtoRequest.setIdProducto(InscripcionBuilder.PRODUCTO_ID);
        dtoRequest.setIdCliente(InscripcionBuilder.CLIENTE_ID);

        when(inscripcionService.save(any())).thenReturn(inscripcion);

        mockMvc.perform(post("/api/inscripciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isCreated());

        verify(inscripcionService, times(1)).save(any());
    }

    @Test
    @WithMockUser
    void testCrearInscripcionSinProducto_Retorna400() throws Exception {
        InscripcionDTO dtoRequest = new InscripcionDTO();
        dtoRequest.setIdProducto(null);
        dtoRequest.setIdCliente(InscripcionBuilder.CLIENTE_ID);

        when(inscripcionService.save(any()))
                .thenThrow(new ValidacionException("Producto requerido"));

        mockMvc.perform(post("/api/inscripciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testDeleteInscripcionPorIdsCompuestas_Retorna200() throws Exception {
        Inscripcion inscripcion = InscripcionBuilder.crear();
        when(inscripcionService.findById(any()))
                .thenReturn(Optional.of(inscripcion));
        doNothing().when(inscripcionService).delete(any());

        mockMvc.perform(delete("/api/inscripciones/" + InscripcionBuilder.PRODUCTO_ID + "/" + InscripcionBuilder.CLIENTE_ID))
                .andExpect(status().isOk());

        verify(inscripcionService, times(1)).delete(any());
    }
}
