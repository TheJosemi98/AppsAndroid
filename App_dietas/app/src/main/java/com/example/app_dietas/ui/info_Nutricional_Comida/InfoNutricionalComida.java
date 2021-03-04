package com.example.app_dietas.ui.info_Nutricional_Comida;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_dietas.Dietas.Alimento;
import com.example.app_dietas.Dietas.ComidaProgreso;
import com.example.app_dietas.Dietas.DietaProgreso;
import com.example.app_dietas.Dietas.DietaUsuario;
import com.example.app_dietas.R;
import com.example.app_dietas.logica.AlimentoAdapter;
import com.example.app_dietas.ui.InicioSesion.CargaPantalla;
import com.example.app_dietas.ui.info_nutricional_alimento.InfoNutricionalAlimento;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InfoNutricionalComida extends AppCompatActivity {

    private PieChart Diagrama_sectores_comida;
    private TextView CaloriasC;
    private TextView HidratosC;
    private TextView GrasasC;
    private TextView ProteinasC;
    public RecyclerView ListaComidas;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager LayoutManager;

    private String TipoComida;
    private Date fechaSeleccionada;


    @Override
    protected void onResume(){
        super.onResume();
        this.actualizarActivity();

    }

    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.info_nutricional_comida);

        Diagrama_sectores_comida = findViewById(R.id.piechart_Comida);
        CaloriasC = findViewById(R.id.Calorias_C);
        HidratosC = findViewById(R.id.HidratosDeCaborno_C);
        GrasasC = findViewById(R.id.Grasas_C);
        ProteinasC = findViewById(R.id.Proteinas_C);
        ListaComidas = findViewById(R.id.ListaComidas);

        SharedPreferences sharedPref_ = getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaUsuario DietasDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);

        Bundle bundleInfoNutricionalComida = this.getIntent().getExtras();
        TipoComida = new String();
        fechaSeleccionada = new Date();


        if(bundleInfoNutricionalComida!= null){

            sharedPref_ = getSharedPreferences("Fecha Seleccionada", Context.MODE_PRIVATE);
            String fecha_seleccionadaS = sharedPref_.getString("FechaSeleccionada","");
            Log.i("Fecha_que_se_lee",fecha_seleccionadaS);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                fechaSeleccionada = format.parse(fecha_seleccionadaS);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TipoComida = bundleInfoNutricionalComida.getString("TipoComida");
            this.setInformacion(DietasDelUsuario,TipoComida,fechaSeleccionada);

            sharedPref_ = getSharedPreferences("InfoNutrAlimentosPos", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref_.edit();
            editor.putString("FechaSeleccionada",fecha_seleccionadaS);
            editor.putString("TipoComida",TipoComida);
            editor.commit();

        }

        //Use this setting to improve performance if you know that changes
        // int ccontent do not change the layout size of the RecyclerView
        ListaComidas.setHasFixedSize(true);

        // Use a linear Layout
        LayoutManager = new LinearLayoutManager(this);
        ListaComidas.setLayoutManager(LayoutManager);

        //Specify an adapter
        List<Alimento> ListaAlimento = DietasDelUsuario.getListaAlimentos(TipoComida,fechaSeleccionada);
        mAdapter = new AlimentoAdapter(ListaAlimento);

        ListaComidas.setAdapter(mAdapter);

        ((AlimentoAdapter) mAdapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DemoRecView", "Pulsado el elemento " + ListaComidas.getChildPosition(v));
                Intent intentComida_Alimento = new Intent(InfoNutricionalComida.this, InfoNutricionalAlimento.class);
                SharedPreferences sharedPref_ = getSharedPreferences("InfoNutrAlimentosPos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref_.edit();
                editor.putInt("IndAlimento",ListaComidas.getChildPosition(v));
                editor.commit();

                startActivity(intentComida_Alimento);

            }
        });



    }

    public void setInformacion(DietaUsuario DietaDelUsuario, String TipoComida, Date fechaSeleccionada){

        DietaProgreso Progreso = DietaDelUsuario.getProgresoDeDieta();
        ComidaProgreso[] progresoDeDias = Progreso.getComidasPorDia();


        int calorias_ = 0, hidratos_ = 0, grasas_ = 0, proteinas_ = 0;
        List<PieEntry> pieEntradas = new ArrayList<>();
        ArrayList<String> TipoDeAlimentos = new ArrayList();
        ArrayList<Integer> num_TipoDeAlimentos = new ArrayList();
        int ind_comida = this.getIndComida(TipoComida);



        int ind = 0;
        boolean fin_bucle = false;
        do{
            if(progresoDeDias[ind].getFecha().getDate() == fechaSeleccionada.getDate() && progresoDeDias[ind].getFecha().getMonth() == fechaSeleccionada.getMonth() && progresoDeDias[ind].getFecha().getYear() == fechaSeleccionada.getYear()){
                fin_bucle = true;
                int[] num_alimentos = progresoDeDias[ind].getNum_alimentos();
                Alimento[][] AlimentosDeComida = progresoDeDias[ind].getComidasDelDia();

                for(int j = 0; j < num_alimentos[ind_comida]; j++){

                    if(j==0){
                        TipoDeAlimentos.add(AlimentosDeComida[ind_comida][j].getGrupoAlimenticio());
                        num_TipoDeAlimentos.add(1);
                        calorias_ += Math.round(AlimentosDeComida[ind_comida][j].getEnergía()*(AlimentosDeComida[ind_comida][j].getCantidadNumerica()/100.0));
                        hidratos_ += Math.round(AlimentosDeComida[ind_comida][j].getHidratosCarbono()*(AlimentosDeComida[ind_comida][j].getCantidadNumerica()/100.0));
                        grasas_ += Math.round(AlimentosDeComida[ind_comida][j].getGrasas()*(AlimentosDeComida[ind_comida][j].getCantidadNumerica()/100.0));
                        proteinas_ += Math.round(AlimentosDeComida[ind_comida][j].getProteinas()*(AlimentosDeComida[ind_comida][j].getCantidadNumerica()/100.0));
                    }
                    else{

                        boolean encontrado = false;
                        for(int z = 0;  z < TipoDeAlimentos.size(); z++){
                            if(TipoDeAlimentos.get(z).equals(AlimentosDeComida[ind_comida][j].getGrupoAlimenticio())){
                                int num_aux = num_TipoDeAlimentos.get(z)+1;
                                num_TipoDeAlimentos.set(z,num_aux);
                                encontrado = true;
                            }
                        }
                        if(!encontrado){
                            TipoDeAlimentos.add(AlimentosDeComida[ind_comida][j].getGrupoAlimenticio());
                            num_TipoDeAlimentos.add(1);
                        }

                        calorias_ += AlimentosDeComida[ind_comida][j].getEnergía()*(AlimentosDeComida[ind_comida][j].getCantidadNumerica()/100);
                        hidratos_ += AlimentosDeComida[ind_comida][j].getHidratosCarbono()*(AlimentosDeComida[ind_comida][j].getCantidadNumerica()/100);
                        grasas_ += AlimentosDeComida[ind_comida][j].getGrasas()*(AlimentosDeComida[ind_comida][j].getCantidadNumerica()/100);
                        proteinas_ += AlimentosDeComida[ind_comida][j].getProteinas()*(AlimentosDeComida[ind_comida][j].getCantidadNumerica()/100);

                    }
                }
            }
            ind++;
        }while(fin_bucle==false && ind < Progreso.getNum_dias_dietas_llevados());

        CaloriasC.setText(calorias_+" Kcal");
        HidratosC.setText(hidratos_+" g");
        GrasasC.setText(grasas_+" g");
        ProteinasC.setText(proteinas_+" g");

        for(int i = 0; i < TipoDeAlimentos.size(); i++)
            pieEntradas.add(new PieEntry(num_TipoDeAlimentos.get(i),TipoDeAlimentos.get(i)));

        PieDataSet dataSet = new PieDataSet(pieEntradas,"Numero de alimentos");
        PieData data = new PieData(dataSet);
        Diagrama_sectores_comida.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);


    }

    public int getIndComida(String TipoComida){
        int ind_comida = 0;

        if(TipoComida.equals("Desayuno"))
            ind_comida = 0;
        else if(TipoComida.equals("Almuerzo"))
            ind_comida = 1;
        else if(TipoComida.equals("Merienda"))
            ind_comida = 2;
        else if(TipoComida.equals("Cena"))
            ind_comida = 3;

        return ind_comida;


    }

    public void actualizarActivity(){

        SharedPreferences sharedPref_ = getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaUsuario DietasDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);

        this.setInformacion(DietasDelUsuario,TipoComida,fechaSeleccionada);

        //Use this setting to improve performance if you know that changes
        // int ccontent do not change the layout size of the RecyclerView
        ListaComidas.setHasFixedSize(true);

        // Use a linear Layout
        LayoutManager = new LinearLayoutManager(this);
        ListaComidas.setLayoutManager(LayoutManager);

        //Specify an adapter
        List<Alimento> ListaAlimento = DietasDelUsuario.getListaAlimentos(TipoComida,fechaSeleccionada);
        mAdapter = new AlimentoAdapter(ListaAlimento);

        ListaComidas.setAdapter(mAdapter);

        ((AlimentoAdapter) mAdapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DemoRecView", "Pulsado el elemento " + ListaComidas.getChildPosition(v));
                Intent intentComida_Alimento = new Intent(InfoNutricionalComida.this, InfoNutricionalAlimento.class);
                SharedPreferences sharedPref_ = getSharedPreferences("InfoNutrAlimentosPos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref_.edit();
                editor.putInt("IndAlimento",ListaComidas.getChildPosition(v));
                editor.commit();

                startActivity(intentComida_Alimento);

            }
        });

    }


}
