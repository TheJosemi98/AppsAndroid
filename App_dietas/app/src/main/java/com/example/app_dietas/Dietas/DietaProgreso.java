package com.example.app_dietas.Dietas;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class DietaProgreso {

    private ComidaProgreso[] comidasPorDia;
    private int num_dias_libres;
    private int num_dias_dietas_llevados;
    private int num_dias_cumplidos_seguidos;
    private int num_dias_cumplidos_completos;
    private int num_dias_cumplidos_parcial;
    private int num_dias_no_cumplidos;
    private int num_dias_por_cumplir;
    private int num_dias_tomados_libres;

    public DietaProgreso(ComidaProgreso[] comidas_, int num_dias_) {
        num_dias_dietas_llevados = num_dias_;

        num_dias_cumplidos_seguidos = 0;

        //Progreso
        num_dias_cumplidos_completos = 0;
        num_dias_cumplidos_parcial = 0;
        num_dias_no_cumplidos = 0;
        num_dias_por_cumplir = 0;
        num_dias_tomados_libres = 0;

        comidasPorDia = new ComidaProgreso[num_dias_dietas_llevados];
        for(int i = 0; i < num_dias_dietas_llevados; i++)
            comidasPorDia[i] = comidas_[i];
        this.ordenarDieta();
        this.getProgreso();

    }

    public DietaProgreso(){
        num_dias_dietas_llevados = 0;
        num_dias_cumplidos_seguidos = 0;

        //Progreso
        num_dias_cumplidos_completos = 0;
        num_dias_cumplidos_parcial = 0;
        num_dias_no_cumplidos = 0;
        num_dias_por_cumplir = 0;
        num_dias_tomados_libres = 0;
    }

    public ComidaProgreso[] getComidasPorDia() {
        return comidasPorDia;
    }

    public ComidaProgreso getComidaPorDia(int pos){
        return comidasPorDia[pos];
    }

    public void setComidasPorDia(ComidaProgreso[] comidasPorDia, int num_dias) {
        num_dias_dietas_llevados = num_dias;
        this.comidasPorDia = new ComidaProgreso[num_dias_dietas_llevados];
        for(int i = 0; i< num_dias_dietas_llevados; i++)
            this.comidasPorDia[i] = comidasPorDia[i];
        this.ordenarDieta();
    }

    public void addDia(ComidaProgreso comida){
        num_dias_dietas_llevados++;
        ComidaProgreso[] comidaProgresoAuxes = new ComidaProgreso[num_dias_dietas_llevados];
        for(int i = 0; i < num_dias_dietas_llevados-1; i++)
            comidaProgresoAuxes[i] = comidasPorDia[i];

        comidaProgresoAuxes[num_dias_dietas_llevados-1] = comida;

        comidasPorDia = comidaProgresoAuxes;
        this.ordenarDieta();
        this.getProgreso();
    }

    public void ordenarDieta(){
        ComidaProgreso comidaProgresoAux;
        boolean cambio = false;

        for(int i = 0; i < num_dias_dietas_llevados; i++){
            for(int j = i; j < num_dias_dietas_llevados; j++){

                if(comidasPorDia[i].getFecha().compareTo(comidasPorDia[j].getFecha()) > 1)
                    cambio = true;

                if(cambio){
                    comidaProgresoAux = comidasPorDia[i];
                    comidasPorDia[i] = comidasPorDia[j];
                    comidasPorDia[j] = comidaProgresoAux;
                    cambio = false;
                }


            }
        }


    }

    public void setDiaLibre(Date fechaCogida,boolean estado){

        for(int i = 0; i < num_dias_dietas_llevados; i++){
            if(comidasPorDia[i].getFecha().compareTo(fechaCogida) == 0)
                comidasPorDia[i].setDia_libre(estado);
        }

        if(estado)
            num_dias_libres--;
        else
            num_dias_libres++;

    }

    public void getProgreso(){

        num_dias_cumplidos_completos = 0;
        num_dias_cumplidos_parcial = 0;
        num_dias_no_cumplidos = 0;
        num_dias_por_cumplir = 0;
        num_dias_tomados_libres = 0;
        num_dias_cumplidos_seguidos = 0;

        Date fecha_actual = Calendar.getInstance().getTime();

        boolean fecha_pasada;

        for(int i = 0; i < num_dias_dietas_llevados; i++){
            fecha_pasada = false;


            if(comidasPorDia[i].getFecha().compareTo(fecha_actual) > 0)
                fecha_pasada = true;


            if(fecha_pasada)
                num_dias_por_cumplir++;
            else{
                if(comidasPorDia[i].isDia_libre())
                    num_dias_tomados_libres++;
                else {
                    if (comidasPorDia[i].getEstado_confirmacion() == 4){
                        num_dias_cumplidos_completos++;
                        num_dias_cumplidos_seguidos++;
                    }
                    else if (comidasPorDia[i].getEstado_confirmacion() < 4 && comidasPorDia[i].getEstado_confirmacion() > 0){
                        num_dias_cumplidos_parcial++;
                        num_dias_cumplidos_seguidos = 0;
                    }
                    else{
                        num_dias_no_cumplidos++;
                        num_dias_cumplidos_seguidos = 0;
                    }

                }
            }
        }
    }


    public int getNum_dias_libres() {
        return num_dias_libres;
    }

    public void setNum_dias_libres(int num_dias_libres) {
        this.num_dias_libres = num_dias_libres;
    }

    public int getNum_dias_dietas_llevados() {
        return num_dias_dietas_llevados;
    }

    public void setNum_dias_dietas_llevados(int num_dias_dietas) {
        this.num_dias_dietas_llevados = num_dias_dietas;
    }

    public int getNum_dias_cumplidos_seguidos() {
        return num_dias_cumplidos_seguidos;
    }

    public void setNum_dias_cumplidos_seguidos(int num_dias_cumplidos_seguidos) {
        this.num_dias_cumplidos_seguidos = num_dias_cumplidos_seguidos;
    }

    public void setComidasPorDia(ComidaProgreso[] comidasPorDia) {
        this.comidasPorDia = comidasPorDia;
    }

    public int getNum_dias_cumplidos_completos() {
        return num_dias_cumplidos_completos;
    }

    public void setNum_dias_cumplidos_completos(int num_dias_cumplidos_completos) {
        this.num_dias_cumplidos_completos = num_dias_cumplidos_completos;
    }

    public int getNum_dias_cumplidos_parcial() {
        return num_dias_cumplidos_parcial;
    }

    public void setNum_dias_cumplidos_parcial(int num_dias_cumplidos_parcial) {
        this.num_dias_cumplidos_parcial = num_dias_cumplidos_parcial;
    }

    public int getNum_dias_no_cumplidos() {
        return num_dias_no_cumplidos;
    }

    public void setNum_dias_no_cumplidos(int num_dias_no_cumplidos) {
        this.num_dias_no_cumplidos = num_dias_no_cumplidos;
    }

    public int getNum_dias_por_cumplir() {
        return num_dias_por_cumplir;
    }

    public void setNum_dias_por_cumplir(int num_dias_por_cumplir) {
        this.num_dias_por_cumplir = num_dias_por_cumplir;
    }

    public int getNum_dias_tomados_libres() {
        return num_dias_tomados_libres;
    }

    public void setNum_dias_tomados_libres(int num_dias_tomados_libres) {
        this.num_dias_tomados_libres = num_dias_tomados_libres;
    }

}
