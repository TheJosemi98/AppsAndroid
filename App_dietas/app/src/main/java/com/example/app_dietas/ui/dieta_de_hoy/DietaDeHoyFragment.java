package com.example.app_dietas.ui.dieta_de_hoy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class DietaDeHoyFragment extends Fragment {

    private TextView caloriasH;
    private TextView proteinasH;
    private TextView hidratos_de_carbonoH;
    private TextView grasasH;
    public Button ButtonDesayunoH;
    public Button ButtonAlmuerzoH;
    public Button ButtonMeriendaH;
    public Button ButtonCenaH;
    private PieChart Diagrama_sectores;

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPref_ = getContext().getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaUsuario DietasDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);

        this.fijarPropiedadesDeComidas(DietasDelUsuario);
        this.setDiagramaDeSectores(DietasDelUsuario);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dieta_hoy, container, false);

        caloriasH = root.findViewById(R.id.Calorias_);
        proteinasH = root.findViewById(R.id.Proteinas_);
        hidratos_de_carbonoH = root.findViewById(R.id.HidratosDeCaborno_);
        grasasH = root.findViewById(R.id.Grasas_);
        ButtonDesayunoH = root.findViewById(R.id.desayuno_dieta_dia);
        ButtonAlmuerzoH = root.findViewById(R.id.almuerzo_dieta_dia);
        ButtonMeriendaH = root.findViewById(R.id.merienda_dieta_dia);
        ButtonCenaH = root.findViewById(R.id.cena_dieta_dia);
        Diagrama_sectores = root.findViewById(R.id.piechart_Dietas_Del_Dia);

        SharedPreferences sharedPref_ = getContext().getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaUsuario DietasDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);

        this.fijarPropiedadesDeComidas(DietasDelUsuario);
        this.setDiagramaDeSectores(DietasDelUsuario);

        return root;
    }

    public void fijarPropiedadesDeComidas(DietaUsuario DietaDelUsuario){
        double CaloriasDeHoy = 0, grasasDeHoy = 0, HidratosDeHoy = 0, ProteinasDeHoy = 0;

        DietaProgreso Progreso = DietaDelUsuario.getProgresoDeDieta();
        ComidaProgreso[] progresoDeDias = Progreso.getComidasPorDia();
        Calendar fecha_hoy = Calendar.getInstance();
        boolean fin_bucle = false;


        int ind = 0;
        do{
            if(progresoDeDias[ind].getFecha().getDate() == fecha_hoy.getTime().getDate() && progresoDeDias[ind].getFecha().getMonth() == fecha_hoy.getTime().getMonth() && progresoDeDias[ind].getFecha().getYear() == fecha_hoy.getTime().getYear()){
                fin_bucle = true;
                int[] num_alimentos = progresoDeDias[ind].getNum_alimentos();
                Alimento[][] AlimentosDeComida = progresoDeDias[ind].getComidasDelDia();
                for(int i = 0; i < 4; i++){
                    for(int j = 0; j < num_alimentos[i]; j++){
                        CaloriasDeHoy  += AlimentosDeComida[i][j].getEnergía()*(AlimentosDeComida[i][j].getCantidadNumerica()/100.0);
                        grasasDeHoy  += AlimentosDeComida[i][j].getGrasas()*(AlimentosDeComida[i][j].getCantidadNumerica()/100.0);
                        HidratosDeHoy  += AlimentosDeComida[i][j].getHidratosCarbono()*(AlimentosDeComida[i][j].getCantidadNumerica()/100.0);
                        ProteinasDeHoy  += AlimentosDeComida[i][j].getProteinas()*(AlimentosDeComida[i][j].getCantidadNumerica()/100.0);
                    }

                }
            }
            ind++;
        }while(fin_bucle==false && ind < Progreso.getNum_dias_dietas_llevados());

        caloriasH.setText(" "+Math.round(CaloriasDeHoy)+" Kcal");
        grasasH.setText(" "+Math.round(grasasDeHoy)+"g");
        hidratos_de_carbonoH.setText(" "+Math.round(HidratosDeHoy)+"g");
        proteinasH.setText(" "+Math.round(ProteinasDeHoy)+"g");
    }

    public void setDiagramaDeSectores(DietaUsuario DietaDelUsuario){

        DietaProgreso Progreso = DietaDelUsuario.getProgresoDeDieta();
        ComidaProgreso[] progresoDeDias = Progreso.getComidasPorDia();
        Calendar fecha_hoy = Calendar.getInstance();
        Calendar calendar_registro = Calendar.getInstance();


        boolean fin_bucle = false;
        List<PieEntry> pieEntradas = new ArrayList<>();
        ArrayList<String> TipoDeAlimentos = new ArrayList();
        ArrayList<Integer> num_TipoDeAlimentos = new ArrayList();

        int ind = 0;
        do{
            Log.i("Es hoy?",String.valueOf(progresoDeDias[ind].getFecha().compareTo(fecha_hoy.getTime())));
            Log.i("Fecha de hoy",String.valueOf(fecha_hoy.getTime().toString()));
            Log.i("Fecha indicada",String.valueOf(progresoDeDias[ind].getFecha().toString()));
            calendar_registro.setTime(progresoDeDias[ind].getFecha());
            if(calendar_registro.get(Calendar.DAY_OF_MONTH) == fecha_hoy.get(Calendar.DAY_OF_MONTH)  && calendar_registro.get(Calendar.MONTH)  == fecha_hoy.get(Calendar.MONTH) && calendar_registro.get(Calendar.YEAR)  == fecha_hoy.get(Calendar.YEAR)){
                fin_bucle = true;
                int[] num_alimentos = progresoDeDias[ind].getNum_alimentos();
                Alimento[][] AlimentosDeComida = progresoDeDias[ind].getComidasDelDia();
                for(int i = 0; i < 4; i++){
                    for(int j = 0; j < num_alimentos[i]; j++){

                        if(i==0 && j==0){
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
            }
            ind++;
        }while(fin_bucle==false && ind < Progreso.getNum_dias_dietas_llevados());

        for(int i = 0; i < TipoDeAlimentos.size(); i++)
            pieEntradas.add(new PieEntry(num_TipoDeAlimentos.get(i),TipoDeAlimentos.get(i)));

        PieDataSet dataSet = new PieDataSet(pieEntradas,"Numero de alimentos");
        PieData data = new PieData(dataSet);
        Diagrama_sectores.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

    }


}
