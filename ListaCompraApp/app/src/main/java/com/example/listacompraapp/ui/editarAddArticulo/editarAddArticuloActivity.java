package com.example.listacompraapp.ui.editarAddArticulo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.listacompraapp.Listas.Articulo;
import com.example.listacompraapp.Listas.ListaDeUnDia;
import com.example.listacompraapp.Listas.ListaTotal;
import com.example.listacompraapp.R;
import com.google.gson.Gson;

import java.awt.font.TextAttribute;
import java.util.Calendar;

public class editarAddArticuloActivity extends AppCompatActivity {

    private ListaTotal listaTotal;
    private TextView editarAdd;
    private EditText EditNombre;
    private EditText EditCategoria;
    private EditText EditPrecio;
    private Button botonCambios;

    private int AddEditar;
    private int posDia;
    private int posArticulo;
    private Articulo articulo;



    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion_de_articulos);

        editarAdd = findViewById(R.id.editar_add);
        EditNombre = findViewById(R.id.editText_nombre_articulo);
        EditCategoria = findViewById(R.id.editText_categoria_articulo);
        EditPrecio = findViewById(R.id.editText_precio_articulo);
        botonCambios = findViewById(R.id.button_aplicar_cambios);
        this.cargarListaTotal();

        Bundle bundleAddEditar = this.getIntent().getExtras();
        AddEditar = bundleAddEditar.getInt("AñadirEditar");

        if(bundleAddEditar != null){
            if(AddEditar == 1)
                editarAdd.setText("Añadir nuevo articulo");
            else{
                editarAdd.setText("Editar articulo");
                posDia = bundleAddEditar.getInt("PosDia");
                posArticulo = bundleAddEditar.getInt("PosArticulo");
                articulo = listaTotal.getListaDiaPos(posDia).getArticulo(posArticulo);

                EditNombre.setText(articulo.getNombre());
                if(articulo.isTienePrecio())
                    EditPrecio.setText(""+articulo.getPrecio());
                EditCategoria.setText(articulo.getCategoria());
            }
        }
        else
            editarAdd.setText("Añadir nuevo articulo");

    }

    public void cargarListaTotal(){
        SharedPreferences sharedPreferences = getSharedPreferences("ListaTotal", Context.MODE_PRIVATE);
        String ListaTotalJSON = sharedPreferences.getString("ListaCompra","");
        Gson gson = new Gson();
        listaTotal = gson.fromJson(ListaTotalJSON,ListaTotal.class);
    }

    public void aplicarCambios(View view){
        if(view.getId() == botonCambios.getId()){
            Calendar calendario = Calendar.getInstance();
            Articulo articulo_aux;

            if(AddEditar == 1){
                if(EditNombre.getText().toString().equals("") || EditCategoria.getText().toString().equals(""))
                    Toast.makeText(this,"Debes introducir al menos un nombre y categoria",Toast.LENGTH_LONG).show();
                else{
                    if(EditPrecio.getText().toString().equals(""))
                        articulo_aux = new Articulo(EditNombre.getText().toString(),EditCategoria.getText().toString(),calendario.getTime());
                    else
                        articulo_aux = new Articulo(EditNombre.getText().toString(),EditCategoria.getText().toString(),Double.valueOf(EditPrecio.getText().toString()),calendario.getTime());

                    listaTotal.getDia(calendario).addArticulo(articulo_aux);
                    listaTotal.calcularGastoTotal();
                    this.guardarListaTotal();
                    finish();
                }
            }
            else{
                if(EditNombre.getText().toString().equals("") || EditCategoria.getText().toString().equals(""))
                    Toast.makeText(this,"Debes introducir al menos un nombre y categoria",Toast.LENGTH_LONG).show();
                else{
                    Log.i("Categoria",EditCategoria.getText().toString());
                    calendario.setTime(articulo.getFecha());
                    if(EditPrecio.getText().toString().equals(""))
                        articulo_aux = new Articulo(EditNombre.getText().toString(),EditCategoria.getText().toString(),calendario.getTime());
                    else
                        articulo_aux = new Articulo(EditNombre.getText().toString(),EditCategoria.getText().toString(),Double.valueOf(EditPrecio.getText().toString()),calendario.getTime());

                    Log.i("PosDia",""+posDia);
                    Log.i("posArticulo",""+posArticulo);
                    ListaDeUnDia listaDia = listaTotal.getListaDiaPos(posDia);
                    listaDia.setArticulo(posArticulo,articulo_aux);
                    listaTotal.setListaDia(posDia,listaDia);
                    listaTotal.calcularGastoTotal();
                    this.guardarListaTotal();

                    finish();
                }
            }

        }
    }

    public void guardarListaTotal(){
        SharedPreferences sharedPreferences = getSharedPreferences("ListaTotal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String ListaTotalJSON = gson.toJson(listaTotal);
        Log.i("ListaTotalJSON",ListaTotalJSON);
        editor.putString("ListaCompra",ListaTotalJSON);
        editor.commit();

    }



}
