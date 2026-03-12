package com.fondos.inversion.controllers;

import com.fondos.inversion.DTO.ClienteDTO;
import com.fondos.inversion.builders.ClienteBuilder;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Cliente;
import com.fondos.inversion.services.ClienteService;
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
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetTodosClientes_Retorna200() throws Exception {
        Cliente cliente = ClienteBuilder.crear();
        when(clienteService.findAll()).thenReturn(Arrays.asList(cliente));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true));

        verify(clienteService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    void testGetClientePorId_Retorna200() throws Exception {
        Cliente cliente = ClienteBuilder.crear();
        when(clienteService.findById(ClienteBuilder.CLIENTE_ID))
                .thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/clientes/" + ClienteBuilder.CLIENTE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos.nombre").value(ClienteBuilder.NOMBRE));

        verify(clienteService, times(1)).findById(ClienteBuilder.CLIENTE_ID);
    }

    @Test
    @WithMockUser
    void testGetClientePorIdNoExiste_Retorna404() throws Exception {
        when(clienteService.findById(999))
                .thenThrow(new RecursoNoEncontradoException("Cliente no encontrado"));

        mockMvc.perform(get("/api/clientes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exito").value(false));
    }

    @Test
    @WithMockUser
    void testCrearClienteValido_Retorna201() throws Exception {
        Cliente cliente = ClienteBuilder.crearSinId();
        Cliente guardado = ClienteBuilder.crear();
        
        ClienteDTO dtoRequest = new ClienteDTO();
        dtoRequest.setNombre(ClienteBuilder.NOMBRE);
        dtoRequest.setApellidos(ClienteBuilder.APELLIDOS);
        dtoRequest.setCiudad(ClienteBuilder.CIUDAD);

        when(clienteService.save(any())).thenReturn(guardado);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.exito").value(true));

        verify(clienteService, times(1)).save(any());
    }

    @Test
    @WithMockUser
    void testCrearClienteNombreNulo_Retorna400() throws Exception {
        ClienteDTO dtoRequest = new ClienteDTO();
        dtoRequest.setNombre(null);
        dtoRequest.setApellidos(ClienteBuilder.APELLIDOS);
        dtoRequest.setCiudad(ClienteBuilder.CIUDAD);

        when(clienteService.save(any()))
                .thenThrow(new ValidacionException("Nombre requerido"));

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testUpdateCliente_Retorna200() throws Exception {
        Cliente clienteActualizado = ClienteBuilder.crear();
        clienteActualizado.setNombre("Cliente Actualizado");

        ClienteDTO dtoRequest = new ClienteDTO();
        dtoRequest.setNombre("Cliente Actualizado");
        dtoRequest.setApellidos(ClienteBuilder.APELLIDOS);
        dtoRequest.setCiudad(ClienteBuilder.CIUDAD);

        when(clienteService.findById(ClienteBuilder.CLIENTE_ID))
                .thenReturn(Optional.of(clienteActualizado));
        when(clienteService.update(any())).thenReturn(clienteActualizado);

        mockMvc.perform(put("/api/clientes/" + ClienteBuilder.CLIENTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true));
    }
}
