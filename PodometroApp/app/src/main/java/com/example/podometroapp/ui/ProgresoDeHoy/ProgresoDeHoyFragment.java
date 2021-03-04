package com.example.podometroapp.ui.ProgresoDeHoy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.podometroapp.R;
import com.example.podometroapp.Registros.RegistrosCuartosDeHora;
import com.example.podometroapp.progreso.Progreso;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Calendar;

public class ProgresoDeHoyFragment extends Fragment {

    private Progreso progreso;

    private TextView NPasosDados;
    private TextView PasosObjetivoT;
    private TextView velocidadMediaT;
    private TextView distanciaRecorridaT;
    private TextView caloriasQuemadasT;
    private TextView tiempoActivoT; //Activo si la velocidad Media es mas de 6Km

    @Override
    public void onResume() {
        super.onResume();
        this.setProgreso();
        this.setProgresoDeHoy();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.setProgreso();
        this.setProgresoDeHoy();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.setProgreso();
        this.setProgresoDeHoy();
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_progreso_de_hoy, container, false);

        NPasosDados = root.findViewById(R.id.N_pasos_text);
        PasosObjetivoT = root.findViewById(R.id.Objetivo_text);
        velocidadMediaT = root.findViewById(R.id.velocidad_media_velMed);
        distanciaRecorridaT = root.findViewById(R.id.distancia_recorrida);
        tiempoActivoT = root.findViewById(R.id.tiempo_activo_);
        caloriasQuemadasT = root.findViewById(R.id.calorias_quemadas);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ProgresoYRegistros", Context.MODE_PRIVATE);
        String progresoRegistrosJSON = sharedPreferences.getString("Progreso","");
        Gson gson = new Gson();
        progreso = gson.fromJson(progresoRegistrosJSON, Progreso.class);

        this.setProgreso();
        this.setProgresoDeHoy();
        return root;
    }

    public void setProgreso(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ProgresoYRegistros", Context.MODE_PRIVATE);
        String progresoRegistrosJSON = sharedPreferences.getString("Progreso","");
        Gson gson = new Gson();
        progreso = gson.fromJson(progresoRegistrosJSON, Progreso.class);
    }

    public void setProgresoDeHoy(){

        Calendar calendarioHoy = Calendar.getInstance();
        Calendar calendarioRegistro = Calendar.getInstance();

        if(progreso.getNum_registros() != 0){
            RegistrosCuartosDeHora[] registrosTotales = progreso.getRegistrosObtenidos();

            double caloriasQuemadasH, velocidadMediaH, distanciaRecorridaH;
            int tiempoActivoMH, NPasosHoy = 0;

            double caloriasQuemadasSum = 0.0, velocidadMediaSum = 0.0, distanciaRecorridaSum = 0.0;
            int tiempoActivoMSum= 0;
            int num_registros = 0;

            for(int i = 0; i < progreso.getNum_registros(); i++){
                calendarioRegistro.setTime(registrosTotales[i].getFecha());
                if(calendarioRegistro.get(Calendar.DAY_OF_MONTH) == calendarioHoy.get(Calendar.DAY_OF_MONTH) && calendarioRegistro.get(Calendar.MONTH) == calendarioHoy.get(Calendar.MONTH) && calendarioRegistro.get(Calendar.YEAR) == calendarioHoy.get(Calendar.YEAR)){
                    caloriasQuemadasSum += registrosTotales[i].getCaloriasQuemadas();
                    velocidadMediaSum += registrosTotales[i].getVelocidadMediaKmH();
                    distanciaRecorridaSum += registrosTotales[i].getDistanciaRecorrida();
                    tiempoActivoMSum += registrosTotales[i].getTiempoActivoMin();
                    Log.i("tiempoActivoMH:",""+registrosTotales[i].getTiempoActivoMin());
                    NPasosHoy+= registrosTotales[i].getPasosDados();
                    num_registros++;
                }
            }

            if(num_registros == 0)
                num_registros = 1;

            caloriasQuemadasH = caloriasQuemadasSum;
            velocidadMediaH = velocidadMediaSum/num_registros;
            distanciaRecorridaH = distanciaRecorridaSum;
            tiempoActivoMH = tiempoActivoMSum;
            int horas = tiempoActivoMH/60;
            Log.i("tiempoActivoMH:",""+tiempoActivoMH);
            Log.i("Horas:",""+horas);
            int minutos = tiempoActivoMH-60*horas;

            NPasosDados.setText(String.valueOf(NPasosHoy));
            PasosObjetivoT.setText("(Objetivo: "+progreso.getPasosObjetivo()+")");
            velocidadMediaT.setText(String.format("%.2f",velocidadMediaH)+" Km/h");
            distanciaRecorridaT.setText(String.format("%.3f",distanciaRecorridaH)+" Km");
            caloriasQuemadasT.setText(String.format("%.1f",caloriasQuemadasH)+" Kcal");
            if(minutos > 9)
                tiempoActivoT.setText(horas+":"+minutos+" horas");
            else
                tiempoActivoT.setText(horas+":0"+minutos+" horas");
        }
        else{
            NPasosDados.setText(String.valueOf(0));
            PasosObjetivoT.setText("(Objetivo: "+progreso.getPasosObjetivo()+")");
            velocidadMediaT.setText(0+" Km/h");
            distanciaRecorridaT.setText(0+" Km");
            caloriasQuemadasT.setText(0+" Kcal");
            tiempoActivoT.setText(" 0:00 horas");
        }

    }


}
