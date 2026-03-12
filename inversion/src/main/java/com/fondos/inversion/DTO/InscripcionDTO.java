package com.fondos.inversion.DTO;

import com.fondos.inversion.model.Inscripcion;
import com.fondos.inversion.model.InscripcionId;

public class InscripcionDTO {
    private InscripcionId id;
    private Integer idProducto;
    private Integer idCliente;
    private String nombreProducto;
    private String nombreCliente;

    public InscripcionDTO() {}

    public InscripcionDTO(Inscripcion inscripcion) {
        this.id = inscripcion.getId();
        if (inscripcion.getId() != null) {
            this.idProducto = inscripcion.getId().getIdProducto();
            this.idCliente = inscripcion.getId().getIdCliente();
        }
        this.nombreProducto = inscripcion.getProducto() != null ? inscripcion.getProducto().getNombre() : null;
        this.nombreCliente = inscripcion.getCliente() != null ? inscripcion.getCliente().getNombre() : null;
    }

    public Inscripcion toEntity() {
        Inscripcion inscripcion = new Inscripcion();
        InscripcionId inscripcionId = new InscripcionId();
        inscripcionId.setIdProducto(this.idProducto);
        inscripcionId.setIdCliente(this.idCliente);
        inscripcion.setId(inscripcionId);
        return inscripcion;
    }

    public InscripcionId getId() {
        return id;
    }

    public void setId(InscripcionId id) {
        this.id = id;
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

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
