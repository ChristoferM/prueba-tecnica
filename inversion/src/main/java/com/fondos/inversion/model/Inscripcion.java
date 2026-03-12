package com.fondos.inversion.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inscripcion")
public class Inscripcion {
    @EmbeddedId
    private InscripcionId id;

    @ManyToOne
    @JoinColumn(name = "idProducto", insertable = false, updatable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "idCliente", insertable = false, updatable = false)
    private Cliente cliente;

    public Inscripcion() {}

    public Inscripcion(InscripcionId id) {
        this.id = id;
    }

    public InscripcionId getId() {
        return id;
    }

    public void setId(InscripcionId id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}