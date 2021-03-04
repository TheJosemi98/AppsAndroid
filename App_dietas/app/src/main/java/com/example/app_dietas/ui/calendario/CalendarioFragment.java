package com.example.app_dietas.ui.calendario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.app_dietas.Dietas.Alimento;
import com.example.app_dietas.Dietas.ComidaDieta;
import com.example.app_dietas.Dietas.ComidaProgreso;
import com.example.app_dietas.Dietas.DietaProgreso;
import com.example.app_dietas.Dietas.DietaUsuario;
import com.example.app_dietas.R;
import com.example.app_dietas.ui.InicioSesion.CargaPantalla;
import com.example.app_dietas.ui.InicioSesion.InicioSesion;
import com.example.app_dietas.ui.ListaEquivalentes.ListaEquivalentes;
import com.example.app_dietas.ui.info_Nutricional_Comida.InfoNutricionalComida;
import com.example.app_dietas.ui.info_nutricional_alimento.InfoNutricionalAlimento;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarioFragment extends Fragment {

    private CalendarView calendario;
    private Button textDesayuno;
    private Button textAlmuerzo;
    private Button textMerienda;
    private Button textCena;
    private DietaUsuario DietaDelUsuario;
    private Date curDate_;
    private String[] textComidas;


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref_ = getContext().getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendario, container, false);
        calendario = root.findViewById(R.id.Calendario);
        textDesayuno = root.findViewById(R.id.Desayuno);
        textAlmuerzo = root.findViewById(R.id.Almuerzo);
        textMerienda = root.findViewById(R.id.Merienda);
        textCena = root.findViewById(R.id.Cena);
        textComidas = new String[4];

        SharedPreferences sharedPref_ = getContext().getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);


        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendario_ = Calendar.getInstance();
                calendario_.set(year,month,dayOfMonth);
                curDate_ = new Date();
                curDate_.setYear(year);
                Log.i("Año",""+year);
                Log.i("ConCalendar",""+calendario_.getTime().toString());
                Log.i("ConCalendarYear",""+calendario_.get(Calendar.YEAR));
                curDate_.setDate(dayOfMonth);
                curDate_.setMonth(month);
                Log.i("Fecha seleccionada",curDate_.toString());
                Calendar Calendar_fecha_hoy = Calendar.getInstance();
                Date fecha_hoy = Calendar_fecha_hoy.getTime();
                Calendar calendario_progreso = Calendar.getInstance();

                DietaProgreso Progreso = DietaDelUsuario.getProgresoDeDieta();
                ComidaProgreso[] progresoDeDias = Progreso.getComidasPorDia();
                int ind = 0, indDia = 0;
                boolean fin_bucle = false;
                do{
                    calendario_progreso.setTime(progresoDeDias[ind].getFecha());
                    Log.i("Fecha Progreso",progresoDeDias[ind].getFecha().toString());
                    Log.i("Fecha Date",""+calendario_progreso.get(Calendar.DAY_OF_MONTH));
                    Log.i("Fecha Month",""+(calendario_progreso.get(Calendar.MONTH)+1));
                    Log.i("Fecha Year",""+calendario_progreso.get(Calendar.YEAR));

                    if(calendario_progreso.get(Calendar.DAY_OF_MONTH) == calendario_.get(Calendar.DAY_OF_MONTH) && (calendario_progreso.get(Calendar.MONTH)+1) == (calendario_.get(Calendar.MONTH)+1) && calendario_progreso.get(Calendar.YEAR) == calendario_.get(Calendar.YEAR)){
                        fin_bucle = true;
                        int[] num_Alimentos = progresoDeDias[ind].getNum_alimentos();
                        boolean[][] confirmados = progresoDeDias[ind].getConfirmados();


                        Log.i("Fecha hoy",fecha_hoy.toString());
                        Log.i("Fecha progreso",fecha_hoy.toString());
                        Log.i("Es anterior?",""+progresoDeDias[ind].getFecha().compareTo(fecha_hoy));

                        if(progresoDeDias[ind].getFecha().compareTo(fecha_hoy) <= 0) {
                            for (int i = 0; i < 4; i++) {

                                int confirmados_sum = 0;
                                int color = Color.GRAY;

                                for (int j = 0; j < num_Alimentos[i]; j++) {
                                    if(confirmados[i][j] == true)
                                        confirmados_sum++;
                                }

                                textComidas[i] = "Confirmados "+confirmados_sum+" de "+num_Alimentos[i]+" alimentos";

                                Log.i("NumConfirmados",""+confirmados_sum);
                                Log.i("IndComida",""+i);

                            }
                        }
                        else{

                            for(int i = 0; i < 4; i++)
                                textComidas[i] = "Alimentos sin poder confirmar";
                        }
                    }
                    ind++;
                }while(fin_bucle==false && ind < Progreso.getNum_dias_dietas_llevados());

                SharedPreferences sharedPref_ = getContext().getSharedPreferences("Fecha Seleccionada", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor  = sharedPref_.edit();
                String fecha_a_guardar = curDate_.getDate()+"/"+(curDate_.getMonth()+1)+"/"+curDate_.getYear();
                Log.i("Fecha seleccionada__",fecha_a_guardar);
                editor.putString("FechaSeleccionada",fecha_a_guardar);
                editor.commit();

                sharedPref_ = getContext().getSharedPreferences("TextBotones",Context.MODE_PRIVATE);
                editor = sharedPref_.edit();
                editor.putString("Desayuno",textComidas[0]);
                editor.putString("Almuerzo",textComidas[1]);
                editor.putString("Merienda",textComidas[2]);
                editor.putString("Cena",textComidas[3]);
                editor.commit();



            }
        });

        return root;
    }





}
