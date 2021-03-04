package com.example.podometroapp.progreso;

import java.util.Date;

public class RegistrosDia {

    private Date fecha;
    private double distanciaTotalRecorrida;
    private int tiempoEnMinutos;
    private int nPasosDados;
    private double caloriasQuemadas;
    private boolean cumplidoPasos;
    private boolean cumplidoCalorias;

    public RegistrosDia(Date fecha, double distanciaTotalRecorrida, int tiempoEnMinutos, int nPasosDados, double caloriasQuemadas, boolean cumplidoPasos, boolean cumplidoCalorias) {
        this.fecha = fecha;
        this.distanciaTotalRecorrida = distanciaTotalRecorrida;
        this.tiempoEnMinutos = tiempoEnMinutos;
        this.nPasosDados = nPasosDados;
        this.caloriasQuemadas = caloriasQuemadas;
        this.cumplidoPasos = cumplidoPasos;
        this.cumplidoCalorias = cumplidoCalorias;
    }



    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getDistanciaTotalRecorrida() {
        return distanciaTotalRecorrida;
    }

    public void setDistanciaTotalRecorrida(double distanciaTotalRecorrida) {
        this.distanciaTotalRecorrida = distanciaTotalRecorrida;
    }

    public int getTiempoEnMinutos() {
        return tiempoEnMinutos;
    }

    public void setTiempoEnMinutos(int tiempoEnMinutos) {
        this.tiempoEnMinutos = tiempoEnMinutos;
    }

    public int getnPasosDados() {
        return nPasosDados;
    }

    public void setnPasosDados(int nPasosDados) {
        this.nPasosDados = nPasosDados;
    }

    public double getCaloriasQuemadas() {
        return caloriasQuemadas;
    }

    public void setCaloriasQuemadas(double caloriasQuemadas) {
        this.caloriasQuemadas = caloriasQuemadas;
    }

    public boolean isCumplidoCalorias() {
        return cumplidoCalorias;
    }

    public void setCumplidoCalorias(boolean cumplido) {
        this.cumplidoCalorias = cumplido;
    }

    public boolean isCumplidoPasos() {
        return cumplidoPasos;
    }

    public void setCumplidoPasos(boolean cumplido) {
        this.cumplidoCalorias = cumplido;
    }

}
