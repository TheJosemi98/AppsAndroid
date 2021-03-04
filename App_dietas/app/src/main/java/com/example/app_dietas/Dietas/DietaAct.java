package com.example.app_dietas.Dietas;

import java.util.Date;
import java.io.Serializable;

public class DietaAct {

    private ComidaDieta[] comidasPorDia;
    private int num_dias_dietas;
    private int num_dias_libres;

    public DietaAct(ComidaDieta[] comidas_, int num_dias_, int num_libres) {
        num_dias_dietas = num_dias_;
        num_dias_libres = num_libres;

        comidasPorDia = new ComidaDieta[num_dias_dietas];
        for(int i = 0; i < 7; i++)
            comidasPorDia[i] = comidas_[i];

    }

    public ComidaDieta getComidaPorDia(int pos){ return comidasPorDia[pos]; }

    public ComidaDieta[] getComidasPorDia() {
        return comidasPorDia;
    }

    public void setComidasPorDia(ComidaDieta[] comidasPorDia, int num_dias) {
        num_dias_dietas = num_dias;
        this.comidasPorDia = new ComidaDieta[num_dias_dietas];
        for(int i = 0; i< num_dias_dietas; i++)
            this.comidasPorDia[i] = comidasPorDia[i];
    }

    public void addDia(ComidaDieta comida){
        num_dias_dietas++;
        ComidaDieta[] comidaDietaAuxes = new ComidaDieta[num_dias_dietas];
        for(int i = 0; i < num_dias_dietas-1; i++)
            comidaDietaAuxes[i] = comidasPorDia[i];

        comidaDietaAuxes[num_dias_dietas-1] = comida;

        comidasPorDia = comidaDietaAuxes;
    }


    public int getNum_dias_libres() {
        return num_dias_libres;
    }

    public void setNum_dias_libres(int num_dias_libres) {
        this.num_dias_libres = num_dias_libres;
    }

    public int getNum_dias_dietas() {
        return num_dias_dietas;
    }

    public void setNum_dias_dietas(int num_dias_dietas) {
        this.num_dias_dietas = num_dias_dietas;
    }

}
