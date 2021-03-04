package com.example.app_dietas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_dietas.ui.InicioSesion.CargaPantalla;
import com.example.app_dietas.ui.InicioSesion.InicioSesion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    //private AppBarConfiguration mAppBarConfiguration;

    EditText CorreoElectronico;
    EditText Contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inicio);

        CorreoElectronico = (EditText) findViewById(R.id.email_edit);
        Contraseña = (EditText) findViewById(R.id.contraseña_edit);


        /*
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_progreso, R.id.nav_calendario, R.id.nav_dieta_de_hoy, R.id.nav_info_nutricional, R.id.nav_info_app, R.id.nav_opciones)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/
    }


    public void IntroducirCredenciales(View view){

        if(view.getId() == R.id.boton_inicio){
            String correoElectronico_ = CorreoElectronico.getText().toString();
            String contraseña_ = Contraseña.getText().toString();

            if(correoElectronico_.equals("AppDietas@gmail.com") && contraseña_.equals("AppDieta")){
                Intent intentInicio_Carga = new Intent(MainActivity.this, CargaPantalla.class);
                Bundle bundleInicio_Carga = new Bundle();
                int pos_arroba = correoElectronico_.indexOf("@");
                String NombreUsuario = correoElectronico_.substring(0,pos_arroba);
                bundleInicio_Carga.putString("NombreUsuario",NombreUsuario);
                intentInicio_Carga.putExtras(bundleInicio_Carga);
                startActivity(intentInicio_Carga);
            }
            else {
                Toast.makeText(this,"Credenciales introducidos incorrectos",Toast.LENGTH_LONG).show();
            }

        }
    }


    /*
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
    }*/
}
