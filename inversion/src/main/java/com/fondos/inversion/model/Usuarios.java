package com.fondos.inversion.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "usuarios")
public class Usuarios {
    @Id
    private String correo;
    private String nombre;
    private String contrasena;
    private String role;
    private Boolean activo;

    public Usuarios() {}

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

    /**
     * Valida si la contraseña en texto plano coincide con el hash BCrypt almacenado
     */
    public boolean validarContrasena(String contrasenaPlano, BCryptPasswordEncoder encoder) {
        return encoder.matches(contrasenaPlano, this.contrasena);
    }
}