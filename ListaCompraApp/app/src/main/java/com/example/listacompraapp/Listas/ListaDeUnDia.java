package com.example.listacompraapp.Listas;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaDeUnDia {

    private int num_articulos;
    private int lim_articulos;
    private Articulo[] articulos;
    private Date fecha_dia;
    private double gastoDia;


    ListaDeUnDia(Articulo[] articulos_, int num_articulos_, Date fecha_dia_){
        num_articulos = num_articulos_;
        lim_articulos = num_articulos;
        articulos = new Articulo[num_articulos];
        fecha_dia = fecha_dia_;

        for(int i = 0; i < num_articulos; i++)
            articulos[i] = articulos_[i];

        this.calcularGasto();

    }

    public ListaDeUnDia(Date fecha_dia_){
        lim_articulos = 10;
        num_articulos = 0;
        articulos = new Articulo[lim_articulos];
        fecha_dia = fecha_dia_;
        gastoDia = 0.0;
    }

    public List<Articulo> getListaArticulos(){
        List<Articulo> ListaArticulos = new ArrayList<>();

        for(int i = 0; i < num_articulos; i++)
            ListaArticulos.add(articulos[i]);

        return ListaArticulos;
    }

    public void calcularGasto(){
        double gasto_Final = 0.0;

        for(int i = 0; i < num_articulos; i++){
            if(articulos[i].isTienePrecio())
                gasto_Final += articulos[i].getPrecio();
        }

        gastoDia = gasto_Final;
    }

    public void addArticulo(Articulo articulo){

        if(num_articulos > lim_articulos) {
            Articulo[] articulos_aux = new Articulo[2*lim_articulos];
            lim_articulos = 2*lim_articulos;
            for (int i = 0; i < num_articulos; i++)
                articulos_aux[i] = articulos[i];
            articulos = articulos_aux;
        }
        articulos[num_articulos] = articulo;
        num_articulos++;
        this.calcularGasto();
    }

    public void removeArticulo(int pos){

        for(int i = pos; i < num_articulos-1; i++)
            articulos[i] = articulos[i+1];

        num_articulos--;
        this.calcularGasto();
    }

    public String[] getCategorias(){

        String[] categorias = new String[num_articulos];
        int indCategorias = 0;

        for(int i = 0; i < num_articulos; i++){
            if(i==0){
                categorias[indCategorias] = articulos[i].getCategoria();
                indCategorias++;
            }
            else{
                boolean repetido = false;
                for(int j = 0; j < indCategorias; j++){
                    if(categorias[j].equals(articulos[i].getCategoria()))
                        repetido = true;
                }

                if(!repetido){
                    categorias[indCategorias] = articulos[i].getCategoria();
                    indCategorias++;
                }
            }
        }

        String[] categoriasFinales = new String[indCategorias];
        for(int i = 0; i < indCategorias; i++)
            categoriasFinales[i] = categorias[i];

        Log.i("IndCategorias:",""+indCategorias);

        return categoriasFinales;
    }

    public Articulo getArticulo(int pos){
        return articulos[pos];

    }

    public void setArticulo(int pos, Articulo articulo){
        articulos[pos] = articulo;
    }

    public int getNumVecesCategoria(String categoria){
        int numVeces = 0;

        for(int i = 0; i < num_articulos; i++){
            if(categoria.equals(articulos[i].getCategoria()))
                numVeces++;
        }

        return numVeces;

    }


    public String getCategoriaMasComprada(){

        String categoriaMasComprada = new String();
        String[] categorias = this.getCategorias();
        int[] NumVecesCategorias = new int[categorias.length];

        for(int i = 0; i < categorias.length; i++){
            NumVecesCategorias[i] = 0;
            for(int j = 0; j < num_articulos; j++){
                if(categorias[i].equals(articulos[j].getCategoria()))
                    NumVecesCategorias[i]++;
            }
        }

        int indPosMasComprada = 0;
        for(int i = 0; i < categorias.length; i++){
            if(i==0){
                indPosMasComprada = i;
                categoriaMasComprada = categorias[i];
            }
            else{
                if(NumVecesCategorias[indPosMasComprada] < NumVecesCategorias[i]){
                    indPosMasComprada = i;
                    categoriaMasComprada = categorias[i];
                }
            }
        }

        return categoriaMasComprada;
    }


    public int getNum_articulos() {
        return num_articulos;
    }

    public void setNum_articulos(int num_articulos) {
        this.num_articulos = num_articulos;
    }

    public Articulo[] getArticulos() {
        return articulos;
    }

    public void setArticulos(Articulo[] articulos) {
        this.articulos = articulos;
    }

    public Date getFecha_dia() {
        return fecha_dia;
    }

    public void setFecha_dia(Date fecha_dia) {
        this.fecha_dia = fecha_dia;
    }

    public double getGastoDia() {
        return gastoDia;
    }

    public void setGastoDia(double gastoDia) {
        this.gastoDia = gastoDia;
    }
}
