package com.fondos.inversion.DTO;

import com.fondos.inversion.model.Disponibilidad;
import com.fondos.inversion.model.DisponibilidadId;

public class DisponibilidadDTO {
    private DisponibilidadId id;
    private Integer idSucursal;
    private Integer idProducto;
    private String nombreSucursal;
    private String nombreProducto;

    public DisponibilidadDTO() {}

    public DisponibilidadDTO(Disponibilidad disponibilidad) {
        this.id = disponibilidad.getId();
        if (disponibilidad.getId() != null) {
            this.idSucursal = disponibilidad.getId().getIdSucursal();
            this.idProducto = disponibilidad.getId().getIdProducto();
        }
        this.nombreSucursal = disponibilidad.getSucursal() != null ? disponibilidad.getSucursal().getNombre() : null;
        this.nombreProducto = disponibilidad.getProducto() != null ? disponibilidad.getProducto().getNombre() : null;
    }

    public Disponibilidad toEntity() {
        Disponibilidad disponibilidad = new Disponibilidad();
        DisponibilidadId disponibilidadId = new DisponibilidadId();
        disponibilidadId.setIdSucursal(this.idSucursal);
        disponibilidadId.setIdProducto(this.idProducto);
        disponibilidad.setId(disponibilidadId);
        return disponibilidad;
    }

    // Getters y Setters
    public DisponibilidadId getId() {
        return id;
    }

    public void setId(DisponibilidadId id) {
        this.id = id;
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

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
}
