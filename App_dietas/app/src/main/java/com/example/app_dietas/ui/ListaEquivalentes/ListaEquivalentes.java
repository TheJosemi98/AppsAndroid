package com.example.app_dietas.ui.ListaEquivalentes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_dietas.Dietas.Alimento;
import com.example.app_dietas.Dietas.DietaUsuario;
import com.example.app_dietas.R;
import com.example.app_dietas.logica.AlimentoAdapter;
import com.example.app_dietas.ui.info_Nutricional_Comida.InfoNutricionalComida;
import com.example.app_dietas.ui.info_nutricional_alimento.InfoNutricionalAlimento;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListaEquivalentes extends AppCompatActivity {

    public RecyclerView ListaEquivalente;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager LayoutManager;

    private DietaUsuario DietaDelUsuario;
    private int IndComida;
    private int IndAlimento;
    private Date fechaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.equivalentes_list);

        ListaEquivalente = findViewById(R.id.Lista_equivalentes);

        SharedPreferences sharedPref_ = getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);

        Bundle bundleInfoNutricionalAlimento = this.getIntent().getExtras();
        IndComida = 0; IndAlimento = 0;
        String fechaSeleccionada_ = new String();
        fechaSeleccionada = Calendar.getInstance().getTime();

        if(bundleInfoNutricionalAlimento!= null){
            IndComida = bundleInfoNutricionalAlimento.getInt("TipoComida");
            IndAlimento = bundleInfoNutricionalAlimento.getInt("IndAlimento");
            fechaSeleccionada_ = bundleInfoNutricionalAlimento.getString("fechaSeleccionada");
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fechaSeleccionada = format.parse(fechaSeleccionada_);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("IndComida",""+IndComida);
        Log.i("IndAlimento",""+IndAlimento);


        //Use this setting to improve performance if you know that changes
        // int ccontent do not change the layout size of the RecyclerView
        ListaEquivalente.setHasFixedSize(true);

        // Use a linear Layout
        LayoutManager = new LinearLayoutManager(this);
        ListaEquivalente.setLayoutManager(LayoutManager);

        //Specify an adapter
        List<Alimento> ListaAlimento = DietaDelUsuario.getListaEquivalente(IndComida, IndAlimento, fechaSeleccionada);
        //Log.i("EquivalenteName",DietasDelUsuario.getProgresoDeDieta().getComidaPorDia());
        mAdapter = new AlimentoAdapter(ListaAlimento);
        ListaEquivalente.setAdapter(mAdapter);


        ((AlimentoAdapter) mAdapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DemoRecView", "Pulsado el elemento " + ListaEquivalente.getChildPosition(v));

                DietaDelUsuario.cambiarEquivalente(IndComida,IndAlimento,ListaEquivalente.getChildPosition(v),fechaSeleccionada);

                SharedPreferences sharedPref_ = getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor  = sharedPref_.edit();
                Gson gson = new Gson();
                String jsonDietaDelUsuario = gson.toJson(DietaDelUsuario);
                editor.putString("DietaDelUsuario",jsonDietaDelUsuario);
                editor.commit();
                finish();
            }
        });

    }

}
