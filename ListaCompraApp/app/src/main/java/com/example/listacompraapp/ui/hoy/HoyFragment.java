package com.example.listacompraapp.ui.hoy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.listacompraapp.Listas.Articulo;
import com.example.listacompraapp.Listas.ListaDeUnDia;
import com.example.listacompraapp.Listas.ListaTotal;
import com.example.listacompraapp.R;
import com.example.listacompraapp.ui.editarAddArticulo.editarAddArticuloActivity;
import com.example.listacompraapp.ui.progreso.dayActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HoyFragment extends Fragment {

    private ListaTotal listaTotal;
    private PieChart pieChart;
    private TextView gastoHoy;
    private Calendar calendario;
    private RecyclerView ListaArticulos;
    private RecyclerView.Adapter articulosAdapter;
    private RecyclerView.LayoutManager articulosLayoutManager;

    @Override
    public void onResume() {
        this.cargarListaTotal();
        this.setProgresosHoy();

        gastoHoy.setText(String.format("%.2f €",listaTotal.getDia(calendario).getGastoDia()));
        ListaArticulos.setHasFixedSize(true);

        articulosLayoutManager = new LinearLayoutManager(getContext());
        ListaArticulos.setLayoutManager(articulosLayoutManager);
        final List<Articulo> articulos = listaTotal.getDia(calendario).getListaArticulos();
        articulosAdapter = new ArticuloAdapter(articulos);
        ListaArticulos.setAdapter(articulosAdapter);
        ((ArticuloAdapter) articulosAdapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDayEditar = new Intent(getContext(), editarAddArticuloActivity.class);
                Bundle bundleDayEditar = new Bundle();

                int posDia = listaTotal.getPosDia(calendario);
                bundleDayEditar.putInt("AñadirEditar",0);
                bundleDayEditar.putInt("PosDia",posDia);
                bundleDayEditar.putInt("PosArticulo",ListaArticulos.getChildPosition(v));
                intentDayEditar.putExtras(bundleDayEditar);
                startActivity(intentDayEditar);

            }
        });

        super.onResume();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_hoy, container, false);

        pieChart = root.findViewById(R.id.piechart_categoria_hoy);
        gastoHoy = root.findViewById(R.id.Gasto_text_edit);
        ListaArticulos = root.findViewById(R.id.Lista_recyclerview_hoy);
        this.cargarListaTotal();
        this.setProgresosHoy();

        calendario = Calendar.getInstance();
        gastoHoy.setText(String.format("%.2f €",listaTotal.getDia(calendario).getGastoDia()));

        ListaArticulos.setHasFixedSize(true);

        articulosLayoutManager = new LinearLayoutManager(getContext());
        ListaArticulos.setLayoutManager(articulosLayoutManager);
        final List<Articulo> articulos = listaTotal.getDia(calendario).getListaArticulos();
        articulosAdapter = new ArticuloAdapter(articulos);
        ListaArticulos.setAdapter(articulosAdapter);
        ((ArticuloAdapter) articulosAdapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDayEditar = new Intent(getContext(), editarAddArticuloActivity.class);
                Bundle bundleDayEditar = new Bundle();

                int posDia = listaTotal.getPosDia(calendario);
                bundleDayEditar.putInt("AñadirEditar",0);
                bundleDayEditar.putInt("PosDia",posDia);
                bundleDayEditar.putInt("PosArticulo",ListaArticulos.getChildPosition(v));
                intentDayEditar.putExtras(bundleDayEditar);
                startActivity(intentDayEditar);

            }
        });

        return root;
    }

    public void cargarListaTotal(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ListaTotal", Context.MODE_PRIVATE);
        String ListaTotalJSON = sharedPreferences.getString("ListaCompra","");
        Log.i("ListaTotalJSON",ListaTotalJSON);
        Gson gson = new Gson();
        listaTotal = gson.fromJson(ListaTotalJSON,ListaTotal.class);
    }

    public void setProgresosHoy(){
        Calendar calendarioHoy = Calendar.getInstance();
        ListaDeUnDia listaDeUnDiaHoy = listaTotal.getDia(calendarioHoy);
        List<PieEntry> pieEntradas = new ArrayList<>();

        String[] categorias  = listaDeUnDiaHoy.getCategorias();

        for(int i = 0; i < categorias.length; i++)
            pieEntradas.add(new PieEntry(listaDeUnDiaHoy.getNumVecesCategoria(categorias[i]),categorias[i]));

        PieDataSet dataSet = new PieDataSet(pieEntradas,"Categorias");
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

    }


}
