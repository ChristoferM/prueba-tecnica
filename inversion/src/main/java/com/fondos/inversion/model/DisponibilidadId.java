package com.fondos.inversion.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clase ID embeddida para la tabla disponibilidad (PK compuesta)
 */
@Embeddable
public class DisponibilidadId implements Serializable {
    @Column(name = "idSucursal")
    private Integer idSucursal;
    
    @Column(name = "idProducto")
    private Integer idProducto;

    public DisponibilidadId() {}

    public DisponibilidadId(Integer idSucursal, Integer idProducto) {
        this.idSucursal = idSucursal;
        this.idProducto = idProducto;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisponibilidadId that = (DisponibilidadId) o;
        return Objects.equals(idSucursal, that.idSucursal) &&
               Objects.equals(idProducto, that.idProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSucursal, idProducto);
    }
}
