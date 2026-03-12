package com.fondos.inversion.DTO;

import com.fondos.inversion.model.Producto;

public class ProductoDTO {
    private Integer id;
    private String nombre;
    private String tipoProducto;

    public ProductoDTO() {}

    public ProductoDTO(Integer id, String nombre, String tipoProducto) {
        this.id = id;
        this.nombre = nombre;
        this.tipoProducto = tipoProducto;
    }

    public ProductoDTO(Producto producto) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.tipoProducto = producto.getTipoProducto();
    }

    public Producto toEntity() {
        Producto producto = new Producto();
        if (this.id != null) {
            producto.setId(this.id);
        }
        producto.setNombre(this.nombre);
        producto.setTipoProducto(this.tipoProducto);
        return producto;
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

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
}
