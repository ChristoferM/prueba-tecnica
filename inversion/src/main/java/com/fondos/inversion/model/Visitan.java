package com.fondos.inversion.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "visitan")
public class Visitan {
    @EmbeddedId
    private VisitanId id;

    @ManyToOne
    @JoinColumn(name = "idSucursal", insertable = false, updatable = false)
    private Sucursal sucursal;

    @ManyToOne
    @JoinColumn(name = "idCliente", insertable = false, updatable = false)
    private Cliente cliente;

    public Visitan() {}

    public Visitan(VisitanId id) {
        this.id = id;
    }

    public VisitanId getId() {
        return id;
    }

    public void setId(VisitanId id) {
        this.id = id;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFechaVisita() {
        return id != null ? id.getFechaVisita() : null;
    }

    public void setFechaVisita(Date fechaVisita) {
        if (id == null) {
            id = new VisitanId();
        }
        id.setFechaVisita(fechaVisita);
    }
}