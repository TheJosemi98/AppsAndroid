package com.example.app_dietas.Dietas;

import android.media.Image;

import java.util.Date;

public class ComidaProgreso {

    private Alimento[][] comidasDelDia;
    private int[] num_alimentos;
    private Date fecha;
    private boolean[][] confirmados;
    private boolean dia_libre;
    private int estado_confirmacion;
    private Alimento[][][] equivalentes;
    private int[][] num_equivalentes;
    private int numMaxEquivalentes;

    private Image[] ImagenComida;

    public ComidaProgreso(Date fecha_, Alimento[][] alimentos_, int[] num_alimentos_, boolean[][] confirmados_){
        fecha = fecha_;
        dia_libre = false;
        num_alimentos = new int[4];
        int num_max_alimentos = 0;

        for(int i = 0; i < 4 ; i++){
            if(i==0)
                num_max_alimentos = num_alimentos_[i];
            else{
                if(num_max_alimentos < num_alimentos_[i])
                    num_max_alimentos = num_alimentos_[i];
            }
            num_alimentos[i] = num_alimentos_[i];
        }

        comidasDelDia = new Alimento[4][num_max_alimentos];
        confirmados = new boolean[4][num_max_alimentos];
        estado_confirmacion = 0;

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < num_alimentos[i]; j++){
                comidasDelDia[i][j] = alimentos_[i][j];
                confirmados[i][j] = confirmados_[i][j];
            }
        }
        this.contarConfirmados();
    }


    public ComidaProgreso(Date fecha_, Alimento[][] alimentos_, int[] num_alimentos_, boolean[][] confirmados_, Alimento[][][] equivalentes_, int[][] num_equivalentes_){
        fecha = fecha_;
        dia_libre = false;
        num_alimentos = new int[4];
        int num_max_alimentos = 0;

        for(int i = 0; i < 4 ; i++){
            if(i==0)
                num_max_alimentos = num_alimentos_[i];
            else{
                if(num_max_alimentos < num_alimentos_[i])
                    num_max_alimentos = num_alimentos_[i];
            }
            num_alimentos[i] = num_alimentos_[i];
        }

        num_equivalentes = new int[4][num_max_alimentos];
        comidasDelDia = new Alimento[4][num_max_alimentos];
        confirmados = new boolean[4][num_max_alimentos];
        estado_confirmacion = 0;

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < num_alimentos[i]; j++){
                comidasDelDia[i][j] = alimentos_[i][j];
                confirmados[i][j] = confirmados_[i][j];
                num_equivalentes[i][j] = num_equivalentes_[i][j];
            }
        }
        this.numMaxEquivalentes();
        equivalentes = new Alimento[4][num_max_alimentos][numMaxEquivalentes];
        this.setEquivalentes(equivalentes_);

        this.contarConfirmados();
    }


    public Alimento[][] getComidasDelDia() {
        return comidasDelDia;
    }

    public void setComidasDelDia(Alimento[][] comidasDelDia) {
        this.comidasDelDia = comidasDelDia;
    }

    public int[] getNum_alimentos() {
        return num_alimentos;
    }

    public void setNum_alimentos(int[] num_alimentos) {
        this.num_alimentos = num_alimentos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean[][] getConfirmados() {
        return confirmados;
    }

    public void setConfirmados(boolean[][] confirmados) {
        this.confirmados = confirmados;
    }

    public int getEstado_confirmacion() {
        return estado_confirmacion;
    }

    public void setEstado_confirmacion(int estado_confirmacion) {
        this.estado_confirmacion = estado_confirmacion;
    }

    public Image[] getImagenComida() {
        return ImagenComida;
    }

    public void setImagenComida(Image[] imagenComida) {
        ImagenComida = imagenComida;
    }

    public boolean isDia_libre() {
        return dia_libre;
    }

    public void setDia_libre(boolean dia_libre) {
        this.dia_libre = dia_libre;
    }

    public void confirmarDesconfirmarComida(boolean estado, int comida, int pos){
        confirmados[comida][pos] = estado;
        this.contarConfirmados();
    }

    public void contarConfirmados(){
        int conteo = 0;
        boolean confirmar;

        for(int i = 0; i<4; i++){

            confirmar = true;

            for(int j = 0; j < num_alimentos[i]; j++){
                if(!confirmados[i][j])
                    confirmar = false;

            }

            if(confirmar)
                conteo++;

        }
        estado_confirmacion = conteo;
    }

    public int getNumComidasConfirmadas(){

        int resultado = 0;

        for(int i = 0; i<4; i++){
            for(int j = 0; j < num_alimentos[i]; j++){
                if(confirmados[i][j])
                    resultado++;
            }
        }

        return resultado;
    }

    public int getNumComidasTotal(){
        int total_comidas = 0;

        for(int i = 0; i < 4; i++)
            total_comidas+=num_alimentos[i];

        return total_comidas;
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

    public Alimento[][][] getEquivalentes() {
        return equivalentes;
    }

    public int[][] getNum_equivalentes() {
        return num_equivalentes;
    }

    public void setNum_equivalentes(int[][] num_equivalentes) {
        this.num_equivalentes = num_equivalentes;
    }

    public void setCambioEquivalentesComida(int indComida, int indAlimento, int indEquivalente){
        Alimento AlimentoAux;

        AlimentoAux = comidasDelDia[indComida][indAlimento];
        comidasDelDia[indComida][indAlimento] = equivalentes[indComida][indAlimento][indEquivalente];
        equivalentes[indComida][indAlimento][indEquivalente] = AlimentoAux;

    }


}
