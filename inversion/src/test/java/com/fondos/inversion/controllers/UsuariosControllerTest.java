package com.fondos.inversion.controllers;

import com.fondos.inversion.DTO.UsuariosDTO;
import com.fondos.inversion.builders.UsuariosBuilder;
import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.model.Usuarios;
import com.fondos.inversion.services.UsuariosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuariosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuariosService usuariosService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegistroUsuarioNuevo_Retorna201() throws Exception {
        // ARRANGE
        Usuarios usuarioNuevo = UsuariosBuilder.crear();
        UsuariosDTO dtoRequest = new UsuariosDTO();
        dtoRequest.setCorreo(UsuariosBuilder.CORREO_USER);
        dtoRequest.setNombre(UsuariosBuilder.NOMBRE);
        dtoRequest.setContrasena(UsuariosBuilder.CONTRASENA);
        dtoRequest.setRole("USUARIO");

        when(usuariosService.save(any())).thenReturn(usuarioNuevo);

        // ACT & ASSERT
        mockMvc.perform(post("/api/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.datos.correo").value(UsuariosBuilder.CORREO_USER));

        verify(usuariosService, times(1)).save(any());
    }

    @Test
    void testGetUsuarioPorCorreoSinAuth_Retorna401() throws Exception {
        mockMvc.perform(get("/api/usuarios/" + UsuariosBuilder.CORREO_ADMIN))
                .andExpect(status().isUnauthorized());

        // NO debe llamar al servicio
        verify(usuariosService, never()).findById(anyString());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void testGetUsuarioPorCorreoConAuth_Retorna200() throws Exception {
        Usuarios usuario = UsuariosBuilder.crearAdmin();
        when(usuariosService.findById(UsuariosBuilder.CORREO_ADMIN))
                .thenReturn(Optional.of(usuario));

        // ACT & ASSERT
        mockMvc.perform(get("/api/usuarios/" + UsuariosBuilder.CORREO_ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.datos.correo").value(UsuariosBuilder.CORREO_ADMIN));

        verify(usuariosService, times(1)).findById(UsuariosBuilder.CORREO_ADMIN);
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void testGetUsuarioPorCorreoNoExiste_Retorna404() throws Exception {
        // ARRANGE
        String correoNoExistente = "noexiste@test.com";
        when(usuariosService.findById(correoNoExistente))
                .thenThrow(new RecursoNoEncontradoException("Usuario no encontrado"));

        // ACT & ASSERT
        mockMvc.perform(get("/api/usuarios/" + correoNoExistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exito").value(false));
    }

    @Test
    void testRegistroCorreoDuplicado_Retorna400() throws Exception {
        // ARRANGE
        UsuariosDTO dtoRequest = new UsuariosDTO();
        dtoRequest.setCorreo(UsuariosBuilder.CORREO_ADMIN);
        dtoRequest.setNombre(UsuariosBuilder.NOMBRE);
        dtoRequest.setContrasena(UsuariosBuilder.CONTRASENA);

        when(usuariosService.save(any()))
                .thenThrow(new ValidacionException("Correo ya registrado"));

        // ACT & ASSERT
        mockMvc.perform(post("/api/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exito").value(false));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void testUpdateUsuario_Retorna200() throws Exception {
        // ARRANGE
        Usuarios usuarioActualizado = UsuariosBuilder.crearAdmin();
        usuarioActualizado.setNombre("Admin Actualizado");

        UsuariosDTO dtoRequest = new UsuariosDTO();
        dtoRequest.setCorreo(UsuariosBuilder.CORREO_ADMIN);
        dtoRequest.setNombre("Admin Actualizado");
        dtoRequest.setContrasena(UsuariosBuilder.CONTRASENA);

        when(usuariosService.findById(UsuariosBuilder.CORREO_ADMIN))
                .thenReturn(Optional.of(usuarioActualizado));
        when(usuariosService.update(any()))
                .thenReturn(usuarioActualizado);

        // ACT & ASSERT
        mockMvc.perform(put("/api/usuarios/" + UsuariosBuilder.CORREO_ADMIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.datos.nombre").value("Admin Actualizado"));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    void testDeleteUsuarioConRolUser_Retorna403() throws Exception {
        mockMvc.perform(delete("/api/usuarios/" + UsuariosBuilder.CORREO_ADMIN))
                .andExpect(status().isForbidden());

        // NO debe llamar al servicio
        verify(usuariosService, never()).deleteById(anyString());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void testDeleteUsuarioConRolAdmin_Retorna200() throws Exception {
        // ARRANGE
        Usuarios usuario = UsuariosBuilder.crearAdmin();
        when(usuariosService.findById(UsuariosBuilder.CORREO_ADMIN))
                .thenReturn(Optional.of(usuario));
        doNothing().when(usuariosService).delete(any());

        // ACT & ASSERT
        mockMvc.perform(delete("/api/usuarios/" + UsuariosBuilder.CORREO_ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true));

        verify(usuariosService, times(1)).delete(any());
    }
}
