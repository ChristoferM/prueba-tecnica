package com.fondos.inversion.DTO;

import com.fondos.inversion.model.Cliente;

public class ClienteDTO {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String ciudad;

    // Constructores
    public ClienteDTO() {}

    public ClienteDTO(Integer id, String nombre, String apellidos, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.ciudad = ciudad;
    }

    public ClienteDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nombre = cliente.getNombre();
        this.apellidos = cliente.getApellidos();
        this.ciudad = cliente.getCiudad();
    }

    public Cliente toEntity() {
        Cliente cliente = new Cliente();
        if (this.id != null) {
            cliente.setId(this.id);
        }
        cliente.setNombre(this.nombre);
        cliente.setApellidos(this.apellidos);
        cliente.setCiudad(this.ciudad);
        return cliente;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
