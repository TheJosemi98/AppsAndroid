package com.example.app_dietas.Dietas;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DietaUsuario {

    private DietaAct DietaActual;
    private DietaProgreso ProgresoDeDieta;
    private String Nombre;
    private String Apellidos;
    private String Recomendaciones;

    public DietaUsuario(DietaAct dietaActual, DietaProgreso progresoDeDieta, String nombre, String apellidos, String recomendaciones) {
        DietaActual = dietaActual;
        ProgresoDeDieta = progresoDeDieta;
        Nombre = nombre;
        Apellidos = apellidos;
        Recomendaciones = recomendaciones;

        this.organizarDietaProgreso();
    }

    public void organizarDietaProgreso(){

        int num_dias_dietas = DietaActual.getNum_dias_dietas();
        int num_dia_semana;
        ComidaDieta comidaDietaAux;
        ComidaProgreso comidaProgresoAux;
        Alimento[][] alimentosAux;
        int[] num_alimentos;
        boolean[][] confirmados_;
        Alimento[][][] equivalentes_;
        int[][] num_equivalentes_;
        Calendar fecha_hoy  = Calendar.getInstance();
        Calendar fecha_aux = Calendar.getInstance();

        ProgresoDeDieta.setNum_dias_libres(DietaActual.getNum_dias_libres());

        for(int i = 0; i < num_dias_dietas; i++) {
            num_dia_semana = this.getDiaInicial(fecha_hoy.get(Calendar.DAY_OF_WEEK));
            num_dia_semana = ((num_dia_semana + i) % 7)-1;
            if(num_dia_semana==-1)
                num_dia_semana=6;

            comidaDietaAux = DietaActual.getComidaPorDia(num_dia_semana);
            alimentosAux = comidaDietaAux.getComidasDelDia();
            num_alimentos = comidaDietaAux.getNum_alimentos();
            confirmados_ = new boolean[4][comidaDietaAux.getNumMaxAlimentos()];
            equivalentes_ = comidaDietaAux.getEquivalentes();
            num_equivalentes_ = comidaDietaAux.getNum_equivalentes();

            for (int j = 0; j < 4; j++) {
                for (int z = 0; z < num_alimentos[j]; z++)
                    confirmados_[j][z] = false;
            }
            Log.i("YearDietaUsuario",""+fecha_aux.get(Calendar.YEAR));
            comidaProgresoAux = new ComidaProgreso(fecha_aux.getTime(),alimentosAux,num_alimentos,confirmados_,equivalentes_,num_equivalentes_);
            ProgresoDeDieta.addDia(comidaProgresoAux);
            fecha_aux.add(Calendar.DAY_OF_YEAR,1);
        }

    }

    public int getDiaInicial(int ind){
        if(ind==1)
            return 7;
        else
            return ind-1;

    }

    public DietaAct getDietaActual() {
        return DietaActual;
    }

    public void setDietaActual(DietaAct dietaActual) {
        DietaActual = dietaActual;
    }

    public DietaProgreso getProgresoDeDieta() {
        return ProgresoDeDieta;
    }

    public void setProgresoDeDieta(DietaProgreso progresoDeDieta) {
        ProgresoDeDieta = progresoDeDieta;
        ProgresoDeDieta.getProgreso();
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getRecomendaciones() {
        return Recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        Recomendaciones = recomendaciones;
    }

    public List<Alimento> getListaAlimentos( String TipoComida, Date fechaSeleccionada){
        List<Alimento> ListaAlimento = new ArrayList<Alimento>();
        ComidaProgreso[] progresoDeDias = ProgresoDeDieta.getComidasPorDia();

        int ind_comida = this.getIndComida(TipoComida);

        int ind = 0;
        boolean fin_bucle = false;
        do{
            if(progresoDeDias[ind].getFecha().getDate() == fechaSeleccionada.getDate() && progresoDeDias[ind].getFecha().getMonth() == fechaSeleccionada.getMonth() && progresoDeDias[ind].getFecha().getYear() == fechaSeleccionada.getYear()){
                fin_bucle = true;
                int[] num_alimentos = progresoDeDias[ind].getNum_alimentos();
                Alimento[][] AlimentosDeComida = progresoDeDias[ind].getComidasDelDia();

                for(int j = 0; j < num_alimentos[ind_comida]; j++)
                    ListaAlimento.add(AlimentosDeComida[ind_comida][j]);

            }
            ind++;
        }while(fin_bucle==false && ind < ProgresoDeDieta.getNum_dias_dietas_llevados());



        return ListaAlimento;
    }

    public List<Alimento> getListaEquivalente(int Ind_Comida, int Ind_Alimento, Date fechaSeleccionada){

        List<Alimento> ListaEquivalentes = new ArrayList<Alimento>();
        ComidaProgreso[] progresoDeDias = ProgresoDeDieta.getComidasPorDia();
        Alimento[][][] ListaDeEquivaletesTotal = new Alimento[4][10][10];
        int[][] num_equivalentes = new int[4][10];

        int ind = 0;
        boolean fin_bucle = false;
        do {
            if(progresoDeDias[ind].getFecha().getDate() == fechaSeleccionada.getDate() && progresoDeDias[ind].getFecha().getMonth() == fechaSeleccionada.getMonth() && progresoDeDias[ind].getFecha().getYear() == fechaSeleccionada.getYear()){
                ComidaProgreso DietaReferencia = ProgresoDeDieta.getComidaPorDia(ind);
                ListaDeEquivaletesTotal = DietaReferencia.getEquivalentes();
                num_equivalentes = DietaReferencia.getNum_equivalentes();
                fin_bucle = true;
            }
            ind++;
        }while(fin_bucle==false && ind < ProgresoDeDieta.getNum_dias_dietas_llevados());

        for(int i = 0; i < num_equivalentes[Ind_Comida][Ind_Alimento]; i++)
            ListaEquivalentes.add(ListaDeEquivaletesTotal[Ind_Comida][Ind_Alimento][i]);

        return ListaEquivalentes;
    }



    public int getIndComida(String TipoComida){
        int ind_comida = 0;

        if(TipoComida.equals("Desayuno"))
            ind_comida = 0;
        else if(TipoComida.equals("Almuerzo"))
            ind_comida = 1;
        else if(TipoComida.equals("Merienda"))
            ind_comida = 2;
        else if(TipoComida.equals("Cena"))
            ind_comida = 3;

        return ind_comida;


    }

    public void cambiarEquivalente(int posComida, int posAlimento, int indEquivalente, Date fechaSeleccionada){

        int ind = 0;
        boolean fin_bucle = false;
        do {
            if(ProgresoDeDieta.getComidaPorDia(ind).getFecha().getDay() == fechaSeleccionada.getDay() && ProgresoDeDieta.getComidaPorDia(ind).getFecha().getMonth() == fechaSeleccionada.getMonth() && ProgresoDeDieta.getComidaPorDia(ind).getFecha().getYear() == fechaSeleccionada.getYear()){
                ProgresoDeDieta.getComidaPorDia(ind).setCambioEquivalentesComida(posComida,posAlimento,indEquivalente);
                fin_bucle = true;
            }
            ind++;
        }while(fin_bucle==false && ind < ProgresoDeDieta.getNum_dias_dietas_llevados());

    }

}
