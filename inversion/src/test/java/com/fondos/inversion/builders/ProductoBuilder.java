package com.fondos.inversion.builders;

import com.fondos.inversion.model.Producto;

public class ProductoBuilder {

    public static final int PRODUCTO_ID = 1;
    public static final String NOMBRE = "Fondo ABC";
    public static final String TIPO_PRODUCTO = "Renta Fija";

    public static Producto crear() {
        Producto producto = new Producto();
        producto.setId(PRODUCTO_ID);
        producto.setNombre(NOMBRE);
        producto.setTipoProducto(TIPO_PRODUCTO);
        return producto;
    }

    public static Producto crearSinId() {
        Producto producto = new Producto();
        producto.setNombre(NOMBRE);
        producto.setTipoProducto(TIPO_PRODUCTO);
        return producto;
    }

    public static Producto crearConId(int id) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(NOMBRE);
        producto.setTipoProducto(TIPO_PRODUCTO);
        return producto;
    }
}
