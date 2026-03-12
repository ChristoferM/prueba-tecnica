package com.fondos.inversion.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clase ID embeddida para la tabla inscripcion (PK compuesta)
 */
@Embeddable
public class InscripcionId implements Serializable {
    @Column(name = "idProducto")
    private Integer idProducto;
    
    @Column(name = "idCliente")
    private Integer idCliente;

    public InscripcionId() {}

    public InscripcionId(Integer idProducto, Integer idCliente) {
        this.idProducto = idProducto;
        this.idCliente = idCliente;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InscripcionId that = (InscripcionId) o;
        return Objects.equals(idProducto, that.idProducto) &&
               Objects.equals(idCliente, that.idCliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, idCliente);
    }
}
