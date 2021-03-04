package com.example.app_dietas.ui.Inicio;

import android.content.Context;
import android.content.Intent;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import com.example.app_dietas.Dietas.ComidaProgreso;
import com.example.app_dietas.Dietas.DietaProgreso;
import com.example.app_dietas.Dietas.DietaUsuario;
import com.example.app_dietas.R;
import com.example.app_dietas.ui.dieta_de_hoy.DietaDeHoyFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.Calendar;

public class InicioFragment extends Fragment {

    ProgressBar progresso_barra_dia_de_hoy;
    TextView Cuantos_dias_libres_tengo_text;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume(){
        super.onResume();
        this.setProgresoYDias();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ventana_principal, container, false);

        ////////////
        progresso_barra_dia_de_hoy= (ProgressBar) root.findViewById(R.id.progreso_del_dia_bar);
        Cuantos_dias_libres_tengo_text = (TextView) root.findViewById(R.id.tienes_n_dias);
        this.setProgresoYDias();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setProgresoYDias(){

        SharedPreferences sharedPref_ = getContext().getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaUsuario DietasDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);

        DietaProgreso ProgresoDieta = DietasDelUsuario.getProgresoDeDieta();
        ComidaProgreso[] progresoDeDias = ProgresoDieta.getComidasPorDia();
        int num_comidas_cumplidas = 0;
        int num_comidas_totales = 10;
        boolean fecha_actual_existe = false;

        Calendar progresoDiasC = Calendar.getInstance();
        Calendar diaHoyC = Calendar.getInstance();



        int ind = 0;
        do{
            progresoDiasC.setTime(progresoDeDias[ind].getFecha());
            if( progresoDiasC.get(Calendar.DAY_OF_MONTH) == diaHoyC.get(Calendar.DAY_OF_MONTH) && progresoDiasC.get(Calendar.MONTH) == diaHoyC.get(Calendar.MONTH) && progresoDiasC.get(Calendar.YEAR) == diaHoyC.get(Calendar.YEAR) && fecha_actual_existe==false){
                num_comidas_cumplidas = progresoDeDias[ind].getNumComidasConfirmadas();
                num_comidas_totales = progresoDeDias[ind].getNumComidasTotal();
                fecha_actual_existe = true;
            }
            ind++;
        }while(fecha_actual_existe==false && ind < ProgresoDieta.getNum_dias_dietas_llevados());

        if(fecha_actual_existe==false){
            num_comidas_cumplidas = progresoDeDias[ind-1].getNumComidasConfirmadas();
            num_comidas_totales = progresoDeDias[ind-1].getNumComidasTotal();
        }

        Log.i("num_comidas_cumplidas",""+num_comidas_cumplidas);
        Log.i("num_comidas_cumplidas",""+num_comidas_totales);

        double porcentaje = (double) num_comidas_cumplidas/num_comidas_totales;
        int num_dias_libres = ProgresoDieta.getNum_dias_libres();
        Log.i("DietaNLibreProgreso",String.valueOf(num_dias_libres));
        Log.i("DietaNLibreUsuario",String.valueOf(DietasDelUsuario.getDietaActual().getNum_dias_libres()));
        Log.i("NumDietasTotal","Quedan "+ProgresoDieta.getNum_dias_por_cumplir());
        Log.i("NumDietasTotalUsuario","Quedan "+DietasDelUsuario.getDietaActual().getNum_dias_dietas());
        Log.i("ProgresoDietas","Num Alimentos: "+ind);
        Log.i("Procentaje",""+porcentaje);
        progresso_barra_dia_de_hoy.setProgress((int) Math.round(100*porcentaje) );
        //Cuantos_dias_libres_tengo_text.setText("Tienes "+num_dias_libres+" dias libres");
        Cuantos_dias_libres_tengo_text.setText(num_comidas_cumplidas+" de "+num_comidas_totales+"\n comidas cumplidas hoy");
    }


    public void mostrarDietaDeHoy(View view){

        if(view.getId() == R.id.progreso_del_dia_bar){
        Intent intentInicio_Carga = new Intent(getContext(), DietaDeHoyFragment.class);
        startActivity(intentInicio_Carga);
        }
    }

}
