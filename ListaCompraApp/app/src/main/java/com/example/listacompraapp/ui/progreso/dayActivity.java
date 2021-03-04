package com.example.listacompraapp.ui.progreso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listacompraapp.Adapters.ArticuloAdapter;
import com.example.listacompraapp.Listas.Articulo;
import com.example.listacompraapp.Listas.ListaDeUnDia;
import com.example.listacompraapp.Listas.ListaTotal;
import com.example.listacompraapp.R;
import com.example.listacompraapp.ui.editarAddArticulo.editarAddArticuloActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class dayActivity extends AppCompatActivity {

    private ListaTotal listaTotal;
    private PieChart pieChart;
    private TextView gastoHoy;
    private int posDia;
    private Calendar calendario;
    private RecyclerView ListaArticulos;
    private RecyclerView.Adapter articulosAdapter;
    private RecyclerView.LayoutManager articulosLayoutManager;


    @Override
    protected void onResume(){
        super.onResume();

        Bundle bundleRegistroDia = this.getIntent().getExtras();
        posDia = bundleRegistroDia.getInt("PosDia");
        calendario.setTime(listaTotal.getListaDiaPos(posDia).getFecha_dia());
        gastoHoy.setText(String.format("%.2f €",listaTotal.getDia(calendario).getGastoDia()));
        this.setProgresosDia();
        articulosLayoutManager = new LinearLayoutManager(this);
        ListaArticulos.setLayoutManager(articulosLayoutManager);
        final List<Articulo> articulos = listaTotal.getDia(calendario).getListaArticulos();
        articulosAdapter = new ArticuloAdapter(articulos);
        ListaArticulos.setAdapter(articulosAdapter);
        ((ArticuloAdapter) articulosAdapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDayEditar = new Intent(dayActivity.this, editarAddArticuloActivity.class);
                Bundle bundleDayEditar = new Bundle();

                bundleDayEditar.putInt("AñadirEditar",0);
                bundleDayEditar.putInt("PosDia",posDia);
                bundleDayEditar.putInt("PosArticulo",ListaArticulos.getChildPosition(v));
                intentDayEditar.putExtras(bundleDayEditar);
                startActivity(intentDayEditar);
            }
        });
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progreso_de_un_dia);

        ListaArticulos = findViewById(R.id.Lista_recyclerview_hoy_dia);
        pieChart = findViewById(R.id.piechart_categoria_dia);
        gastoHoy = findViewById(R.id.Gasto_text_edit_dia);
        calendario = Calendar.getInstance();
        this.cargarListaTotal();

        Bundle bundleRegistroDia = this.getIntent().getExtras();
        posDia = bundleRegistroDia.getInt("PosDia");
        calendario.setTime(listaTotal.getListaDiaPos(posDia).getFecha_dia());
        gastoHoy.setText(String.format("%.2f €",listaTotal.getDia(calendario).getGastoDia()));
        this.setProgresosDia();
        articulosLayoutManager = new LinearLayoutManager(this);
        ListaArticulos.setLayoutManager(articulosLayoutManager);
        final List<Articulo> articulos = listaTotal.getDia(calendario).getListaArticulos();
        articulosAdapter = new ArticuloAdapter(articulos);
        ListaArticulos.setAdapter(articulosAdapter);
        ((ArticuloAdapter) articulosAdapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDayEditar = new Intent(dayActivity.this, editarAddArticuloActivity.class);
                Bundle bundleDayEditar = new Bundle();

                bundleDayEditar.putInt("AñadirEditar",0);
                bundleDayEditar.putInt("PosDia",posDia);
                bundleDayEditar.putInt("PosArticulo",ListaArticulos.getChildPosition(v));
                intentDayEditar.putExtras(bundleDayEditar);
                startActivity(intentDayEditar);
            }
        });
    }

    public void cargarListaTotal(){
        SharedPreferences sharedPreferences = getSharedPreferences("ListaTotal", Context.MODE_PRIVATE);
        String ListaTotalJSON = sharedPreferences.getString("ListaCompra","");
        Gson gson = new Gson();
        listaTotal = gson.fromJson(ListaTotalJSON,ListaTotal.class);
    }

    public void setProgresosDia(){
        ListaDeUnDia listaDeUnDiaHoy = listaTotal.getDia(calendario);
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
