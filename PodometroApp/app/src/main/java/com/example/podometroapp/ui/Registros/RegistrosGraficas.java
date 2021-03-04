package com.example.podometroapp.ui.Registros;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.podometroapp.R;
import com.example.podometroapp.Registros.RegistrosCuartosDeHora;
import com.example.podometroapp.Registros.RegistrosMinutos;
import com.example.podometroapp.progreso.Progreso;
import com.example.podometroapp.progreso.RegistrosDia;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegistrosGraficas extends AppCompatActivity {

    private Progreso progreso;
    private Calendar CalendarioFechaActual;
    private int indBoton;
    private int indCategoria;
    private Button botonDias;
    private Button botonSemanas;
    private Button botonMes;
    private Button botonPasos;
    private Button botonDist;
    private Button botonCalorias;
    private BarChart barChart;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graficas_registros);

        indBoton = 0;
        indCategoria = 0;

        this.cargarProgreso();
        this.cargarCalendarioFechaActual();
        botonDias = findViewById(R.id.boton_dia);
        botonMes = findViewById(R.id.boton_mes);
        botonSemanas = findViewById(R.id.boton_semana);
        botonPasos = findViewById(R.id.boton_pasos);
        botonDist = findViewById(R.id.boton_distancia);
        botonCalorias = findViewById(R.id.boton_calorias);
        barChart = findViewById(R.id.barchart_graficas);

        this.setGrafica();

    }

    public void cargarProgreso(){
        SharedPreferences sharedPreferences = getSharedPreferences("ProgresoYRegistros", Context.MODE_PRIVATE);
        String progresoRegistrosJSON = sharedPreferences.getString("Progreso","");
        Gson gson = new Gson();
        progreso = gson.fromJson(progresoRegistrosJSON, Progreso.class);
    }

    public void cargarCalendarioFechaActual(){
        SharedPreferences sharedPref = getSharedPreferences("RegistrosPorDia", Context.MODE_PRIVATE);
        String registroBuscadoJSON = sharedPref.getString("RegistroDiaSeleccionado","");
        Gson gson = new Gson();
        RegistrosDia registroBuscado = gson.fromJson(registroBuscadoJSON,RegistrosDia.class);
        CalendarioFechaActual = Calendar.getInstance();
        CalendarioFechaActual.setTime(registroBuscado.getFecha());
    }


    public void getIdBoton(View view){

        if(view.getId() == R.id.boton_dia)
            indBoton = 0;
        else if(view.getId() == R.id.boton_semana)
            indBoton = 1;
        else if(view.getId() == R.id.boton_mes)
            indBoton = 2;
        else if(view.getId() == R.id.boton_pasos)
            indCategoria = 0;
        else if(view.getId() == R.id.boton_distancia)
            indCategoria = 1;
        else if(view.getId() == R.id.boton_calorias)
            indCategoria = 2;

        this.setGrafica();

    }

    public void setGrafica(){

        String[] xLabel = new String[0];
        List<BarEntry> barEntradas = new ArrayList<>();

        if(indBoton == 0){
            xLabel = new String[95];
            int horas = 0;
            int minutos = 15;

            for(int i = 0; i < xLabel.length; i++){
                if(minutos == 0)
                    xLabel[i] = horas+":0"+minutos;
                else
                    xLabel[i] = horas+":"+minutos;

                if(minutos == 45){
                    minutos = 0;
                    horas++;
                }
                else
                    minutos += 15;
            }

            List<RegistrosDia> registrosCuartosDia = progreso.getRegistroDia(CalendarioFechaActual);

            for(int i = 0; i < 95; i++){
                if(i < registrosCuartosDia.size()){
                    if(indCategoria == 0) //Pasos
                        barEntradas.add(new BarEntry(i, (float) registrosCuartosDia.get(i).getnPasosDados()));
                    else if(indCategoria == 1) //Distancia reccorida
                        barEntradas.add(new BarEntry(i, (float) registrosCuartosDia.get(i).getDistanciaTotalRecorrida()));
                    else if(indCategoria == 2) //Calorias
                        barEntradas.add(new BarEntry(i, (float) registrosCuartosDia.get(i).getCaloriasQuemadas()));
                }
                else{
                    barEntradas.add(new BarEntry(i, (float) 0.0));
                }
            }
        }
        else if(indBoton == 1){
            xLabel = new String[7];
            int DiaSemanaActual = CalendarioFechaActual.get(Calendar.DAY_OF_WEEK);

            if(DiaSemanaActual == 1)
                DiaSemanaActual = 6;
            else
                DiaSemanaActual = DiaSemanaActual - 2;

            Calendar calendarioAux = Calendar.getInstance();
            calendarioAux.setTime(CalendarioFechaActual.getTime());
            int diasARestar = - DiaSemanaActual;
            calendarioAux.add(Calendar.DAY_OF_YEAR, diasARestar);

            for(int i = 0; i < 7; i++){
                String dia, mes;

                if(calendarioAux.get(Calendar.DAY_OF_MONTH) < 10)
                    dia = "0"+calendarioAux.get(Calendar.DAY_OF_MONTH);
                else
                    dia = ""+calendarioAux.get(Calendar.DAY_OF_MONTH);

                if((calendarioAux.get(Calendar.MONTH)+1) < 10)
                    mes = "0"+(calendarioAux.get(Calendar.MONTH)+1);
                else
                    mes = ""+(calendarioAux.get(Calendar.MONTH)+1);

                int year = calendarioAux.get(Calendar.YEAR);
                int resta = year/100;
                int decenas = year - 100*resta;

                xLabel[i] = dia+"/"+mes+"/"+decenas;
                calendarioAux.add(Calendar.DAY_OF_YEAR,1);
            }

            calendarioAux.add(Calendar.DAY_OF_YEAR, -7);
            Log.i("FechaActual",CalendarioFechaActual.getTime().toString());
            Log.i("Fecha:",calendarioAux.getTime().toString());
            Log.i("Dias a restar:",diasARestar+"");
            List<RegistrosDia> registrosSemana = progreso.getRegistrosSemana(calendarioAux);

            for(int i = 0; i < 7; i++){
                if(i < registrosSemana.size()){
                    if(indCategoria == 0) //Pasos
                        barEntradas.add(new BarEntry(i, (float) registrosSemana.get(i).getnPasosDados()));
                    else if(indCategoria == 1) //Distancia recorrida
                        barEntradas.add(new BarEntry(i, (float) registrosSemana.get(i).getDistanciaTotalRecorrida()));
                    else if(indCategoria == 2) //Calorias
                        barEntradas.add(new BarEntry(i, (float) registrosSemana.get(i).getCaloriasQuemadas()));
                }
                else{
                    barEntradas.add( new BarEntry(i, (float) 0.0));
                }
            }
        }
        else if(indBoton == 2){

            if(CalendarioFechaActual.get(Calendar.MONTH) == 1 && CalendarioFechaActual.get(Calendar.YEAR)%4 != 0)
                xLabel = new String[28];
            else if(CalendarioFechaActual.get(Calendar.MONTH) == 1 && CalendarioFechaActual.get(Calendar.YEAR)%4 == 0)
                xLabel = new String[29];
            else if(CalendarioFechaActual.get(Calendar.MONTH) == 3 || CalendarioFechaActual.get(Calendar.MONTH) == 5 || CalendarioFechaActual.get(Calendar.MONTH) == 8 || CalendarioFechaActual.get(Calendar.MONTH) == 10)
                xLabel = new String[30];
            else
                xLabel = new String[31];

            for(int i = 0; i < xLabel.length; i++)
                xLabel[i] = String.valueOf(i+1);

            List<RegistrosDia> registrosMes = progreso.getRegistrosMes(CalendarioFechaActual);

            for(int i = 0; i < xLabel.length; i++){
                if(i < registrosMes.size()){
                    if(indCategoria == 0) //Pasos
                        barEntradas.add(new BarEntry(i, (float) registrosMes.get(i).getnPasosDados()));
                    else if(indCategoria == 1) //Distancia recorrida
                        barEntradas.add(new BarEntry(i, (float) registrosMes.get(i).getDistanciaTotalRecorrida()));
                    else if(indCategoria == 2) //Calorias
                        barEntradas.add(new BarEntry(i, (float) registrosMes.get(i).getCaloriasQuemadas()));
                }
                else{
                    barEntradas.add(new BarEntry(i, (float) 0.0));
                }
            }
        }

        final ArrayList<String> label = new ArrayList<>();
        for(int i = 0; i < xLabel.length; i++)
            label.add(xLabel[i]);

        BarDataSet barDataSet = new BarDataSet(barEntradas,"Dias");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.5f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return label.get((int) v);
            }
        });


        barChart.setData(data);
        barChart.animateY(5000);
        barChart.setFitBars(true);
        barChart.invalidate();



    }


}
