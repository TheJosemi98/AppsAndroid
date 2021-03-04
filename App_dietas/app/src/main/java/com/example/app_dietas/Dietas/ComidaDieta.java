package com.example.app_dietas.Dietas;

import android.media.Image;

public class ComidaDieta {

    private Alimento[][] comidasDelDia;
    private int[] num_alimentos;
    private int numMaxAlimentos;
    private Alimento[][][] equivalentes;
    private int[][] num_equivalentes;
    private int numMaxEquivalentes;



    public ComidaDieta( Alimento[][] alimentos_, int[] num_alimentos_, Alimento[][][] equivalentes_, int[][] num_equivalentes_){

        num_alimentos = new int[4];
        for(int i = 0; i < 4; i++)
            num_alimentos[i] = num_alimentos_[i];

        this.numMaxAlimentos();
        num_equivalentes = new int[4][numMaxAlimentos];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < num_alimentos[i]; j++)
                num_equivalentes[i][j] = num_equivalentes_[i][j];
        }
        this.numMaxEquivalentes();

        comidasDelDia = new Alimento[4][numMaxAlimentos];
        equivalentes = new Alimento[4][numMaxAlimentos][numMaxEquivalentes];
        this.setAlimentos(alimentos_);
        this.setEquivalentes(equivalentes_);

    }

    public int[] getNum_alimentos() {
        return num_alimentos;
    }

    public void setNum_alimentos(int[] num_alimentos) {
        this.num_alimentos = num_alimentos;
    }

    public int getNumMaxAlimentos() {
        return numMaxAlimentos;
    }

    public void setNumMaxAlimentos(int numMaxAlimentos) {
        this.numMaxAlimentos = numMaxAlimentos;
    }

    public Alimento[][] getComidasDelDia() {
        return comidasDelDia;
    }

    public void setComidasDelDia(Alimento[][] comidasDelDia) {
        this.comidasDelDia = comidasDelDia;
    }

    public Alimento[][][] getEquivalentes() {
        return equivalentes;
    }

    public int[][] getNum_equivalentes() {
        return num_equivalentes;
    }

    public int getNum_equivalentes_Alimento(int pos_comida, int pos_alimento){
        return num_equivalentes[pos_comida][pos_alimento];
    }


    public void setNum_equivalentes(int[][] num_equivalentes) {
        this.num_equivalentes = num_equivalentes;
    }

    public int getNumMaxEquivalentes() {
        return numMaxEquivalentes;
    }

    public void setNumMaxEquivalentes(int numMaxEquivalentes) {
        this.numMaxEquivalentes = numMaxEquivalentes;
    }

    public void numMaxAlimentos(){

        for(int i = 0; i < 4; i++){
            if(i==0)
                numMaxAlimentos = num_alimentos[i];
            else{
                if(numMaxAlimentos < num_alimentos[i])
                    numMaxAlimentos = num_alimentos[i];
            }
        }

    }

    public void numMaxEquivalentes(){

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < num_alimentos[i]; j++) {
                if (i == 0 && j == 0)
                    numMaxEquivalentes = num_equivalentes[i][j];
                else {
                    if (numMaxEquivalentes < num_equivalentes[i][j])
                        numMaxEquivalentes = num_equivalentes[i][j];
                }
            }
        }
    }

    public void setEquivalentes(Alimento[][][] equivalentes_){

        for(int i = 0; i < 4; i++){

            for(int j = 0; j < num_alimentos[i]; j++)
                for(int z = 0; z < num_equivalentes[i][j]; z++)
                equivalentes[i][j] = equivalentes_[i][j];

        }

    }

    public void setAlimentos(Alimento[][] alimentos_){

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < num_alimentos[i]; j++)
                comidasDelDia[i][j] = alimentos_[i][j];
        }

    }



}
