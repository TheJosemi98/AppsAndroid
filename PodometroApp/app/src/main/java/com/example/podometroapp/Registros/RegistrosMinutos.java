package com.example.podometroapp.Registros;

public class RegistrosMinutos {

    private int pasosDados;
    private double distanciaRecorrida;
    private long contadorSegundosEnMilis;
    private double distanciaPorZancadaMetros;
    private boolean isCompleto;

    public RegistrosMinutos(int pasosDados, long contadorSegundos, double altura){
        this.pasosDados = pasosDados;
        this.contadorSegundosEnMilis = contadorSegundos;
        distanciaPorZancadaMetros = altura * 0.415;
        isCompleto = false;
        this.calcularDistancia();
    }

    public void addPasos(int pasosNuevos, long contadorMasSegundos){
        if(!isCompleto){
            pasosDados += pasosNuevos;
            contadorSegundosEnMilis += contadorMasSegundos;
            this.calcularDistancia();
        }

    }

    public void calcularDistancia(){
        if(contadorSegundosEnMilis==60000000){
            double distanciaRecorridaM = distanciaPorZancadaMetros*pasosDados;
            distanciaRecorrida = distanciaRecorridaM/1000.0;
            isCompleto = true;
        }
    }

    public int getPasosDados() {
        return pasosDados;
    }

    public void setPasosDados(int pasosDados) {
        this.pasosDados = pasosDados;
    }

    public double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(double distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

    public long getContadorSegundos() {
        return contadorSegundosEnMilis/1000000;
    }

    public void setContadorSegundos(long contadorSegundos) {
        this.contadorSegundosEnMilis = contadorSegundos;
    }

    public double getDistanciaPorZancadaMetros() {
        return distanciaPorZancadaMetros;
    }

    public void setDistanciaPorZancadaMetros(double distanciaPorZancada) {
        this.distanciaPorZancadaMetros = distanciaPorZancada;
    }

    public boolean isCompleto() {
        return isCompleto;
    }

    public void setCompleto(boolean completo) {
        isCompleto = completo;
    }
}
