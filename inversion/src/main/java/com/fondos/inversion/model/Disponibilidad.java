package com.fondos.inversion.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "disponibilidad")
public class Disponibilidad {
    @EmbeddedId
    private DisponibilidadId id;

    @ManyToOne
    @JoinColumn(name = "idSucursal", insertable = false, updatable = false)
    private Sucursal sucursal;

    @ManyToOne
    @JoinColumn(name = "idProducto", insertable = false, updatable = false)
    private Producto producto;

    public Disponibilidad() {}

    public DisponibilidadId getId() {
        return id;
    }

    public void setId(DisponibilidadId id) {
        this.id = id;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}