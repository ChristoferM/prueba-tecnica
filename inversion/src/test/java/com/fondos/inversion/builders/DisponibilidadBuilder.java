package com.fondos.inversion.builders;

import com.fondos.inversion.model.Disponibilidad;
import com.fondos.inversion.model.DisponibilidadId;
import com.fondos.inversion.model.Sucursal;
import com.fondos.inversion.model.Producto;

public class DisponibilidadBuilder {

    public static final int SUCURSAL_ID = 1;
    public static final int PRODUCTO_ID = 1;

    public static Disponibilidad crear() {
        Disponibilidad disponibilidad = new Disponibilidad();
        DisponibilidadId id = new DisponibilidadId();
        id.setIdSucursal(SUCURSAL_ID);
        id.setIdProducto(PRODUCTO_ID);
        disponibilidad.setId(id);

        Sucursal sucursal = SucursalBuilder.crearConId(SUCURSAL_ID);
        Producto producto = ProductoBuilder.crearConId(PRODUCTO_ID);
        disponibilidad.setSucursal(sucursal);
        disponibilidad.setProducto(producto);

        return disponibilidad;
    }

    public static Disponibilidad crearConIds(int idSucursal, int idProducto) {
        Disponibilidad disponibilidad = new Disponibilidad();
        DisponibilidadId id = new DisponibilidadId();
        id.setIdSucursal(idSucursal);
        id.setIdProducto(idProducto);
        disponibilidad.setId(id);

        Sucursal sucursal = SucursalBuilder.crearConId(idSucursal);
        Producto producto = ProductoBuilder.crearConId(idProducto);
        disponibilidad.setSucursal(sucursal);
        disponibilidad.setProducto(producto);

        return disponibilidad;
    }
}
