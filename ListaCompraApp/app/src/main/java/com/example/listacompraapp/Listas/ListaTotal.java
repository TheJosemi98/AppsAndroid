package com.example.listacompraapp.Listas;

import android.provider.CalendarContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListaTotal {

    private int lim_dias;
    private int num_dias;
    private ListaDeUnDia[] listaPorDia;
    private double gastoTotal;

    public ListaTotal(ListaDeUnDia[] listaAux, int num_dias){
        lim_dias = num_dias;
        listaPorDia = new ListaDeUnDia[lim_dias];

        for(int i = 0; i < num_dias; i++)
            listaPorDia[i] = listaAux[i];

        this.calcularGastoTotal();
    }

    public ListaTotal(){
        lim_dias = 1;
        listaPorDia = new ListaDeUnDia[lim_dias];
        Calendar calendario = Calendar.getInstance();
        listaPorDia[0] = new ListaDeUnDia(calendario.getTime());
        gastoTotal = 0.0;
        num_dias = 1;
    }

    public int getPosDia(Calendar calendarioDay){
        Calendar calendarioRegistro = Calendar.getInstance();

        for(int i = 0; i < num_dias; i++){
            calendarioRegistro.setTime(listaPorDia[i].getFecha_dia());
            if(calendarioRegistro.get(Calendar.DAY_OF_YEAR) == calendarioDay.get(Calendar.DAY_OF_YEAR) && calendarioRegistro.get(Calendar.YEAR) == calendarioDay.get(Calendar.YEAR))
                return i;
        }

        return  num_dias-1;

    }

    public List<ListaDeUnDia> getListaDias(){
        List<ListaDeUnDia> listaDeLosDias = new ArrayList<>();

        for(int i = 0; i < num_dias; i++)
            listaDeLosDias.add(listaPorDia[i]);

        return listaDeLosDias;

    }

    public void calcularGastoTotal(){
        double gastoFinal = 0.0;

        for(int i = 0; i < num_dias; i++){
            listaPorDia[i].calcularGasto();
            gastoFinal += listaPorDia[i].getGastoDia();
            Log.i("GastoDia",""+listaPorDia[i].getGastoDia());
        }

        gastoTotal = gastoFinal;
    }

    public double getGastoTotalDelMes(Calendar calendario){
        double gastoMes = 0.0;
        Calendar calendarioAux = Calendar.getInstance();

        for(int i = 0; i < num_dias; i++){
            calendarioAux.setTime(listaPorDia[i].getFecha_dia());
            if(calendario.get(Calendar.MONTH) == calendarioAux.get(Calendar.MONTH))
                gastoMes += listaPorDia[i].getGastoDia();
        }
        return gastoMes;
    }

    public List<String> getCategoriaMes(Calendar calendario){
        List<String> categorias = new ArrayList<>();
        String[] categoriasDia;
        Calendar calendarioAux = Calendar.getInstance();

        for(int i = 0; i < num_dias; i++){
            calendarioAux.setTime(listaPorDia[i].getFecha_dia());
            if(calendarioAux.get(Calendar.MONTH) == calendario.get(Calendar.MONTH)){
                categoriasDia = listaPorDia[i].getCategorias();
                for(int j = 0; j < categoriasDia.length; j++) {
                    if (!categorias.contains(categoriasDia[j]))
                        categorias.add(categoriasDia[j]);
                }
            }
        }

        return categorias;
    }

    public int getNumVeces(Calendar calendario, String categoria){
        int numVeces = 0;
        Calendar calendarioAux = Calendar.getInstance();

        for(int i = 0; i < num_dias; i++){
            calendarioAux.setTime(listaPorDia[i].getFecha_dia());
            if(calendarioAux.get(Calendar.MONTH) == calendario.get(Calendar.MONTH))
                numVeces += listaPorDia[i].getNumVecesCategoria(categoria);
        }
        return  numVeces;

    }


    public double[] getGastoDiarioDelMes(Calendar calendario){

        double[] gastoDiasMes = new double[31];
        Calendar calendarioAux = Calendar.getInstance();
        calendarioAux.setTime(calendario.getTime());
        calendarioAux.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calendarioDias = Calendar.getInstance();

        if((calendario.get(Calendar.MONTH)+1) == 2 && calendario.get(Calendar.YEAR)%4 == 0 )
            gastoDiasMes = new double[29];
        else if((calendario.get(Calendar.MONTH)+1) == 2 && calendario.get(Calendar.YEAR)%4 != 0)
            gastoDiasMes = new double[28];
        else if( (calendario.get(Calendar.MONTH)+1) == 1 || (calendario.get(Calendar.MONTH)+1) == 3 || (calendario.get(Calendar.MONTH)+1) == 5 || (calendario.get(Calendar.MONTH)+1) == 7 || (calendario.get(Calendar.MONTH)+1) == 8 || (calendario.get(Calendar.MONTH)+1) == 10 || (calendario.get(Calendar.MONTH)+1) == 12 )
            gastoDiasMes = new double[31];
        else if( (calendario.get(Calendar.MONTH)+1) == 4 || (calendario.get(Calendar.MONTH)+1) == 6 || (calendario.get(Calendar.MONTH)+1) == 9 || (calendario.get(Calendar.MONTH)+1) == 11)
            gastoDiasMes = new double[30];

        int Dias = 0;
        for(int i = 0; i < gastoDiasMes.length; i++){
            if(Dias < num_dias)
                calendarioDias.setTime(listaPorDia[Dias].getFecha_dia());

            if(calendarioAux.get(Calendar.MONTH) == calendarioDias.get(Calendar.MONTH) && calendarioAux.get(Calendar.DAY_OF_MONTH) == calendarioDias.get(Calendar.DAY_OF_MONTH)){
                gastoDiasMes[i] = listaPorDia[Dias].getGastoDia();
                Dias++;
            }
            else
                gastoDiasMes[i] = 0.0;

            calendarioAux.add(Calendar.DAY_OF_MONTH,1);

        }

        return gastoDiasMes;

    }

    public void setListaDia(int pos, ListaDeUnDia listaDia){
        listaPorDia[pos] = listaDia;
    }

    public ListaDeUnDia getListaDiaPos(int pos){
        return listaPorDia[pos];
    }

    public void addDia(ListaDeUnDia listaDia){

        if(num_dias >= lim_dias){
            ListaDeUnDia[] ListDiaAux = new ListaDeUnDia[2*lim_dias];
            lim_dias = 2*lim_dias;

            for(int i = 0; i < num_dias; i++)
                ListDiaAux[i] = listaPorDia[i];

            listaPorDia = ListDiaAux;
        }
        listaPorDia[num_dias] = listaDia;
        num_dias++;
        this.calcularGastoTotal();
    }

    public void removeDia(int pos){

        for(int i = pos; i < num_dias-1; i++)
            listaPorDia[i] = listaPorDia[i+1];

        num_dias--;
        this.calcularGastoTotal();
    }

    public ListaDeUnDia getDia(Calendar calendarioAux){
        ListaDeUnDia listaResultado;
        Calendar fechaDia = Calendar.getInstance();
        boolean encontrado = false;

        for(int i = 0; i < num_dias; i++){
            fechaDia.setTime(listaPorDia[i].getFecha_dia());
            if(fechaDia.get(Calendar.DAY_OF_YEAR) == calendarioAux.get(Calendar.DAY_OF_YEAR) && fechaDia.get(Calendar.YEAR) == calendarioAux.get(Calendar.YEAR)){
                listaResultado = listaPorDia[i];
                return listaResultado;
            }
        }

        listaResultado = new ListaDeUnDia(calendarioAux.getTime());
        return listaResultado;

    }

    public int getLim_dias() {
        return lim_dias;
    }

    public void setLim_dias(int lim_dias) {
        this.lim_dias = lim_dias;
    }

    public int getNum_dias() {
        return num_dias;
    }

    public void setNum_dias(int num_dias) {
        this.num_dias = num_dias;
    }

    public ListaDeUnDia[] getListaPorDia() {
        return listaPorDia;
    }

    public void setListaPorDia(ListaDeUnDia[] listaPorDia) {
        this.listaPorDia = listaPorDia;
    }

    public double getGastoTotal() {
        return gastoTotal;
    }

    public void setGastoTotal(double gastoTotal) {
        this.gastoTotal = gastoTotal;
    }
}
