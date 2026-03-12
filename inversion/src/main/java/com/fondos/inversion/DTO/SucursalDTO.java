package com.fondos.inversion.DTO;

import com.fondos.inversion.model.Sucursal;

public class SucursalDTO {
    private Integer id;
    private String nombre;
    private String ciudad;

    public SucursalDTO() {}

    public SucursalDTO(Integer id, String nombre, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    public SucursalDTO(Sucursal sucursal) {
        this.id = sucursal.getId();
        this.nombre = sucursal.getNombre();
        this.ciudad = sucursal.getCiudad();
    }

    public Sucursal toEntity() {
        Sucursal sucursal = new Sucursal();
        if (this.id != null) {
            sucursal.setId(this.id);
        }
        sucursal.setNombre(this.nombre);
        sucursal.setCiudad(this.ciudad);
        return sucursal;
    }

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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
