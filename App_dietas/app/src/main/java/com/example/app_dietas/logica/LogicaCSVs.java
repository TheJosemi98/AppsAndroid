package com.example.app_dietas.logica;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.app_dietas.Dietas.Alimento;
import com.example.app_dietas.Dietas.ComidaDieta;
import com.example.app_dietas.Dietas.ComidaProgreso;
import com.example.app_dietas.Dietas.DietaAct;
import com.example.app_dietas.Dietas.DietaProgreso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogicaCSVs {

    public DietaAct cargarDietaActual(String usuario){
        //Checkeo de los permisos de lectura y escritura


        Alimento[] AlimentoLista = this.extraccionAlimentosDietaActual(usuario);
        ComidaDieta[] ComidasDeLaDietaActual = this.organizacionComidasDelDiaDietaActual(AlimentoLista);

        int[] num_dias_info = new int[2];
        this.extraccionDiasInfo(usuario,num_dias_info);

        DietaAct DietaResultado = new DietaAct(ComidasDeLaDietaActual, num_dias_info[0],num_dias_info[1]);

        return DietaResultado;

    }


    public Alimento[] extraccionAlimentosDietaActual(String usuario){
        Alimento[] AlimentoLista = new Alimento[1000];

        boolean cabecera = true;
        boolean alternativa, equivalentes;
        int ind_alimentos = 0;
        BufferedReader br = null;

        try{
            File archivo  = new File("/storage/emulated/0/CSVFiles/"+usuario+"/DietaActual.csv");
            //FileReader fr = new FileReader(archivo);
            br = new BufferedReader( new InputStreamReader(new FileInputStream(archivo),"ISO-8859-1"));
            String line = br.readLine();
            Alimento alimento_aux;


            while (null!=line){
                String[] fields = line.split(";");

                if(cabecera)
                    cabecera = false;
                else{

                    if(fields[10].equals("Si"))
                        alternativa = true;
                    else
                        alternativa = false;

                    if(fields[11].equals("Si"))
                        equivalentes = true;
                    else
                        equivalentes = false;


                    if(fields[12].equals("-") == false){
                        alimento_aux = new Alimento(Integer.parseInt(fields[0]),fields[1],fields[2],fields[3],fields[4],Double.parseDouble(fields[5]),Double.parseDouble(fields[6]),Double.parseDouble(fields[7]),Double.parseDouble(fields[8]),fields[9],alternativa,equivalentes,Integer.parseInt(fields[12]),fields[13]);
                    }
                    else{
                        alimento_aux = new Alimento(Integer.parseInt(fields[0]),fields[1],fields[2],fields[3],fields[4],Double.parseDouble(fields[5]),Double.parseDouble(fields[6]),Double.parseDouble(fields[7]),Double.parseDouble(fields[8]),fields[9],alternativa,equivalentes,fields[13]);
                    }
                    AlimentoLista[ind_alimentos] = alimento_aux;
                    ind_alimentos++;
                }

                line = br.readLine();
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        Alimento[] AlimentoListaFinal = new Alimento[ind_alimentos];

        for(int i = 0; i < ind_alimentos; i++)
            AlimentoListaFinal[i] = AlimentoLista[i];

        return AlimentoListaFinal;

    }

    public ComidaDieta[] organizacionComidasDelDiaDietaActual(Alimento[] AlimentoLista){

        int num_list = AlimentoLista.length;
        ComidaDieta[] comidaDietaAux = new ComidaDieta[7];
        Alimento[][] AlimentoComida = new Alimento[4][10];
        Alimento[][][] Equivalentes = new Alimento[4][10][10];
        int[] num_alimentos = new int[4];
        for(int i = 0; i < 4; i++)
            num_alimentos[i] = 0;
        int[][] num_equivalentes = new int[4][10];

        String[] dias_semana = {"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"};

        int ind_num_comida = 0;

        for(int i = 0; i < 7; i++){

            for(int z = 0; z < 4; z++)
                num_alimentos[z] = 0;

            for(int v = 0; v < 4; v++){
                for(int j = 0; j < 10; j++)
                    num_equivalentes[v][j] = 0;
            }

            for(int j = 0; j < num_list; j++){
                if(AlimentoLista[j].getDiaDeLaSemana().equals(dias_semana[i]) || AlimentoLista[j].getDiaDeLaSemana().equals("Todos los días")){


                    //Fijar ComidaDelDiaTipo
                    if(AlimentoLista[j].getComidaDelDiaTipo().equals("Desayuno"))
                        ind_num_comida = 0;
                    else if(AlimentoLista[j].getComidaDelDiaTipo().equals("Almuerzo"))
                        ind_num_comida = 1;
                    else if(AlimentoLista[j].getComidaDelDiaTipo().equals("Merienda"))
                        ind_num_comida = 2;
                    else if(AlimentoLista[j].getComidaDelDiaTipo().equals("Cena"))
                        ind_num_comida = 3;

                    if(AlimentoLista[j].isAlternativa()){
                        //Se coloca como equivalente
                        for(int z = 0; z < num_alimentos[ind_num_comida]; z++){
                            if(AlimentoLista[j].isEquivalentes()){
                                if(AlimentoComida[ind_num_comida][z].getAlternableGrupo() == AlimentoLista[j].getAlternableGrupo()){
                                    int ind_pos_equivalente = num_equivalentes[ind_num_comida][z];
                                    Equivalentes[ind_num_comida][z][ind_pos_equivalente] = AlimentoLista[j];
                                    num_equivalentes[ind_num_comida][z]++;
                                }
                            }
                        }
                    }
                    else{
                        AlimentoComida[ind_num_comida][num_alimentos[ind_num_comida]] = AlimentoLista[j];
                        num_alimentos[ind_num_comida]++;
                    }

                }
            }

            comidaDietaAux[i] = new ComidaDieta(AlimentoComida,num_alimentos,Equivalentes,num_equivalentes);
        }

        return comidaDietaAux;

    }

    public void extraccionInfoUsuario(String usuario, String[] nombreApellidosYRecomendaciones){

        BufferedReader br = null;
        boolean cabecera = true;

        try{
            File archivo  = new File("/storage/emulated/0/CSVFiles/"+usuario+"/InfoUsuario.csv");
            //FileReader fr = new FileReader(archivo);
            //br = new BufferedReader(fr);
            br = new BufferedReader( new InputStreamReader(new FileInputStream(archivo),"ISO-8859-1"));
            String line = br.readLine();

            while (null!=line){
                String[] fields = line.split(";");
                if(cabecera)
                    cabecera = false;
                else{
                    for(int i = 0; i < 3; i++)
                            nombreApellidosYRecomendaciones[i] = fields[i];
                }
                line = br.readLine();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void extraccionDiasInfo(String usuario, int[] num_dias_info){

        BufferedReader br = null;
        boolean cabecera = true;

        try{
            File archivo  = new File("/storage/emulated/0/CSVFiles/"+usuario+"/InfoUsuario.csv");
            //FileReader fr = new FileReader(archivo);
            //br = new BufferedReader(fr);
            br = new BufferedReader( new InputStreamReader(new FileInputStream(archivo),"ISO-8859-1"));
            String line = br.readLine();

            while (null!=line){
                String[] fields = line.split(";");
                if(cabecera)
                    cabecera = false;
                else{
                    for(int i = 3; i < 5; i++)
                        num_dias_info[i-3] = Integer.parseInt(fields[i]);
                }
                line = br.readLine();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }


    public DietaProgreso cargarDietaProgreso(String usuario){

        File archivo  = new File("/storage/emulated/0/CSVFiles/"+usuario+"/DietaProgreso.csv");
        ComidaProgreso[] ComidasDeDietaProgreso;
        DietaProgreso dietaProgreso;

        if(archivo.exists()){
            ComidasDeDietaProgreso = this.organizacionComidasDelDiaProgreso(usuario);
            dietaProgreso = new DietaProgreso(ComidasDeDietaProgreso,ComidasDeDietaProgreso.length);
        }
        else{
            dietaProgreso = new DietaProgreso();
        }

        return dietaProgreso;

    }

    public ComidaProgreso[] organizacionComidasDelDiaProgreso(String usuario){

        ComidaProgreso[] comidaProgreso = new ComidaProgreso[100];
        int ind_comida = 0;
        boolean cabecera = true;
        boolean alternativa, equivalentes;
        BufferedReader br = null;

        Date fecha_comida = new Date();
        Date fecha_nueva = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        boolean comienzo = true;
        boolean diaLibre_ = true;
        boolean confirmado_;

        Alimento[][] alimentosPorDiaAux_ = new Alimento[4][10];

        int[] num_alimentos = new int[4];
        for(int i = 0; i < 4; i++)
            num_alimentos[i] = 0;

        boolean[][] confirmados = new boolean[4][10];


        try{
            File archivo  = new File("/storage/emulated/0/CSVFiles/"+usuario+"/DietaProgreso.csv");
            //FileReader fr = new FileReader(archivo);
            //br = new BufferedReader(fr);
            br = new BufferedReader( new InputStreamReader(new FileInputStream(archivo),"ISO-8859-1"));
            String line = br.readLine();
            Alimento alimento_aux;



            while (null!=line){
                String[] fields = line.split(";");
                if(cabecera){
                    cabecera = false;
                }
                else{

                    if(fields[0].equals("Si"))
                        diaLibre_ = true;
                    else
                        diaLibre_ = false;

                    if(fields[2].equals("Si"))
                        confirmado_ = true;
                    else
                        confirmado_ = false;

                    if(fields[13].equals("Si"))
                        alternativa = true;
                    else
                        alternativa = false;

                    if(fields[14].equals("Si"))
                        equivalentes = true;
                    else
                        equivalentes = false;


                    if(comienzo)
                        fecha_comida = dateFormat.parse(fields[1]);
                    else
                        fecha_nueva = dateFormat.parse(fields[1]);

                    if(fields.length == 17)
                        alimento_aux = new Alimento(Integer.parseInt(fields[3]),fields[4],fields[5],fields[6],fields[7],Double.parseDouble(fields[8]),Double.parseDouble(fields[9]),Double.parseDouble(fields[10]),Double.parseDouble(fields[11]),fields[12],alternativa,equivalentes,Integer.parseInt(fields[15]),fields[16]);
                    else
                        alimento_aux = new Alimento(Integer.parseInt(fields[3]),fields[4],fields[5],fields[6],fields[7],Double.parseDouble(fields[8]),Double.parseDouble(fields[9]),Double.parseDouble(fields[10]),Double.parseDouble(fields[11]),fields[12],alternativa,equivalentes,fields[15]);


                    if(fecha_nueva.compareTo(fecha_comida) == 1 && comienzo==false){

                        comidaProgreso[ind_comida] = new ComidaProgreso(fecha_comida, alimentosPorDiaAux_, num_alimentos, confirmados);
                        comidaProgreso[ind_comida].setDia_libre(diaLibre_);
                        alimentosPorDiaAux_ = new Alimento[4][10];
                        confirmados = new boolean[4][10];
                        for(int i = 0; i < 4; i++)
                            num_alimentos[i] = 0;
                        ind_comida++;
                        fecha_comida = fecha_nueva;
                    }

                    int pos_comida = 0;

                    if(alimento_aux.getComidaDelDiaTipo().equals("Desayuno"))
                        pos_comida = 0;
                    else if(alimento_aux.getComidaDelDiaTipo().equals("Alumerzo"))
                        pos_comida = 1;
                    else if(alimento_aux.getComidaDelDiaTipo().equals("Merienda"))
                        pos_comida = 2;
                    else if(alimento_aux.getComidaDelDiaTipo().equals("Cena"))
                        pos_comida = 3;

                    alimentosPorDiaAux_[pos_comida][num_alimentos[pos_comida]] = alimento_aux;
                    confirmados[pos_comida][num_alimentos[pos_comida]] = confirmado_;
                    num_alimentos[pos_comida]++;

                    if(comienzo)
                        comienzo = false;

                }
                line = br.readLine();
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        comidaProgreso[ind_comida] = new ComidaProgreso(fecha_comida, alimentosPorDiaAux_, num_alimentos, confirmados);
        comidaProgreso[ind_comida].setDia_libre(diaLibre_);
        ind_comida++;

        ComidaProgreso[] comidaProgresoFinal = new ComidaProgreso[ind_comida];

        for(int i = 0; i < ind_comida; i++)
            comidaProgresoFinal[i] = comidaProgreso[i];

        return comidaProgresoFinal;

    }

}
