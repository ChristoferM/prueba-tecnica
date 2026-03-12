package com.fondos.inversion.builders;

import com.fondos.inversion.model.Sucursal;

public class SucursalBuilder {

    public static final int SUCURSAL_ID = 1;
    public static final String NOMBRE = "Sucursal Centro";
    public static final String CIUDAD = "Bogotá";

    public static Sucursal crear() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(SUCURSAL_ID);
        sucursal.setNombre(NOMBRE);
        sucursal.setCiudad(CIUDAD);
        return sucursal;
    }

    public static Sucursal crearSinId() {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(NOMBRE);
        sucursal.setCiudad(CIUDAD);
        return sucursal;
    }

    public static Sucursal crearConId(int id) {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(id);
        sucursal.setNombre(NOMBRE);
        sucursal.setCiudad(CIUDAD);
        return sucursal;
    }
}
