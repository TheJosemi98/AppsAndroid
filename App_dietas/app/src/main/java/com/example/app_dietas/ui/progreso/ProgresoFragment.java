package com.example.app_dietas.ui.progreso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.app_dietas.Dietas.DietaProgreso;
import com.example.app_dietas.Dietas.DietaUsuario;
import com.example.app_dietas.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProgresoFragment extends Fragment {

    private TextView N_Dias_;
    private TextView Porcentaje_progreso;
    private ProgressBar BarraProgresoSimple;
    private PieChart Diagrama_Sectores_P;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        this.mostrarProgreso();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progreso, container, false);

        N_Dias_ = root.findViewById(R.id.N_dias_);
        BarraProgresoSimple = root.findViewById(R.id.progressBar_Progreso);
        Diagrama_Sectores_P = root.findViewById(R.id.piechart_progreso_detallado);
        Porcentaje_progreso = root.findViewById(R.id.progreso_porcentaje);
        this.mostrarProgreso();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void mostrarProgreso(){
        SharedPreferences sharedPref_ = getContext().getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaUsuario DietasDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);
        DietaProgreso Progreso = DietasDelUsuario.getProgresoDeDieta();


        //ESTA MODIFICADO LA CLASE PROGRESO
        N_Dias_.setText(String.valueOf(Progreso.getNum_dias_cumplidos_seguidos()));

        double Porcentaje_dias_cumplidos;

        if(Progreso.getNum_dias_dietas_llevados()==0)
            Porcentaje_dias_cumplidos = 0;
        else
            Porcentaje_dias_cumplidos = 100*Progreso.getNum_dias_cumplidos_completos()/Progreso.getNum_dias_dietas_llevados();

        Log.i("PorcentajeCumplido",Porcentaje_dias_cumplidos+"");
        Porcentaje_progreso.setText(Porcentaje_dias_cumplidos+"%");
        //Porcentaje_progreso.setText(String.valueOf(50)+"% completado");
        BarraProgresoSimple.setProgress((int) Porcentaje_dias_cumplidos,true);


        List<PieEntry> pieEntradas = new ArrayList<>();
        if(Progreso.getNum_dias_cumplidos_completos() != 0)
            pieEntradas.add(new PieEntry(Progreso.getNum_dias_cumplidos_completos(),"Dias cumplidos por completo"));
        if(Progreso.getNum_dias_cumplidos_parcial() != 0)
            pieEntradas.add(new PieEntry(Progreso.getNum_dias_cumplidos_parcial(),"Dias cumplidos parcial"));
        if(Progreso.getNum_dias_no_cumplidos() != 0)
            pieEntradas.add(new PieEntry(Progreso.getNum_dias_no_cumplidos(),"Dias no cumplidos"));
        if(Progreso.getNum_dias_tomados_libres() != 0)
            pieEntradas.add(new PieEntry(Progreso.getNum_dias_tomados_libres(),"Dias tomados libres"));
        if(Progreso.getNum_dias_por_cumplir() != 0)
            pieEntradas.add(new PieEntry(Progreso.getNum_dias_por_cumplir(),"Dias por cumplir"));

        PieDataSet dataSet = new PieDataSet(pieEntradas,"Tipos de dias");
        PieData data = new PieData(dataSet);
        Diagrama_Sectores_P.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

    }



}
