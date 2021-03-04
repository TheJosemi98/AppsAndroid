package com.example.listacompraapp.Listas;

import java.util.Date;

public class Articulo {

    private String nombre;
    private String categoria;
    private double precio;
    private Date fecha;
    private boolean tienePrecio;

    public Articulo(String nombre, String categoria, double precio, Date fecha) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.fecha = fecha;
        this.tienePrecio = true;
    }

    public Articulo(String nombre, String categoria, Date fecha) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.fecha = fecha;
        this.tienePrecio = false;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isTienePrecio() {
        return tienePrecio;
    }

    public void setTienePrecio(boolean tienePrecio) {
        this.tienePrecio = tienePrecio;
    }
}
