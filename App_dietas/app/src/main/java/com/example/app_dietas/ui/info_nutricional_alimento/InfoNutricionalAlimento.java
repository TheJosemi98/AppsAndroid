package com.example.app_dietas.ui.info_nutricional_alimento;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.app_dietas.ui.InicioSesion.InicioSesion;
import com.example.app_dietas.ui.ListaEquivalentes.ListaEquivalentes;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InfoNutricionalAlimento extends AppCompatActivity {

    private PieChart Diagrama_sectores_alimento;
    private TextView NombreAlimento;
    private TextView CaloriasA;
    public Button Boton_Si;
    public Button Boton_No;
    public Button Boton_Equivalente;
    public Button Boton_Cantidad;
    public EditText CantidadTexto_;

    private DietaUsuario DietaDelUsuario;
    private Date fechaSeleccionada;
    private int Ind_Comida;
    private int Ind_Alimento;


    @Override
    protected void onResume(){
        super.onResume();
        this.actualizarActividad();
    }

    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.info_nutricional_alimento);

        Diagrama_sectores_alimento = findViewById(R.id.piechart_alimento);
        CaloriasA = findViewById(R.id.Calorias_);
        Boton_Si = findViewById(R.id.Si_button);
        Boton_No = findViewById(R.id.No_button);
        Boton_Cantidad = findViewById(R.id.boton_cantidadConfirmar);
        Boton_Equivalente = findViewById(R.id.Boton_equivalente);
        CantidadTexto_ = findViewById(R.id.cantidad_edit);
        NombreAlimento = findViewById(R.id.Info_nutricional_text);

        this.actualizarActividad();

    }

    public void setInformacion(){

        DietaProgreso Progreso = DietaDelUsuario.getProgresoDeDieta();
        ComidaProgreso[] progresoDeDias = Progreso.getComidasPorDia();


        double calorias_ = 0, hidratos_ = 0, grasas_ = 0, proteinas_ = 0;
        List<PieEntry> pieEntradas = new ArrayList<>();


        int ind = 0;
        boolean fin_bucle = false;
        do{
            if(progresoDeDias[ind].getFecha().getDate() == fechaSeleccionada.getDate() && progresoDeDias[ind].getFecha().getMonth() == fechaSeleccionada.getMonth() && progresoDeDias[ind].getFecha().getYear() == fechaSeleccionada.getYear()){
                Alimento[][] AlimentosDeComida = progresoDeDias[ind].getComidasDelDia();
                NombreAlimento.setText(AlimentosDeComida[Ind_Comida][Ind_Alimento].getNombre());
                Log.i("NombreAlimento",AlimentosDeComida[Ind_Comida][Ind_Alimento].getNombre());
                calorias_ = AlimentosDeComida[Ind_Comida][Ind_Alimento].getEnergía()*(AlimentosDeComida[Ind_Comida][Ind_Alimento].getCantidadNumerica()/100.0);
                hidratos_ = AlimentosDeComida[Ind_Comida][Ind_Alimento].getHidratosCarbono()*(AlimentosDeComida[Ind_Comida][Ind_Alimento].getCantidadNumerica()/100.0);
                grasas_ = AlimentosDeComida[Ind_Comida][Ind_Alimento].getGrasas()*(AlimentosDeComida[Ind_Comida][Ind_Alimento].getCantidadNumerica()/100.0);
                proteinas_ = AlimentosDeComida[Ind_Comida][Ind_Alimento].getProteinas()*(AlimentosDeComida[Ind_Comida][Ind_Alimento].getCantidadNumerica()/100.0);
                fin_bucle = true;
            }
            else
                ind++;
        }while(fin_bucle==false && ind < Progreso.getNum_dias_dietas_llevados());

        CaloriasA.setText(calorias_+" Kcal");
        Log.i("Calorias",String.valueOf(calorias_));
        Log.i("HidratosDeCarbono",String.valueOf(Math.round(hidratos_)));
        Log.i("Grasas",String.valueOf(Math.round(grasas_)));
        Log.i("Proteinas",String.valueOf(Math.round(proteinas_)));

        //pieEntradas.add(new PieEntry(num_TipoDeAlimentos.get(i),TipoDeAlimentos.get(i)));
        pieEntradas.add(new PieEntry(Math.round(hidratos_),"Hidratos de Carbono"));
        pieEntradas.add(new PieEntry(Math.round(grasas_),"Grasas"));
        pieEntradas.add(new PieEntry(Math.round(proteinas_),"Proteinas"));

        PieDataSet dataSet = new PieDataSet(pieEntradas,"Informacion Nutricional");
        PieData data = new PieData(dataSet);
        Diagrama_sectores_alimento.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

    }

    public void confirmarDesconfirmarAlimento(boolean confirmar){

        DietaProgreso Progreso = DietaDelUsuario.getProgresoDeDieta();
        ComidaProgreso[] progresoDeDias = Progreso.getComidasPorDia();


        int ind = 0;
        boolean fin_bucle = false;
        do{
            if(progresoDeDias[ind].getFecha().getDate() == fechaSeleccionada.getDate() && progresoDeDias[ind].getFecha().getMonth() == fechaSeleccionada.getMonth() && progresoDeDias[ind].getFecha().getYear() == fechaSeleccionada.getYear()){
                progresoDeDias[ind].confirmarDesconfirmarComida(confirmar,Ind_Comida,Ind_Alimento);
                fin_bucle = true;
            }
            else
                ind++;
        }while(fin_bucle==false && ind < Progreso.getNum_dias_dietas_llevados());

        Progreso.setComidasPorDia(progresoDeDias);
        DietaDelUsuario.setProgresoDeDieta(Progreso);
        this.actualizarDieta();

        if(confirmar)
            Toast.makeText(this,"Alimento confirmado",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Alimento desconfirmado",Toast.LENGTH_LONG).show();

    }

    public void actualizarDieta(){
        SharedPreferences sharedPref_ = getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPref_.edit();
        Gson gson = new Gson();
        String jsonDietaDelUsuario = gson.toJson(DietaDelUsuario);
        editor.putString("DietaDelUsuario",jsonDietaDelUsuario);
        editor.commit();

    }


    public int IndTipoComida(String TipoComida){

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

    public void BotonPulsadoInfoNutricionalAlimento(View view){

        if(view.getId() == R.id.Si_button)
            this.confirmarDesconfirmarAlimento(true);
        else if(view.getId() == R.id.No_button)
            this.confirmarDesconfirmarAlimento(false);
        else if(view.getId() == R.id.boton_cantidadConfirmar){

            if(!CantidadTexto_.getText().toString().equals("")){
                DietaProgreso Progreso = DietaDelUsuario.getProgresoDeDieta();
                ComidaProgreso[] progresoDeDias = Progreso.getComidasPorDia();
                List<PieEntry> pieEntradas = new ArrayList<>();
                double calorias_ = 0, hidratos_ = 0, grasas_ = 0, proteinas_ = 0;
                Alimento[][] AlimentosDeComida = new Alimento[4][10];

                Log.i("FechaSeleccionada",fechaSeleccionada.toString());
                int ind = 0;
                boolean fin_bucle = false;
                do{
                    Log.i("FechaBuscadas",progresoDeDias[ind].getFecha().toString());
                    if(progresoDeDias[ind].getFecha().getDate() == fechaSeleccionada.getDate() && progresoDeDias[ind].getFecha().getMonth() == fechaSeleccionada.getMonth() && progresoDeDias[ind].getFecha().getYear() == fechaSeleccionada.getYear()) {
                        AlimentosDeComida = progresoDeDias[ind].getComidasDelDia();
                        AlimentosDeComida[Ind_Comida][Ind_Alimento].setCantidadNumerica(Integer.parseInt(CantidadTexto_.getText().toString()));
                        calorias_ = AlimentosDeComida[Ind_Comida][Ind_Alimento].getEnergía() * (AlimentosDeComida[Ind_Comida][Ind_Alimento].getCantidadNumerica() / 100.0);
                        hidratos_ = AlimentosDeComida[Ind_Comida][Ind_Alimento].getHidratosCarbono() * (AlimentosDeComida[Ind_Comida][Ind_Alimento].getCantidadNumerica() / 100.0);
                        grasas_ = AlimentosDeComida[Ind_Comida][Ind_Alimento].getGrasas() * (AlimentosDeComida[Ind_Comida][Ind_Alimento].getCantidadNumerica() / 100.0);
                        proteinas_ = AlimentosDeComida[Ind_Comida][Ind_Alimento].getProteinas() * (AlimentosDeComida[Ind_Comida][Ind_Alimento].getCantidadNumerica() / 100.0);
                        fin_bucle = true;
                    }
                    else
                        ind++;
                }while(fin_bucle==false && ind < Progreso.getNum_dias_dietas_llevados());


                CaloriasA.setText(calorias_+" Kcal");

                pieEntradas.add(new PieEntry( (int) hidratos_,"Hidratos de Carbono"));
                pieEntradas.add(new PieEntry( (int) grasas_,"Grasas"));
                pieEntradas.add(new PieEntry( (int) proteinas_,"Proteinas"));

                PieDataSet dataSet = new PieDataSet(pieEntradas,"Informacion Nutricional");
                PieData data = new PieData(dataSet);
                Diagrama_sectores_alimento.setData(data);
                Diagrama_sectores_alimento.notifyDataSetChanged();
                Diagrama_sectores_alimento.invalidate();
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                progresoDeDias[ind].setComidasDelDia(AlimentosDeComida);
                Progreso.setComidasPorDia(progresoDeDias);
                DietaDelUsuario.setProgresoDeDieta(Progreso);
                this.actualizarDieta();
            }

        }
        else if(view.getId() == R.id.Boton_equivalente){

            Calendar fechaSelecionadaCalendar = Calendar.getInstance();
            fechaSelecionadaCalendar.setTime(fechaSeleccionada);
            int IndDiaSemana = 0, PosComidaActual = 0;

            IndDiaSemana = fechaSelecionadaCalendar.get(Calendar.DAY_OF_WEEK);

            if(IndDiaSemana==1)
                IndDiaSemana=7;
            else
                IndDiaSemana--;

            PosComidaActual = IndDiaSemana - 1;

            String fechaSeleccionada_ = fechaSelecionadaCalendar.get(Calendar.DAY_OF_MONTH)+"/"+(fechaSelecionadaCalendar.get(Calendar.MONTH)+1)+"/"+fechaSelecionadaCalendar.get(Calendar.YEAR);

            if(DietaDelUsuario.getDietaActual().getComidaPorDia(PosComidaActual).getNum_equivalentes_Alimento(Ind_Comida,Ind_Alimento) == 0)
                Toast.makeText(this,"No hay equivalentes de este alimento",Toast.LENGTH_LONG).show();
            else{
                Intent intentAlimento_Equivalente = new Intent(InfoNutricionalAlimento.this, ListaEquivalentes.class);
                Bundle bundleAlimento_Equivalente = new Bundle();
                bundleAlimento_Equivalente.putInt("TipoComida",Ind_Comida);
                bundleAlimento_Equivalente.putInt("IndAlimento",Ind_Alimento);
                Log.i("FechaSeleccionada_",fechaSeleccionada_);
                bundleAlimento_Equivalente.putString("fechaSeleccionada",fechaSeleccionada_);
                Log.i("FechaEquivalenteWeek",""+fechaSelecionadaCalendar.get(Calendar.DAY_OF_WEEK));
                Log.i("FechaEquivalenteMonth",""+(fechaSelecionadaCalendar.get(Calendar.MONTH)+1));
                Log.i("FechaEquivalenteDay",""+fechaSelecionadaCalendar.get(Calendar.DAY_OF_MONTH));
                Log.i("FechaEquivalenteYear",""+fechaSelecionadaCalendar.get(Calendar.YEAR));
                intentAlimento_Equivalente.putExtras(bundleAlimento_Equivalente);
                startActivity(intentAlimento_Equivalente);
            }
        }

    }

    public void actualizarActividad(){

        SharedPreferences sharedPref_ = getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);

        String TipoComida;

        sharedPref_ = getSharedPreferences("InfoNutrAlimentosPos", Context.MODE_PRIVATE);

        String fechaSeleccionada_ = sharedPref_.getString("FechaSeleccionada","");
        TipoComida = sharedPref_.getString("TipoComida","");
        Ind_Comida = this.IndTipoComida(TipoComida);
        Ind_Alimento = sharedPref_.getInt("IndAlimento",0);

        Log.i("IndComidaAlimento",""+Ind_Comida);
        Log.i("IndAlimentoEnInfo",""+Ind_Alimento);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fechaSeleccionada = format.parse(fechaSeleccionada_);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("FechaInfoNutricionalA",""+fechaSeleccionada);

        this.setInformacion();

    }

}
