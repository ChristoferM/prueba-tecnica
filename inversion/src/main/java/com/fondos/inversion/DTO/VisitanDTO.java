package com.fondos.inversion.DTO;

import com.fondos.inversion.model.Visitan;
import com.fondos.inversion.model.VisitanId;
import java.util.Date;

public class VisitanDTO {
    private VisitanId id;
    private Integer idSucursal;
    private Integer idCliente;
    private Date fechaVisita;
    private String nombreSucursal;
    private String nombreCliente;

    public VisitanDTO() {}

    public VisitanDTO(Visitan visitan) {
        this.id = visitan.getId();
        if (visitan.getId() != null) {
            this.idSucursal = visitan.getId().getIdSucursal();
            this.idCliente = visitan.getId().getIdCliente();
            this.fechaVisita = visitan.getId().getFechaVisita();
        }
        this.nombreSucursal = visitan.getSucursal() != null ? visitan.getSucursal().getNombre() : null;
        this.nombreCliente = visitan.getCliente() != null ? visitan.getCliente().getNombre() : null;
    }

    public Visitan toEntity() {
        Visitan visitan = new Visitan();
        VisitanId visitanId = new VisitanId();
        visitanId.setIdSucursal(this.idSucursal);
        visitanId.setIdCliente(this.idCliente);
        visitanId.setFechaVisita(this.fechaVisita);
        visitan.setId(visitanId);
        // Nota: La sucursal y cliente se asignan en el controlador/servicio
        return visitan;
    }

    // Getters y Setters
    public VisitanId getId() {
        return id;
    }

    public void setId(VisitanId id) {
        this.id = id;
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

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
