package com.fondos.inversion.builders;

import com.fondos.inversion.model.Cliente;

public class ClienteBuilder {

    public static final int CLIENTE_ID = 1;
    public static final String NOMBRE = "Juan Pérez";
    public static final String APELLIDOS = "García López";
    public static final String CIUDAD = "Bogotá";

    public static Cliente crear() {
        Cliente cliente = new Cliente();
        cliente.setId(CLIENTE_ID);
        cliente.setNombre(NOMBRE);
        cliente.setApellidos(APELLIDOS);
        cliente.setCiudad(CIUDAD);
        return cliente;
    }

    public static Cliente crearSinId() {
        Cliente cliente = new Cliente();
        cliente.setNombre(NOMBRE);
        cliente.setApellidos(APELLIDOS);
        cliente.setCiudad(CIUDAD);
        return cliente;
    }

    public static Cliente crearConId(int id) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre(NOMBRE);
        cliente.setApellidos(APELLIDOS);
        cliente.setCiudad(CIUDAD);
        return cliente;
    }
}
