package com.example.app_dietas.ui.InicioSesion;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.app_dietas.Dietas.DietaAct;
import com.example.app_dietas.Dietas.DietaProgreso;
import com.example.app_dietas.Dietas.DietaUsuario;
import com.example.app_dietas.ui.ventana_principal.*;
import com.example.app_dietas.R;
import com.example.app_dietas.logica.LogicaCSVs;
import com.google.gson.Gson;

import java.io.Serializable;

import javax.microedition.khronos.opengles.GL10;

public class CargaPantalla extends AppCompatActivity {

    //Esta clase debe de servir para cargar las dietas y la ficha del usuario

    ProgressBar BarraProgreso;
    String Usuario;
    TextView cargaText;

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences sharedPref_ = getSharedPreferences("SesionIniciada", Context.MODE_PRIVATE);
        int sesionIniciada = sharedPref_.getInt("SesionIniciada",0);

        if(sesionIniciada == 2){
            finish();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.pantalla_de_carga_inicial);

        BarraProgreso = findViewById(R.id.progressBarCargaInicial);
        Bundle bundleCargaPantalla = this.getIntent().getExtras();
        BarraProgreso.setProgress(0,true);
        BarraProgreso.setMax(100);
        cargaText = findViewById(R.id.cargando_text);
        cargaText.setText("Cargando dietas...");


        if(bundleCargaPantalla != null){
            Usuario = bundleCargaPantalla.getString("NombreUsuario");
        }
        else{
            Intent intentCargaPantallaToActividadPrincipal  = new Intent(CargaPantalla.this, ventana_principal.class);
            startActivity(intentCargaPantallaToActividadPrincipal);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void procesoCarga(View view) throws InterruptedException {

            this.checkPermisos();

            SharedPreferences sharedPref_ = getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
            String DietasDelUsuarioJSON = sharedPref_.getString("DietaDelUsuario","");
            Log.i("Dietajson",DietasDelUsuarioJSON);


            if(DietasDelUsuarioJSON.equals("")){
                Thread.sleep(5000);
                LogicaCSVs logicaCSVs = new LogicaCSVs();
                BarraProgreso.setProgress(10,true);
                cargaText.setText("Cargando dietas... 10%");
                DietaAct dietaActual = logicaCSVs.cargarDietaActual(Usuario);
                BarraProgreso.setProgress(20,true);
                cargaText.setText("Cargando dietas... 20%");
                DietaProgreso dietaProgreso = logicaCSVs.cargarDietaProgreso(Usuario);
                BarraProgreso.setProgress(40,true);
                cargaText.setText("Cargando dietas... 40%");
                String[] NombreApellidosYRecomendaciones = new String[3];
                logicaCSVs.extraccionInfoUsuario(Usuario,NombreApellidosYRecomendaciones);
                DietaUsuario dietaDelUsuario = new DietaUsuario(dietaActual,dietaProgreso,NombreApellidosYRecomendaciones[0],NombreApellidosYRecomendaciones[1],NombreApellidosYRecomendaciones[2]);
                BarraProgreso.setProgress(50,true);
                cargaText.setText("Cargando dietas... 50%");


                Toast.makeText(this,dietaDelUsuario.getNombre()+" "+dietaDelUsuario.getApellidos(),Toast.LENGTH_LONG).show();
                sharedPref_ = getSharedPreferences("Dietas_y_Progreso", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor  = sharedPref_.edit();
                BarraProgreso.setProgress(70,true);
                cargaText.setText("Cargando dietas... 70%");
                Gson gson = new Gson();

                BarraProgreso.setProgress(80,true);
                cargaText.setText("Cargando dietas... 80%");
                String jsonDietaDelUsuario = gson.toJson(dietaDelUsuario);
                BarraProgreso.setProgress(90,true);
                cargaText.setText("Cargando dietas... 90%");
                editor.putString("DietaDelUsuario",jsonDietaDelUsuario);
                editor.commit();
                BarraProgreso.setProgress(100,true);
                cargaText.setText("Cargando dietas... 100%");
            }

            Intent intentCargaPantallaToActividadPrincipal  = new Intent(CargaPantalla.this, ventana_principal.class);
            startActivity(intentCargaPantallaToActividadPrincipal);
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

            if(permissionCheckCalendario != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 225);


        }

    }

}
