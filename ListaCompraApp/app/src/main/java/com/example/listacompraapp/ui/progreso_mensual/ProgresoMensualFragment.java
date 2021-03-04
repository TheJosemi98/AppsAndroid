package com.example.listacompraapp.ui.progreso_mensual;

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

import com.example.listacompraapp.Listas.ListaTotal;
import com.example.listacompraapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProgresoMensualFragment extends Fragment {

    private ListaTotal listaTotal;
    private Calendar calendario;
    private PieChart pieChart;
    private BarChart barChart;
    private Button botonAnterior;
    private Button botonPosterior;
    private TextView gastoTotal;
    private TextView gastoDiaText;
    private TextView gastoMes;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progreso_mensual, container, false);

        pieChart = root.findViewById(R.id.piechart_categoria_mes);
        barChart = root.findViewById(R.id.barchart_graficas);
        botonAnterior  = root.findViewById(R.id.button_mesAnt);
        botonPosterior = root.findViewById(R.id.button_mesPos);
        gastoTotal = root.findViewById(R.id.Gasto_total_textEdit);
        gastoDiaText = root.findViewById(R.id.Gasto_por_dia_text);
        gastoMes = root.findViewById(R.id.Gasto_mes_textEdit);
        calendario = Calendar.getInstance();

        String Mes;
        if((calendario.get(Calendar.MONTH)+1) < 10)
            Mes = "0"+(calendario.get(Calendar.MONTH)+1);
        else
            Mes = ""+(calendario.get(Calendar.MONTH)+1);
        gastoDiaText.setText("Gasto por dia: "+Mes+"/"+calendario.get(Calendar.YEAR));

        this.cargarListaTotal();
        this.setGraficasYProgreso();

        botonAnterior.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calendario.add(Calendar.MONTH,-1);
            String Mes;
            if((calendario.get(Calendar.MONTH)+1) < 10)
                Mes = "0"+(calendario.get(Calendar.MONTH)+1);
            else
                Mes = ""+(calendario.get(Calendar.MONTH)+1);
            gastoDiaText.setText("Gasto por dia: "+Mes+"/"+calendario.get(Calendar.YEAR));

            this.setGraficasYProgreso();
        }

            public void setGraficasYProgreso(){

                List<BarEntry> barEntradas = new ArrayList<>();
                List<PieEntry> pieEntradas = new ArrayList<>();

                double GastoMes = listaTotal.getGastoTotalDelMes(calendario);
                gastoMes.setText(String.format("%.2f",GastoMes)+" €");
                double GastoTotal = listaTotal.getGastoTotal();
                gastoTotal.setText(String.format("%.2f",GastoTotal)+" €");

                List<String> categoriasMes = listaTotal.getCategoriaMes(calendario);
                int[] numVeces = new int[categoriasMes.size()];
                for(int i  = 0; i < categoriasMes.size(); i++)
                    numVeces[i] = listaTotal.getNumVeces(calendario,categoriasMes.get(i));

                for(int i = 0; i < categoriasMes.size(); i++)
                    pieEntradas.add(new PieEntry(numVeces[i],categoriasMes.get(i)) );

                PieDataSet dataSet = new PieDataSet(pieEntradas,"Categorias");
                PieData data = new PieData(dataSet);
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieChart.setData(data);
                pieChart.notifyDataSetChanged();
                pieChart.invalidate();

                double[] gastoDiarioMes = listaTotal.getGastoDiarioDelMes(calendario);
                String[] DiasMes = new String[31];

                if((calendario.get(Calendar.MONTH)+1) == 2 && calendario.get(Calendar.YEAR)%4 == 0 )
                    DiasMes = new String[29];
                else if((calendario.get(Calendar.MONTH)+1) == 2 && calendario.get(Calendar.YEAR)%4 != 0)
                    DiasMes = new String[28];
                else if( (calendario.get(Calendar.MONTH)+1) == 1 || (calendario.get(Calendar.MONTH)+1) == 3 || (calendario.get(Calendar.MONTH)+1) == 5 || (calendario.get(Calendar.MONTH)+1) == 7 || (calendario.get(Calendar.MONTH)+1) == 8 || (calendario.get(Calendar.MONTH)+1) == 10 || (calendario.get(Calendar.MONTH)+1) == 12 )
                    DiasMes = new String[31];
                else if( (calendario.get(Calendar.MONTH)+1) == 4 || (calendario.get(Calendar.MONTH)+1) == 6 || (calendario.get(Calendar.MONTH)+1) == 9 || (calendario.get(Calendar.MONTH)+1) == 11)
                    DiasMes = new String[30];

                for(int i = 0; i < DiasMes.length; i++)
                    DiasMes[i] = String.valueOf(i+1);

                for(int i = 0; i < gastoDiarioMes.length; i++)
                    barEntradas.add( new BarEntry(i, (float) gastoDiarioMes[i]) );

                final ArrayList<String> label = new ArrayList<>();
                for(int i  = 0; i < DiasMes.length; i++)
                    label.add(DiasMes[i]);

                Log.i("LengthBarEntradas",""+barEntradas.size());
                Log.i("LengthBarLabel",""+label.size());
                BarDataSet barDataSet = new BarDataSet(barEntradas,"Dias");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                BarData dataBar = new BarData(barDataSet);
                dataBar.setBarWidth(0.5f);


                XAxis xAxis = barChart.getXAxis();
                Log.i("AxisMaxAnt",""+xAxis.getAxisMaximum());
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float v, AxisBase axisBase) {
                        Log.i("StringLabel",""+label.size());
                        Log.i("V",""+v);
                        if(v < label.size())
                            return label.get((int) v);
                        else
                            return null;
                    }
                });

                barChart.setData(dataBar);
                barChart.animateY(2000);
                barChart.setFitBars(true);
                barChart.invalidate();
            }

        });

        botonPosterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendario.add(Calendar.MONTH,1);

                String Mes;
                if((calendario.get(Calendar.MONTH)+1) < 10)
                    Mes = "0"+(calendario.get(Calendar.MONTH)+1);
                else
                    Mes = ""+(calendario.get(Calendar.MONTH)+1);
                gastoDiaText.setText("Gasto por dia: "+Mes+"/"+calendario.get(Calendar.YEAR));

                this.setGraficasYProgreso();
            }

            public void setGraficasYProgreso(){

                List<BarEntry> barEntradas = new ArrayList<>();
                List<PieEntry> pieEntradas = new ArrayList<>();

                double GastoMes = listaTotal.getGastoTotalDelMes(calendario);
                gastoMes.setText(String.format("%.2f",GastoMes)+" €");
                double GastoTotal = listaTotal.getGastoTotal();
                gastoTotal.setText(String.format("%.2f",GastoTotal)+" €");

                List<String> categoriasMes = listaTotal.getCategoriaMes(calendario);
                int[] numVeces = new int[categoriasMes.size()];
                for(int i  = 0; i < categoriasMes.size(); i++)
                    numVeces[i] = listaTotal.getNumVeces(calendario,categoriasMes.get(i));

                for(int i = 0; i < categoriasMes.size(); i++)
                    pieEntradas.add(new PieEntry(numVeces[i],categoriasMes.get(i)) );

                PieDataSet dataSet = new PieDataSet(pieEntradas,"Categorias");
                PieData data = new PieData(dataSet);
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieChart.setData(data);
                pieChart.notifyDataSetChanged();
                pieChart.invalidate();

                double[] gastoDiarioMes = listaTotal.getGastoDiarioDelMes(calendario);
                String[] DiasMes = new String[31];

                if((calendario.get(Calendar.MONTH)+1) == 2 && calendario.get(Calendar.YEAR)%4 == 0 )
                    DiasMes = new String[29];
                else if((calendario.get(Calendar.MONTH)+1) == 2 && calendario.get(Calendar.YEAR)%4 != 0)
                    DiasMes = new String[28];
                else if( (calendario.get(Calendar.MONTH)+1) == 1 || (calendario.get(Calendar.MONTH)+1) == 3 || (calendario.get(Calendar.MONTH)+1) == 5 || (calendario.get(Calendar.MONTH)+1) == 7 || (calendario.get(Calendar.MONTH)+1) == 8 || (calendario.get(Calendar.MONTH)+1) == 10 || (calendario.get(Calendar.MONTH)+1) == 12 )
                    DiasMes = new String[31];
                else if( (calendario.get(Calendar.MONTH)+1) == 4 || (calendario.get(Calendar.MONTH)+1) == 6 || (calendario.get(Calendar.MONTH)+1) == 9 || (calendario.get(Calendar.MONTH)+1) == 11)
                    DiasMes = new String[30];

                for(int i = 0; i < DiasMes.length; i++)
                    DiasMes[i] = String.valueOf(i+1);

                for(int i = 0; i < gastoDiarioMes.length; i++)
                    barEntradas.add( new BarEntry(i, (float) gastoDiarioMes[i]) );

                final ArrayList<String> label = new ArrayList<>();
                for(int i  = 0; i < DiasMes.length; i++)
                    label.add(DiasMes[i]);

                Log.i("LengthBarEntradas",""+barEntradas.size());

                BarDataSet barDataSet = new BarDataSet(barEntradas,"Dias");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                BarData dataBar = new BarData(barDataSet);
                dataBar.setBarWidth(0.5f);


                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float v, AxisBase axisBase) {
                        if(v < label.size())
                            return label.get((int) v);
                        else
                            return null;
                    }
                });

                barChart.setData(dataBar);
                barChart.animateY(2000);
                barChart.setFitBars(true);
                barChart.invalidate();
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

    public void cambiarMes(View view){

        if(view.getId() == R.id.button_mesAnt)
            calendario.add(Calendar.MONTH,-1);
        else if(view.getId() == R.id.button_mesPos)
            calendario.add(Calendar.MONTH,1);

        this.setGraficasYProgreso();
    }


    public void setGraficasYProgreso(){

        List<BarEntry> barEntradas = new ArrayList<>();
        List<PieEntry> pieEntradas = new ArrayList<>();

        double GastoMes = listaTotal.getGastoTotalDelMes(calendario);
        gastoMes.setText(String.format("%.2f",GastoMes)+" €");
        double GastoTotal = listaTotal.getGastoTotal();
        gastoTotal.setText(String.format("%.2f",GastoTotal)+" €");

        List<String> categoriasMes = listaTotal.getCategoriaMes(calendario);
        int[] numVeces = new int[categoriasMes.size()];
        for(int i  = 0; i < categoriasMes.size(); i++)
            numVeces[i] = listaTotal.getNumVeces(calendario,categoriasMes.get(i));

        for(int i = 0; i < categoriasMes.size(); i++)
            pieEntradas.add(new PieEntry(numVeces[i],categoriasMes.get(i)) );

        PieDataSet dataSet = new PieDataSet(pieEntradas,"Categorias");
        PieData data = new PieData(dataSet);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(data);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

        double[] gastoDiarioMes = listaTotal.getGastoDiarioDelMes(calendario);
        String[] DiasMes = new String[31];

        if((calendario.get(Calendar.MONTH)+1) == 2 && calendario.get(Calendar.YEAR)%4 == 0 )
            DiasMes = new String[29];
        else if((calendario.get(Calendar.MONTH)+1) == 2 && calendario.get(Calendar.YEAR)%4 != 0)
            DiasMes = new String[28];
        else if( (calendario.get(Calendar.MONTH)+1) == 1 || (calendario.get(Calendar.MONTH)+1) == 3 || (calendario.get(Calendar.MONTH)+1) == 5 || (calendario.get(Calendar.MONTH)+1) == 7 || (calendario.get(Calendar.MONTH)+1) == 8 || (calendario.get(Calendar.MONTH)+1) == 10 || (calendario.get(Calendar.MONTH)+1) == 12 )
            DiasMes = new String[31];
        else if( (calendario.get(Calendar.MONTH)+1) == 4 || (calendario.get(Calendar.MONTH)+1) == 6 || (calendario.get(Calendar.MONTH)+1) == 9 || (calendario.get(Calendar.MONTH)+1) == 11)
            DiasMes = new String[30];

        for(int i = 0; i < DiasMes.length; i++)
            DiasMes[i] = String.valueOf(i+1);

        for(int i = 0; i < gastoDiarioMes.length; i++)
            barEntradas.add( new BarEntry(i, (float) gastoDiarioMes[i]) );

        final ArrayList<String> label = new ArrayList<>();
        for(int i  = 0; i < DiasMes.length; i++)
            label.add(DiasMes[i]);

        Log.i("LengthBar",""+barEntradas.size());
        Log.i("LengthLabel",""+label.size());
        BarDataSet barDataSet = new BarDataSet(barEntradas,"Dias");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData dataBar = new BarData(barDataSet);
        dataBar.setBarWidth(0.5f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return label.get((int) v);
            }
        });

        barChart.setData(dataBar);
        barChart.animateY(2000);
        barChart.setFitBars(true);
        barChart.invalidate();

    }


}
