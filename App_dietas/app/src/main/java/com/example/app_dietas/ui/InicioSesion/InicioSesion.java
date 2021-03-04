package com.example.app_dietas.ui.InicioSesion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.app_dietas.R;

public class InicioSesion extends AppCompatActivity {

    EditText CorreoElectronico;
    EditText Contraseña;

    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.inicio);

        CorreoElectronico = (EditText) findViewById(R.id.email_edit);
        Contraseña = (EditText) findViewById(R.id.contraseña_edit);

        SharedPreferences sharedPref_ = getSharedPreferences("SesionIniciada", Context.MODE_PRIVATE);
        int sesionIniciada = sharedPref_.getInt("SesionIniciada",0);


        if(sesionIniciada == 1){
            Intent intentInicio_Carga = new Intent(this,CargaPantalla.class);
            startActivity(intentInicio_Carga);
        }


    }

    public void IntroducirCredenciales(View view){

        if(view.getId() == R.id.boton_inicio){
            String correoElectronico_ = CorreoElectronico.getText().toString();
            String contraseña_ = Contraseña.getText().toString();

            if(correoElectronico_.equals("AppDietas@gmail.com") && contraseña_.equals("AppDieta")){
                SharedPreferences sharedPref_ = getSharedPreferences("SesionIniciada", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref_.edit();
                editor.putInt("SesionIniciada",1);
                editor.commit();

                Toast.makeText(this,"Credenciales correctos",Toast.LENGTH_LONG).show();
                Intent intentInicio_Carga = new Intent(this,CargaPantalla.class);
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

}
