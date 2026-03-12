package com.fondos.inversion.util;

public class ApiResponse<T> {
    private int codigo;
    private String mensaje;
    private T datos;
    private boolean exito;

    public ApiResponse(int codigo, String mensaje, T datos, boolean exito) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.datos = datos;
        this.exito = exito;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public T getDatos() {
        return datos;
    }

    public void setDatos(T datos) {
        this.datos = datos;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }
}
