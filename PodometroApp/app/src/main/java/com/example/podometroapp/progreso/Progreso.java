package com.example.podometroapp.progreso;

import android.util.Log;

import com.example.podometroapp.Registros.RegistrosCuartosDeHora;
import com.example.podometroapp.Registros.RegistrosMinutos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Progreso {

    //Registros
    private RegistrosCuartosDeHora[] registrosObtenidos;
    private int num_registros;
    private int lim_registros;

    //Metas
    private int pasosObjetivo;
    private int caloriasObjetivo;

    //Datos de la persona
    private double altura;
    private int edad;
    private double peso;
    private int sexo;

    public Progreso(){
        lim_registros = 100;
        registrosObtenidos = new RegistrosCuartosDeHora[lim_registros];
        num_registros = 0;
        peso = 70.0;
        edad = 22;
        sexo = 1;
        altura = 1.75;
    }

    public Progreso(RegistrosCuartosDeHora[] registros_, int num_registros_){
        registrosObtenidos = registros_;
        num_registros = num_registros_;
    }

    public Progreso(RegistrosCuartosDeHora[] registros_, int num_registros_, double altura_, int edad_, double peso_, int sexo_){
        registrosObtenidos = registros_;
        num_registros = num_registros_;
        altura = altura_;
        edad = edad_;
        peso = peso_;
        sexo = sexo_;
    }

    public RegistrosCuartosDeHora[] getRegistrosObtenidos() {
        return registrosObtenidos;
    }

    public void setRegistrosObtenidos(RegistrosCuartosDeHora[] registrosObtenidos) {
        this.registrosObtenidos = registrosObtenidos;
    }


    public List<RegistrosDia> getRegistrosDias(){
        Calendar calendarioDiaAux = Calendar.getInstance();
        Calendar calendarioDiaObservando = Calendar.getInstance();

        List<RegistrosDia> registrosFinal = new ArrayList<RegistrosDia>();
        RegistrosDia registrosFinalAux;

        Date fechaRegistro = Calendar.getInstance().getTime();

        double distanciaTotalRecorrida_ = 0.0;
        int tiempoEnMinutos_ = 0, nPasosDados_ = 0;
        double caloriasQuemadas_ = 0;
        boolean cumplidoPasos_ = false;
        boolean cumplidoCalorias_ = false;

        for(int i = 0; i < num_registros; i++){

            if(i == 0){
                calendarioDiaAux.setTime(registrosObtenidos[i].getFecha());
                fechaRegistro = registrosObtenidos[i].getFecha();
            }

            calendarioDiaObservando.setTime(registrosObtenidos[i].getFecha());
            //Log.i("fechaRegistro:",fechaRegistro.toString());
            //Log.i("fechaObservado:",registrosObtenidos[i].getFecha().toString());

            if(calendarioDiaAux.get(Calendar.DAY_OF_MONTH) == calendarioDiaObservando.get(Calendar.DAY_OF_MONTH) && calendarioDiaAux.get(Calendar.MONTH) == calendarioDiaObservando.get(Calendar.MONTH) && calendarioDiaAux.get(Calendar.YEAR) == calendarioDiaObservando.get(Calendar.YEAR)){
                distanciaTotalRecorrida_ += registrosObtenidos[i].getDistanciaRecorrida();
                tiempoEnMinutos_+=registrosObtenidos[i].getTiempoActivoMin();
                nPasosDados_ += registrosObtenidos[i].getPasosDados();
                caloriasQuemadas_ += registrosObtenidos[i].getCaloriasQuemadas();

            }
            else{

                Log.i("PasosObjetivo",""+pasosObjetivo);
                Log.i("Pasos",""+nPasosDados_);


                if(caloriasQuemadas_ > caloriasObjetivo)
                    cumplidoCalorias_ = true;
                else
                    cumplidoCalorias_ = false;

                if(nPasosDados_ > pasosObjetivo){
                    Log.i("Si?","SI, WE GOT IT");
                    cumplidoPasos_ = true;
                }
                else
                    cumplidoPasos_ = false;

                registrosFinalAux = new RegistrosDia(fechaRegistro,distanciaTotalRecorrida_,tiempoEnMinutos_,nPasosDados_,caloriasQuemadas_,cumplidoPasos_,cumplidoCalorias_);
                registrosFinal.add(registrosFinalAux);

                distanciaTotalRecorrida_ = registrosObtenidos[i].getDistanciaRecorrida();
                tiempoEnMinutos_ = registrosObtenidos[i].getTiempoActivoMin();
                nPasosDados_ = registrosObtenidos[i].getPasosDados();
                caloriasQuemadas_ = registrosObtenidos[i].getCaloriasQuemadas();

                calendarioDiaAux.setTime(registrosObtenidos[i].getFecha());
                fechaRegistro = registrosObtenidos[i].getFecha();
            }

        }

        Log.i("PasosObjetivo",""+pasosObjetivo);
        Log.i("Pasos",""+nPasosDados_);

        if(caloriasQuemadas_ > caloriasObjetivo)
            cumplidoCalorias_ = true;
        else
            cumplidoCalorias_ = false;


        if(nPasosDados_ > pasosObjetivo){
            cumplidoPasos_ = true;
            Log.i("Si?","WE GOT IT");
        }
        else
            cumplidoPasos_ = false;

        registrosFinalAux = new RegistrosDia(fechaRegistro,distanciaTotalRecorrida_,tiempoEnMinutos_,nPasosDados_,caloriasQuemadas_,cumplidoPasos_,cumplidoCalorias_);
        Log.i("Cumplimos?",""+cumplidoPasos_);
        Log.i("Cumplimos?Copia",""+registrosFinalAux.isCumplidoPasos());
        registrosFinal.add(registrosFinalAux);


        Log.i("RegistroDia",registrosFinal.size()+"");

        return registrosFinal;

    }

    public List<RegistrosDia> getRegistroDia(Calendar calendarioFecha){

        List<RegistrosCuartosDeHora> registrosDelDia = new ArrayList<RegistrosCuartosDeHora>();

        for(int i = 0; i < num_registros; i++){
            Calendar calendarioRegistro = Calendar.getInstance();
            calendarioRegistro.setTime(registrosObtenidos[i].getFecha());
            if(calendarioFecha.get(Calendar.DAY_OF_MONTH) == calendarioRegistro.get(Calendar.DAY_OF_MONTH) && calendarioFecha.get(Calendar.MONTH) == calendarioRegistro.get(Calendar.MONTH) && calendarioFecha.get(Calendar.YEAR) == calendarioRegistro.get(Calendar.YEAR)){
                registrosDelDia.add(registrosObtenidos[i]);
            }
        }

        Calendar calendarioLimSup = Calendar.getInstance();
        calendarioLimSup.setTime(calendarioFecha.getTime());
        calendarioLimSup.set(Calendar.HOUR_OF_DAY,0);
        calendarioLimSup.set(Calendar.MINUTE,15);

        Calendar calendarioLimInf = Calendar.getInstance();
        calendarioLimInf.setTime(calendarioFecha.getTime());
        calendarioLimInf.set(Calendar.HOUR_OF_DAY,0);
        calendarioLimInf.set(Calendar.MINUTE,0);

        List<RegistrosDia> registrosFinal = new ArrayList<RegistrosDia>();
        double distanciaTotalRecorrida_ = 0.0;
        int tiempoEnMinutos_ = 0, nPasosDados_ = 0;
        double caloriasQuemadas_ = 0;

        for(int i = 0; i < 95; i++){
            distanciaTotalRecorrida_ = 0.0;
            tiempoEnMinutos_ = 0;
            nPasosDados_ = 0;
            caloriasQuemadas_ = 0;

            for(int j = 0; j < registrosDelDia.size(); j++){
                Calendar calendarioAux_r = Calendar.getInstance();
                calendarioAux_r.setTime(registrosDelDia.get(j).getFecha());

                if(calendarioLimSup.compareTo(calendarioAux_r) >= 0 && calendarioLimInf.compareTo(calendarioAux_r) == -1){
                    distanciaTotalRecorrida_ += registrosDelDia.get(j).getDistanciaRecorrida();
                    tiempoEnMinutos_+= registrosDelDia.get(j).getTiempoActivoMin();
                    nPasosDados_ += registrosDelDia.get(j).getPasosDados();
                    caloriasQuemadas_ += registrosDelDia.get(j).getCaloriasQuemadas();
                }
            }

            RegistrosDia registrosAuxSum = new RegistrosDia(calendarioLimSup.getTime(),distanciaTotalRecorrida_,tiempoEnMinutos_,nPasosDados_,caloriasQuemadas_,false,false);
            registrosFinal.add(registrosAuxSum);

            calendarioLimInf.add(Calendar.MINUTE,15);
            calendarioLimSup.add(Calendar.MINUTE,15);
        }

        return registrosFinal;

    }

    public List<RegistrosDia> getRegistrosSemana(Calendar calendarioFecha){

        List<RegistrosDia> registrosTotalDia = this.getRegistrosDias();
        List<RegistrosDia> registrosSemana = new ArrayList<RegistrosDia>();
        int indSemanas = 1;
        Calendar calendarioRegistro = Calendar.getInstance();

        Log.i("CalendarioFecha",calendarioFecha.getTime().toString());

        boolean encontrado;

        do{
            encontrado = false;
            for(int i = 0; i < registrosTotalDia.size(); i++){
                calendarioRegistro.setTime(registrosTotalDia.get(i).getFecha());

                if(calendarioFecha.get(Calendar.DAY_OF_MONTH) == calendarioRegistro.get(Calendar.DAY_OF_MONTH) && calendarioFecha.get(Calendar.MONTH) == calendarioRegistro.get(Calendar.MONTH) && calendarioFecha.get(Calendar.YEAR) == calendarioRegistro.get(Calendar.YEAR)){
                    if(indSemanas < 7){
                        registrosSemana.add(registrosTotalDia.get(i));
                        encontrado = true;
                        Log.i("CalendarioFechaPlus",calendarioFecha.getTime().toString());
                    }
                }

            }

           if(!encontrado)
                registrosSemana.add(new RegistrosDia(calendarioFecha.getTime(), 0.0, 0, 0, 0, false, false));

            indSemanas++;
            calendarioFecha.add(Calendar.DAY_OF_MONTH,1);

        }while (indSemanas < 7);

        return registrosSemana;

    }

    public void setLastRegistro(RegistrosCuartosDeHora registro){
        registrosObtenidos[num_registros-1] = new RegistrosCuartosDeHora(registro.getPasosDados(),registro.getContadorMinutosEnMilis(),registro.getFecha(),registro.getEdad(),registro.getSexo(),registro.getPeso(),registro.getAltura());
    }

    public List<RegistrosDia> getRegistrosMes(Calendar calendarioFecha){

        List<RegistrosDia> registrosTotalDia = this.getRegistrosDias();
        List<RegistrosDia> registrosMes = new ArrayList<RegistrosDia>();
        Calendar calendarioRegistro = Calendar.getInstance();
        Calendar calendarioAux = Calendar.getInstance();
        calendarioAux.setTime(calendarioFecha.getTime());
        calendarioAux.set(Calendar.DAY_OF_MONTH,1);

        int indMes = 1;
        int LimMes;

        if(calendarioAux.get(Calendar.MONTH) == 1 && calendarioAux.get(Calendar.YEAR)%4 != 0)
            LimMes = 28;
        else if(calendarioAux.get(Calendar.MONTH) == 1 && calendarioAux.get(Calendar.YEAR)%4 == 0)
            LimMes = 29;
        else if(calendarioAux.get(Calendar.MONTH) == 3 || calendarioAux.get(Calendar.MONTH) == 5 || calendarioAux.get(Calendar.MONTH) == 8 || calendarioAux.get(Calendar.MONTH) == 10)
            LimMes = 30;
        else
            LimMes  = 31;

        boolean encontrado;

        do{
            encontrado = false;
            for(int i = 0; i < registrosTotalDia.size(); i++){
                calendarioRegistro.setTime(registrosTotalDia.get(i).getFecha());
                if(calendarioAux.get(Calendar.DAY_OF_MONTH) == calendarioRegistro.get(Calendar.DAY_OF_MONTH) && calendarioAux.get(Calendar.MONTH) == calendarioRegistro.get(Calendar.MONTH) && calendarioAux.get(Calendar.YEAR) == calendarioRegistro.get(Calendar.YEAR)){
                    registrosMes.add(registrosTotalDia.get(i));
                    encontrado = true;
                }
            }

            if(!encontrado)
                registrosMes.add(new RegistrosDia(calendarioFecha.getTime(), 0.0, 0, 0, 0, false, false));

            indMes++;
            calendarioAux.add(Calendar.DAY_OF_MONTH,1);

        }while(indMes < LimMes);

        return registrosMes;
    }


    public int getNum_registros() {
        return num_registros;
    }

    public void setNum_registros(int num_registros) {
        this.num_registros = num_registros;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public void addRegistro(RegistrosCuartosDeHora registros){

        if(num_registros >= lim_registros){
            lim_registros = 2*lim_registros;
            RegistrosCuartosDeHora[] registros_aux = new RegistrosCuartosDeHora[lim_registros];
            for(int i = 0; i < num_registros; i++)
                registros_aux[i] = registrosObtenidos[i];
            registrosObtenidos = registros_aux;
        }
        registrosObtenidos[num_registros] = registros;
        num_registros++;
    }

    public int getPasosObjetivo() {
        return pasosObjetivo;
    }

    public void setPasosObjetivo(int pasosObjetivo) {
        this.pasosObjetivo = pasosObjetivo;
    }

    public int getCaloriasObjetivo() {
        return caloriasObjetivo;
    }

    public void setCaloriasObjetivo(int caloriasObjetivo) {
        this.caloriasObjetivo = caloriasObjetivo;
    }

    public int getLim_registros() {
        return lim_registros;
    }

    public void setLim_registros(int lim_registros) {
        this.lim_registros = lim_registros;
    }
}
