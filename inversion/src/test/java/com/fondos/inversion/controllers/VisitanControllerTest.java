package com.fondos.inversion.controllers;

import com.fondos.inversion.DTO.VisitanDTO;
import com.fondos.inversion.builders.VisitanBuilder;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Visitan;
import com.fondos.inversion.services.VisitanService;
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
class VisitanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitanService visitanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetTodasVisitas_Retorna200() throws Exception {
        Visitan visitan = VisitanBuilder.crear();
        when(visitanService.findAll()).thenReturn(Arrays.asList(visitan));

        mockMvc.perform(get("/api/visitas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true));

        verify(visitanService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    void testGetVisitaPor3PartesId_Retorna200() throws Exception {
        Visitan visitan = VisitanBuilder.crear();
        when(visitanService.findById(any()))
                .thenReturn(Optional.of(visitan));

        String fechaStr = VisitanBuilder.FECHA_VISITA.toString();
        mockMvc.perform(get("/api/visitas/" + VisitanBuilder.SUCURSAL_ID + "/" + VisitanBuilder.CLIENTE_ID + "/" + fechaStr))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true));

        verify(visitanService, times(1)).findById(any());
    }

    @Test
    @WithMockUser
    void testGetVisitaIdsNoExisten_Retorna404() throws Exception {
        when(visitanService.findById(any()))
                .thenThrow(new RecursoNoEncontradoException("Visita no encontrada"));

        mockMvc.perform(get("/api/visitas/999/999/2026-03-12"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testCrearVisitaValida_Retorna201() throws Exception {
        Visitan visitan = VisitanBuilder.crear();
        
        VisitanDTO dtoRequest = new VisitanDTO();
        dtoRequest.setIdSucursal(VisitanBuilder.SUCURSAL_ID);
        dtoRequest.setIdCliente(VisitanBuilder.CLIENTE_ID);
        dtoRequest.setFechaVisita(VisitanBuilder.FECHA_VISITA);

        when(visitanService.save(any())).thenReturn(visitan);

        mockMvc.perform(post("/api/visitas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isCreated());

        verify(visitanService, times(1)).save(any());
    }

    @Test
    @WithMockUser
    void testCrearVisitaSinFecha_Retorna400() throws Exception {
        VisitanDTO dtoRequest = new VisitanDTO();
        dtoRequest.setIdSucursal(VisitanBuilder.SUCURSAL_ID);
        dtoRequest.setIdCliente(VisitanBuilder.CLIENTE_ID);
        dtoRequest.setFechaVisita(null);

        when(visitanService.save(any()))
                .thenThrow(new ValidacionException("Fecha requerida"));

        mockMvc.perform(post("/api/visitas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testDeleteVisitaPor3PartesId_Retorna200() throws Exception {
        Visitan visita = VisitanBuilder.crear();
        when(visitanService.findById(any()))
                .thenReturn(Optional.of(visita));
        doNothing().when(visitanService).delete(any());

        String fechaStr = VisitanBuilder.FECHA_VISITA.toString();
        mockMvc.perform(delete("/api/visitas/" + VisitanBuilder.SUCURSAL_ID + "/" + VisitanBuilder.CLIENTE_ID + "/" + fechaStr))
                .andExpect(status().isOk());

        verify(visitanService, times(1)).delete(any());
    }
}
