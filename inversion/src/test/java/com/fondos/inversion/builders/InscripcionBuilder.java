package com.fondos.inversion.builders;

import com.fondos.inversion.model.Inscripcion;
import com.fondos.inversion.model.InscripcionId;
import com.fondos.inversion.model.Producto;
import com.fondos.inversion.model.Cliente;

public class InscripcionBuilder {

    public static final int PRODUCTO_ID = 1;
    public static final int CLIENTE_ID = 1;

    public static Inscripcion crear() {
        Inscripcion inscripcion = new Inscripcion();
        InscripcionId id = new InscripcionId();
        id.setIdProducto(PRODUCTO_ID);
        id.setIdCliente(CLIENTE_ID);
        inscripcion.setId(id);

        Producto producto = ProductoBuilder.crearConId(PRODUCTO_ID);
        Cliente cliente = ClienteBuilder.crearConId(CLIENTE_ID);
        inscripcion.setProducto(producto);
        inscripcion.setCliente(cliente);

        return inscripcion;
    }

    public static Inscripcion crearConIds(int idProducto, int idCliente) {
        Inscripcion inscripcion = new Inscripcion();
        InscripcionId id = new InscripcionId();
        id.setIdProducto(idProducto);
        id.setIdCliente(idCliente);
        inscripcion.setId(id);

        Producto producto = ProductoBuilder.crearConId(idProducto);
        Cliente cliente = ClienteBuilder.crearConId(idCliente);
        inscripcion.setProducto(producto);
        inscripcion.setCliente(cliente);

        return inscripcion;
    }
}
