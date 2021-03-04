package com.example.listacompraapp.ui.progreso;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listacompraapp.Adapters.ArticuloAdapter;
import com.example.listacompraapp.Adapters.ListaDiaAdapter;
import com.example.listacompraapp.Listas.Articulo;
import com.example.listacompraapp.Listas.ListaDeUnDia;
import com.example.listacompraapp.Listas.ListaTotal;
import com.example.listacompraapp.R;
import com.google.gson.Gson;

import java.util.List;

public class ProgresoFragment extends Fragment {

    private ListaTotal listaTotal;
    private RecyclerView listaDeDias;
    private RecyclerView.Adapter listaDeDiasAdapter;
    private RecyclerView.LayoutManager listaDeDiasLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progreso, container, false);
        this.cargarListaTotal();
        listaDeDias = root.findViewById(R.id.Lista_recyclerview_progreso);

        listaDeDiasLayoutManager = new LinearLayoutManager(getContext());
        listaDeDias.setLayoutManager(listaDeDiasLayoutManager);
        final List<ListaDeUnDia> listaDeDias_ = listaTotal.getListaDias();
        listaDeDiasAdapter = new ListaDiaAdapter(listaDeDias_);
        listaDeDias.setAdapter(listaDeDiasAdapter);
        ((ListaDiaAdapter) listaDeDiasAdapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegistroDia = new Intent(getContext(),dayActivity.class);
                Bundle bundleRegistroDia = new Bundle();
                int pos = listaDeDias.getChildPosition(v);
                bundleRegistroDia.putInt("PosDia",pos);
                intentRegistroDia.putExtras(bundleRegistroDia);
                startActivity(intentRegistroDia);
            }
        });

        return root;
    }

    public void cargarListaTotal(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ListaTotal", Context.MODE_PRIVATE);
        String ListaTotalJSON = sharedPreferences.getString("ListaCompra","");
        Gson gson = new Gson();
        listaTotal = gson.fromJson(ListaTotalJSON,ListaTotal.class);
    }


}



