package com.example.app_dietas.ui.ventana_principal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.app_dietas.Dietas.ComidaProgreso;
import com.example.app_dietas.Dietas.DietaProgreso;
import com.example.app_dietas.Dietas.DietaUsuario;
import com.example.app_dietas.R;
import com.example.app_dietas.ui.dieta_de_hoy.DietaDeHoyFragment;
import com.example.app_dietas.ui.info_Nutricional_Comida.InfoNutricionalComida;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ventana_principal extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        //setContentView(R.layout.ventana_principal);


        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_inicio,
            R.id.nav_progreso, R.id.nav_calendario, R.id.nav_dieta_de_hoy, R.id.nav_info_nutricional,R.id.nav_cerrarSesion)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void mostrarDietaDeHoy(View view){

        if(view.getId() == R.id.progreso_del_dia_bar){
            Fragment nuevoFragment = new DietaDeHoyFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.drawer_layout,nuevoFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


    public void Boton_Pulsado_CalendarClick(View view){

        SharedPreferences sharedPref_ = getSharedPreferences("TextBotones",Context.MODE_PRIVATE);

        String TipoComida = "Desayuno";
        if(view.getId() == R.id.Desayuno)
            TipoComida = "Desayuno";
        else if(view.getId() == R.id.Almuerzo)
            TipoComida = "Almuerzo";
        else if(view.getId() == R.id.Merienda)
            TipoComida = "Merienda";
        else if(view.getId() == R.id.Cena)
            TipoComida = "Cena";

        String textMostrar = sharedPref_.getString(TipoComida,"");
        Toast.makeText(this,textMostrar,Toast.LENGTH_LONG).show();

        Intent intentCalendarioInfoComida = new Intent(ventana_principal.this, InfoNutricionalComida.class);
        Bundle bundleCalendarioInfoComida = new Bundle();
        bundleCalendarioInfoComida.putString("TipoComida",TipoComida);
        intentCalendarioInfoComida.putExtras(bundleCalendarioInfoComida);
        startActivity(intentCalendarioInfoComida);

    }

    public void Boton_Pulsado_DietaDelDia(View view){

        String TipoComida = "Desayuno";
        int ind_comida = 0;
        if(view.getId() == R.id.desayuno_dieta_dia){
            TipoComida = "Desayuno";
            ind_comida = 0;
        }
        else if(view.getId() == R.id.almuerzo_dieta_dia){
            TipoComida = "Almuerzo";
            ind_comida = 1;
        }
        else if(view.getId() == R.id.merienda_dieta_dia){
            TipoComida = "Merienda";
            ind_comida = 2;
        }
        else if(view.getId() == R.id.cena_dieta_dia){
            TipoComida = "Cena";
            ind_comida = 3;
        }

        SharedPreferences sharedPref_ = getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
        String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
        Gson gson = new Gson();
        DietaUsuario DietaDelUsuario = gson.fromJson(DietasDelUsuarioJSON,DietaUsuario.class);
        DietaProgreso Progreso = DietaDelUsuario.getProgresoDeDieta();
        ComidaProgreso[] progresoDeDias = Progreso.getComidasPorDia();


        Calendar fecha_hoy = Calendar.getInstance();
        Calendar calendario_progreso = Calendar.getInstance();
        boolean fin_bucle = false;
        int ind = 0;

        do{
            calendario_progreso.setTime(progresoDeDias[ind].getFecha());
            Log.i("DietaHoyFecha",calendario_progreso.getTime().toString());

            if(calendario_progreso.get(Calendar.DAY_OF_MONTH) == fecha_hoy.get(Calendar.DAY_OF_MONTH) && calendario_progreso.get(Calendar.MONTH) == fecha_hoy.get(Calendar.MONTH) && calendario_progreso.get(Calendar.YEAR) == fecha_hoy.get(Calendar.YEAR)){
                fin_bucle = true;
                int[] num_Alimentos = progresoDeDias[ind].getNum_alimentos();
                boolean[][] confirmados = progresoDeDias[ind].getConfirmados();
                int confirmado_sum = 0;

                for(int i = 0; i < num_Alimentos[ind_comida]; i++){
                    if(confirmados[ind_comida][i] == true)
                        confirmado_sum++;
                }

                Toast.makeText(this,"Confirmados "+confirmado_sum+" de "+num_Alimentos[ind_comida]+" Alimentos",Toast.LENGTH_LONG).show();

            }

            ind++;
        }while(fin_bucle==false && ind < Progreso.getNum_dias_dietas_llevados());


        sharedPref_ = getSharedPreferences("Fecha Seleccionada", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPref_.edit();
        String fecha_a_guardar = fecha_hoy.get(Calendar.DAY_OF_MONTH)+"/"+(fecha_hoy.get(Calendar.MONTH)+1)+"/"+fecha_hoy.get(Calendar.YEAR);
        Log.i("Fecha seleccionada__",fecha_a_guardar);
        editor.putString("FechaSeleccionada",fecha_a_guardar);
        editor.commit();

        Intent intentDietaDeHoyInfoComida = new Intent(ventana_principal.this, InfoNutricionalComida.class);
        Bundle bundleDietaDeHoyInfoComida = new Bundle();
        bundleDietaDeHoyInfoComida.putString("TipoComida",TipoComida);
        intentDietaDeHoyInfoComida.putExtras(bundleDietaDeHoyInfoComida);
        startActivity(intentDietaDeHoyInfoComida);


    }

}
