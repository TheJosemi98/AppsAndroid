package com.example.podometroapp.ui.Registros;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.podometroapp.R;
import com.example.podometroapp.progreso.Progreso;
import com.example.podometroapp.progreso.RegistroDiaAdapter;
import com.example.podometroapp.progreso.RegistrosDia;
import com.google.gson.Gson;

import java.util.List;

public class RegistrosFragment extends Fragment {

    private Progreso progreso;
    private RecyclerView ListaRegistros;
    private RecyclerView.Adapter registrosDiaAdapter;
    private RecyclerView.LayoutManager registrosLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_registro, container, false);

        ListaRegistros = root.findViewById(R.id.ListaRegistros);
        ListaRegistros.setHasFixedSize(true);
        this.cargarProgreso();

        registrosLayoutManager = new LinearLayoutManager(getContext());
        ListaRegistros.setLayoutManager(registrosLayoutManager);
        final List<RegistrosDia> ListaRegistroPorDia = progreso.getRegistrosDias();
        registrosDiaAdapter = new RegistroDiaAdapter(ListaRegistroPorDia);
        ListaRegistros.setAdapter(registrosDiaAdapter);
        ((RegistroDiaAdapter) registrosDiaAdapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegistroGraficas  = new Intent(getContext(),RegistrosGraficas.class);
                SharedPreferences sharedPref = getContext().getSharedPreferences("RegistrosPorDia", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor  = sharedPref.edit();
                Gson gson = new Gson();
                RegistrosDia registroBuscado = ListaRegistroPorDia.get(ListaRegistros.getChildPosition(v));
                String jsonRegistrosPorDia = gson.toJson(registroBuscado);
                editor.putString("RegistroDiaSeleccionado",jsonRegistrosPorDia);
                editor.commit();

                startActivity(intentRegistroGraficas);

            }
        });



        return root;
    }

    public void cargarProgreso(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ProgresoYRegistros", Context.MODE_PRIVATE);
        String progresoRegistrosJSON = sharedPreferences.getString("Progreso","");
        Gson gson = new Gson();
        progreso = gson.fromJson(progresoRegistrosJSON, Progreso.class);
    }

}
