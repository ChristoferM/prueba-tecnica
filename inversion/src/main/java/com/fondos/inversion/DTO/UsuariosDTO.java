package com.fondos.inversion.DTO;

import com.fondos.inversion.model.Usuarios;

public class UsuariosDTO {
    private String correo;
    private String nombre;
    private String contrasena;
    private String role;
    private Boolean activo;

    public UsuariosDTO() {}

    public UsuariosDTO(String correo, String nombre, String contrasena, String role, Boolean activo) {
        this.correo = correo;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.role = role;
        this.activo = activo;
    }

    public UsuariosDTO(Usuarios usuarios) {
        this.correo = usuarios.getCorreo();
        this.nombre = usuarios.getNombre();
        this.contrasena = usuarios.getContrasena();
        this.role = usuarios.getRole();
        this.activo = usuarios.getActivo();
    }

    public Usuarios toEntity() {
        Usuarios usuarios = new Usuarios();
        usuarios.setCorreo(this.correo);
        usuarios.setNombre(this.nombre);
        usuarios.setContrasena(this.contrasena);
        usuarios.setRole(this.role);
        usuarios.setActivo(this.activo);
        return usuarios;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
