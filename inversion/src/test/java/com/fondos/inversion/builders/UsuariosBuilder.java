package com.fondos.inversion.builders;

import com.fondos.inversion.model.Usuarios;

public class UsuariosBuilder {

    public static final String CORREO_ADMIN = "admin@test.com";
    public static final String CORREO_USER = "user@test.com";
    public static final String NOMBRE = "Test User";
    public static final String CONTRASENA = "Password123";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    public static Usuarios crearAdmin() {
        Usuarios usuario = new Usuarios();
        usuario.setCorreo(CORREO_ADMIN);
        usuario.setNombre("Admin");
        usuario.setContrasena(CONTRASENA);
        usuario.setRole(ROLE_ADMIN);
        usuario.setActivo(true);
        return usuario;
    }

    public static Usuarios crear() {
        Usuarios usuario = new Usuarios();
        usuario.setCorreo(CORREO_USER);
        usuario.setNombre(NOMBRE);
        usuario.setContrasena(CONTRASENA);
        usuario.setRole(ROLE_USER);
        usuario.setActivo(true);
        return usuario;
    }

    public static Usuarios crearConCorreo(String correo) {
        Usuarios usuario = new Usuarios();
        usuario.setCorreo(correo);
        usuario.setNombre(NOMBRE);
        usuario.setContrasena(CONTRASENA);
        usuario.setRole(ROLE_USER);
        usuario.setActivo(true);
        return usuario;
    }

    public static Usuarios crearInactivo() {
        Usuarios usuario = crear();
        usuario.setActivo(false);
        return usuario;
    }
}

