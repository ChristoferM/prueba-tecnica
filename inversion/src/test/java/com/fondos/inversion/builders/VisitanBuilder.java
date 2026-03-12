package com.fondos.inversion.builders;

import com.fondos.inversion.model.Visitan;
import com.fondos.inversion.model.VisitanId;
import com.fondos.inversion.model.Sucursal;
import com.fondos.inversion.model.Cliente;
import java.time.LocalDate;
import java.sql.Date;

public class VisitanBuilder {

    public static final int SUCURSAL_ID = 1;
    public static final int CLIENTE_ID = 1;
    public static final Date FECHA_VISITA = Date.valueOf(LocalDate.of(2026, 3, 12));

    public static Visitan crear() {
        Visitan visitan = new Visitan();
        VisitanId id = new VisitanId();
        id.setIdSucursal(SUCURSAL_ID);
        id.setIdCliente(CLIENTE_ID);
        id.setFechaVisita(FECHA_VISITA);
        visitan.setId(id);

        Sucursal sucursal = SucursalBuilder.crearConId(SUCURSAL_ID);
        Cliente cliente = ClienteBuilder.crearConId(CLIENTE_ID);
        visitan.setSucursal(sucursal);
        visitan.setCliente(cliente);

        return visitan;
    }

    public static Visitan crearConIds(int idSucursal, int idCliente, LocalDate fechaVisita) {
        Visitan visitan = new Visitan();
        VisitanId id = new VisitanId();
        id.setIdSucursal(idSucursal);
        id.setIdCliente(idCliente);
        id.setFechaVisita(Date.valueOf(fechaVisita));
        visitan.setId(id);

        Sucursal sucursal = SucursalBuilder.crearConId(idSucursal);
        Cliente cliente = ClienteBuilder.crearConId(idCliente);
        visitan.setSucursal(sucursal);
        visitan.setCliente(cliente);

        return visitan;
    }
}
