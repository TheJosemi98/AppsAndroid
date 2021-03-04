package com.example.listacompraapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.listacompraapp.Listas.ListaDeUnDia;
import com.example.listacompraapp.Listas.ListaTotal;
import com.example.listacompraapp.ui.editarAddArticulo.editarAddArticuloActivity;
import com.example.listacompraapp.ui.progreso.dayActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Calendar;

public class ListaCompraActivity extends AppCompatActivity {

    private ListaTotal listaTotal;
    private Calendar calendario;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        calendario = Calendar.getInstance();

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHoyAdd = new Intent(ListaCompraActivity.this, editarAddArticuloActivity.class);
                Bundle bundleHoyAdd = new Bundle();
                bundleHoyAdd.putInt("AñadirEditar",1);
                intentHoyAdd.putExtras(bundleHoyAdd);
                startActivity(intentHoyAdd);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("ListaTotal", Context.MODE_PRIVATE);
        String ListaTotalJSON = sharedPreferences.getString("ListaCompra","");
        this.checkPermisos();

        if(!ListaTotalJSON.equals("")){
            Log.i("ListaTotal","No es nulo");
            Gson gson = new Gson();
            listaTotal = gson.fromJson(ListaTotalJSON, ListaTotal.class);

            boolean nuevo = true;
            Calendar calendario_aux = Calendar.getInstance();
            for(int i = 0; i < listaTotal.getNum_dias(); i++){
                calendario_aux.setTime(listaTotal.getListaDiaPos(i).getFecha_dia());
                if(calendario.get(Calendar.DAY_OF_YEAR) == calendario_aux.get(Calendar.DAY_OF_YEAR) && calendario.get(Calendar.YEAR) == calendario_aux.get(Calendar.YEAR))
                    nuevo = false;
            }

            if(nuevo)
                listaTotal.addDia(new ListaDeUnDia(calendario.getTime()));
            Log.i("ListaTotal",ListaTotalJSON);

            listaTotal.calcularGastoTotal();
            this.guardarCambios();

        }
        else{
            Log.i("ListaTotal","Es nulo");
            listaTotal = new ListaTotal();
            this.guardarCambios();
        }


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_Hoy, R.id.navigation_Progreso, R.id.navigation_Progreso_mensual)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void checkPermisos(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int permissionCheckEscritura  = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionCheckLectura = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionCheckCalendario = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR);
            if(permissionCheckEscritura != PackageManager.PERMISSION_GRANTED && permissionCheckLectura != PackageManager.PERMISSION_GRANTED){
                Log.i("Mensaje_info_Storage_W","No hay permisos de escritura");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
                Log.i("Mensaje_info_Storage_R","No hay permisos de lectura");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 225);
            }
            else if(permissionCheckEscritura == PackageManager.PERMISSION_GRANTED && permissionCheckLectura != PackageManager.PERMISSION_GRANTED){
                Log.i("Mensaje_info_Storage_W","Hay permisos de escritura");
                Log.i("Mensaje_info_Storage_R","No hay permisos de lectura");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 225);
            }
            else if(permissionCheckEscritura != PackageManager.PERMISSION_GRANTED && permissionCheckLectura == PackageManager.PERMISSION_GRANTED){
                Log.i("Mensaje_info_Storage_R","Hay permisos de lectura");
                Log.i("Mensaje_info_Storage_W","No hay permisos de escritura");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
            }
            else{
                Log.i("Mensaje_info_Storage_R","Hay permisos de lectura");
                Log.i("Mensaje_info_Storage_W","Hay permisos de escritura");
            }

        }

    }

    public void guardarCambios(){
        SharedPreferences sharedPreferences = getSharedPreferences("ListaTotal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String ListaTotalJSON = gson.toJson(listaTotal);
        editor.putString("ListaCompra",ListaTotalJSON);
        editor.commit();
    }


}
