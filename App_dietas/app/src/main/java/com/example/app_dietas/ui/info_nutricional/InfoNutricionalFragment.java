package com.example.app_dietas.ui.info_nutricional;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.app_dietas.Dietas.Alimento;
import com.example.app_dietas.Dietas.ComidaProgreso;
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
import java.util.Calendar;
import java.util.List;

public class InfoNutricionalFragment extends Fragment {

    private TextView caloriasT;
    private TextView proteinasT;
    private TextView hidratos_de_carbonoT;
    private TextView grasasT;
    private PieChart Diagrama_sectores_T;


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPref_ = getContext().getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaUsuario DietasDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);

        this.fijarPropiedadesDeComidas(DietasDelUsuario);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_info_nutricional, container, false);
        caloriasT = root.findViewById(R.id.Calorias_T);
        proteinasT = root.findViewById(R.id.Proteinas_T);
        hidratos_de_carbonoT = root.findViewById(R.id.HidratosDeCaborno_T);
        grasasT = root.findViewById(R.id.Grasas_T);
        Diagrama_sectores_T = root.findViewById(R.id.piechart_Info_N_T);

        SharedPreferences sharedPref_ = getContext().getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaUsuario DietasDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);

        this.fijarPropiedadesDeComidas(DietasDelUsuario);


        return root;
    }


    public void fijarPropiedadesDeComidas(DietaUsuario DietaDelUsuario){
        double CaloriasTotal = 0, grasasTotal = 0, HidratosTotal = 0, ProteinasTotal = 0;

        DietaProgreso Progreso = DietaDelUsuario.getProgresoDeDieta();
        ComidaProgreso[] progresoDeDias = Progreso.getComidasPorDia();
        List<PieEntry> pieEntradas = new ArrayList<>();
        ArrayList<String> TipoDeAlimentos = new ArrayList();
        ArrayList<Integer> num_TipoDeAlimentos = new ArrayList();

        int ind = 0;
        do{
            int[] num_alimentos = progresoDeDias[ind].getNum_alimentos();
            Alimento[][] AlimentosDeComida = progresoDeDias[ind].getComidasDelDia();
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < num_alimentos[i]; j++){
                    CaloriasTotal  += AlimentosDeComida[i][j].getEnergía()*(AlimentosDeComida[i][j].getCantidadNumerica()/100.0);
                    grasasTotal  += AlimentosDeComida[i][j].getGrasas()*(AlimentosDeComida[i][j].getCantidadNumerica()/100.0);
                    HidratosTotal  += AlimentosDeComida[i][j].getHidratosCarbono()*(AlimentosDeComida[i][j].getCantidadNumerica()/100.0);
                    ProteinasTotal  += AlimentosDeComida[i][j].getProteinas()*(AlimentosDeComida[i][j].getCantidadNumerica()/100.0);

                    if(ind==0 & i==0 && j==0){
                        TipoDeAlimentos.add(AlimentosDeComida[i][j].getGrupoAlimenticio());
                        num_TipoDeAlimentos.add(1);
                    }
                    else{
                        boolean encontrado = false;
                        for(int z = 0;  z < TipoDeAlimentos.size(); z++){
                            if(TipoDeAlimentos.get(z).equals(AlimentosDeComida[i][j].getGrupoAlimenticio())){
                                int num_aux = num_TipoDeAlimentos.get(z)+1;
                                num_TipoDeAlimentos.set(z,num_aux);
                                encontrado = true;
                            }
                        }
                        if(!encontrado){
                            TipoDeAlimentos.add(AlimentosDeComida[i][j].getGrupoAlimenticio());
                            num_TipoDeAlimentos.add(1);
                        }
                    }

                }

            }

            ind++;
        }while(ind < Progreso.getNum_dias_dietas_llevados());

        caloriasT.setText(" "+(Math.round(CaloriasTotal)/ind)+" Kcal/dia");
        grasasT.setText(" "+(Math.round(grasasTotal)/ind)+"g/dia");
        hidratos_de_carbonoT.setText(" "+(Math.round(HidratosTotal)/ind)+"g/dia");
        proteinasT.setText(" "+(Math.round(ProteinasTotal)/ind)+"g/dia");

        for(int i = 0; i < TipoDeAlimentos.size(); i++)
            pieEntradas.add(new PieEntry(num_TipoDeAlimentos.get(i),TipoDeAlimentos.get(i)));

        PieDataSet dataSet = new PieDataSet(pieEntradas,"");
        PieData data = new PieData(dataSet);
        Diagrama_sectores_T.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

    }



}
