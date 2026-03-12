package com.fondos.inversion.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Clase ID embeddida para la tabla visitan (PK compuesta)
 */
@Embeddable
public class VisitanId implements Serializable {
    @Column(name = "idSucursal")
    private Integer idSucursal;
    
    @Column(name = "idCliente")
    private Integer idCliente;
    
    @Column(name = "fechaVisita")
    private Date fechaVisita;

    public VisitanId() {}

    public VisitanId(Integer idSucursal, Integer idCliente, Date fechaVisita) {
        this.idSucursal = idSucursal;
        this.idCliente = idCliente;
        this.fechaVisita = fechaVisita;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Date getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(Date fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitanId visitanId = (VisitanId) o;
        return Objects.equals(idSucursal, visitanId.idSucursal) &&
               Objects.equals(idCliente, visitanId.idCliente) &&
               Objects.equals(fechaVisita, visitanId.fechaVisita);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSucursal, idCliente, fechaVisita);
    }
}
