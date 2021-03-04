package com.example.podometroapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.example.podometroapp.Registros.RegistrosCuartosDeHora;
import com.example.podometroapp.Registros.RegistrosMinutos;
import com.example.podometroapp.progreso.Progreso;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

public class ContadorPasosService extends Service implements SensorEventListener {
    private static final String DEBUG_TAG = "ContadorPasos";

    private Progreso progreso;
    private RegistrosCuartosDeHora registroCuarto;
    private Calendar CalendarCuartos;

    private long tiempoEventoAnterior;
    private boolean actividadCreada;

    private int mSteps = 0;
    private int mCounterSteps = 0;

    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        this.setProgreso();

        CalendarCuartos = Calendar.getInstance();

        final boolean batchMode = sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL, 0);

        if (!batchMode) {
            Log.w("StepSensorSample", "Could not register sensor listener in batch mode, " +
                    "falling back to continuous mode.");
        }

        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent itent){
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        new SensorEventLogTask().execute(event);
        stopSelf();
    }

    private class SensorEventLogTask extends AsyncTask<SensorEvent, Void, Void>{

        @Override
        protected Void doInBackground(SensorEvent... events) {
            SensorEvent event = events[0];
            this.logEventos(event);
            return null;
        }

        public void guardarRegistros(){
            Gson gson = new Gson();
            String progresoRegistroJSON = gson.toJson(progreso);
            Log.i("progresoJSON",progresoRegistroJSON);
            SharedPreferences sharedPreferences = getSharedPreferences("ProgresoYRegistros", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Progreso",progresoRegistroJSON);
            editor.commit();

        }

        public void logEventos(SensorEvent event){

            if(actividadCreada){
                mCounterSteps = 0;
                tiempoEventoAnterior = (event.timestamp / 1000000L);
                actividadCreada = false;
                registroCuarto = new RegistrosCuartosDeHora(CalendarCuartos.getTime(),progreso.getEdad(),progreso.getSexo(),progreso.getPeso(),progreso.getAltura());
                progreso.addRegistro(registroCuarto);
            }

            //Calculo del tiempo transcurrido
            long tiempoRegistroMilis = (event.timestamp / 1000000L);
            long difDelayMilis = tiempoRegistroMilis - tiempoEventoAnterior;
            tiempoEventoAnterior = tiempoRegistroMilis;

            //Calculo de los pasos
            if (mCounterSteps < 1) {
                // initial value
                mCounterSteps = (int) event.values[0];
            }

            mSteps = (int) event.values[0] - mCounterSteps;
            Log.i("Numero Pasos:",""+mSteps);
            Log.i("Tiempo:",""+difDelayMilis);

            if(registroCuarto.isCompleto()){
                progreso.setLastRegistro(registroCuarto);
                CalendarCuartos = Calendar.getInstance();
                registroCuarto = new RegistrosCuartosDeHora(CalendarCuartos.getTime(),progreso.getEdad(),progreso.getSexo(),progreso.getPeso(),progreso.getAltura());
                progreso.addRegistro(registroCuarto);
                this.guardarRegistros();
                mCounterSteps = 0;
                mSteps = 0;
            }
            else{
                registroCuarto.addPasos(mSteps,difDelayMilis);
                progreso.setLastRegistro(registroCuarto);
                this.guardarRegistros();
            }

            Log.i("StepSensorSample", "New step detected by STEP_COUNTER sensor. Total step count: " + mSteps);
        }

    }



    public void setProgreso(){
        SharedPreferences sharedPreferences = getSharedPreferences("ProgresoYRegistros", Context.MODE_PRIVATE);
        String progresoRegistrosJSON = sharedPreferences.getString("Progreso","");
        Gson gson = new Gson();
        progreso = gson.fromJson(progresoRegistrosJSON, Progreso.class);
    }

    /*
    public void guardarRegistros(){
        Gson gson = new Gson();
        String progresoRegistroJSON = gson.toJson(progreso);
        Log.i("progresoJSON",progresoRegistroJSON);
        SharedPreferences sharedPreferences = getSharedPreferences("ProgresoYRegistros", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Progreso",progresoRegistroJSON);
        editor.commit();

    }

    public void logEventos(SensorEvent event){

        if(actividadCreada){
            mCounterSteps = 0;
            tiempoEventoAnterior = 0;
            actividadCreada = false;
            for(int ind = 0; ind < minutosCuarto.length; ind++){
                minutosCuarto[ind] = new RegistrosMinutos(0,0,progreso.getAltura());
            }
        }

        //Calculo del tiempo transcurrido
        long tiempoRegistroMilis = (event.timestamp / 1000000L);
        long difDelayMilis = tiempoRegistroMilis - tiempoEventoAnterior;
        tiempoEventoAnterior = tiempoRegistroMilis;

        //Calculo de los pasos
        if (mCounterSteps < 1) {
            // initial value
            mCounterSteps = (int) event.values[0];
        }

        mSteps = (int) event.values[0] - mCounterSteps;
        Log.i("Numero Pasos:",""+mSteps);
        Log.i("Tiempo:",""+difDelayMilis);

        if(minutosCuarto[ind_minutosCuarto].isCompleto()){
            ind_minutosCuarto++;

            //Escritura del cuarto de hora
            if(ind_minutosCuarto == 14){
                RegistrosMinutos[] registrosAux = new RegistrosMinutos[(ind_minutosCuarto+1)-ind_comienzoCuarto];

                for(int i = 0; i <= registrosAux.length; i++)
                    registrosAux[i] = minutosCuarto[i+ind_comienzoCuarto];

                int edad = progreso.getEdad();
                double peso = progreso.getPeso();
                int sexo = progreso.getSexo();

                RegistrosCuartosDeHora registrosCuartosDeHora_aux = new RegistrosCuartosDeHora(registrosAux,registrosAux.length, Calendar.getInstance().getTime(),edad,sexo,peso);
                progreso.addRegistro(registrosCuartosDeHora_aux);
                this.guardarRegistros();
                ind_comienzoCuarto = 0;
                ind_minutosCuarto = 0;
            }
            minutoNuevo = true;
            mCounterSteps = 0;
            mSteps = 0;
        }
        else{

            if(minutoNuevo){
                minutosCuarto[ind_minutosCuarto] = new RegistrosMinutos(mSteps, difDelayMilis,progreso.getAltura());
                minutoNuevo = false;
            }
            else
                minutosCuarto[ind_minutosCuarto].addPasos(mSteps, difDelayMilis);

        }
        Log.i("StepSensorSample", "New step detected by STEP_COUNTER sensor. Total step count: " + mSteps);
    }*/


}
