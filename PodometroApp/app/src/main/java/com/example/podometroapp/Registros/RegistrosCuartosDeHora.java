package com.example.podometroapp.Registros;

import android.util.Log;

import java.util.Date;

public class RegistrosCuartosDeHora {

    //Saber que pasos se han dado
    private int pasosDados;
    private long contadorMinutosEnMilis;
    private double mPorZancada;

    //Datos que queremos mostrar
    private double distanciaRecorrida; //En Km
    private double velocidadMediaKmH;
    private double caloriasQuemadas;
    private int tiempoActivoMin;
    private long tiempoActivoMilis;

    //Marca de tiempo - Esta completo
    private boolean isCompleto;
    private Date fecha;

    //DATOS
    private int edad;
    private double peso;
    private int sexo;
    private double altura;

    public RegistrosCuartosDeHora(int pasosDados, long tiempoEnMilis, Date fecha, int edad, int sexo, double peso, double altura){
        this.edad = edad;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
        mPorZancada = altura * 0.415;

        this.fecha = fecha;
        isCompleto = false;
        this.pasosDados = 0;
        distanciaRecorrida = 0.0;
        tiempoActivoMilis = 0;
        contadorMinutosEnMilis = 0;

        caloriasQuemadas = 0.0;
        this.addPasos(pasosDados, tiempoEnMilis);
    }

    public RegistrosCuartosDeHora(Date fecha, int edad, int sexo, double peso, double altura){
        this.edad = edad;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
        mPorZancada = altura * 0.415;

        this.fecha = fecha;
        isCompleto = false;
        this.pasosDados = 0;
        distanciaRecorrida = 0.0;
        tiempoActivoMilis = 0;

        velocidadMediaKmH = 0.0;
        caloriasQuemadas = 0.0;
        contadorMinutosEnMilis = 0;
    }


    //
    public void calcularDatosImportantesDirecto(){

        if(contadorMinutosEnMilis >= 15*60*1000000){
            isCompleto = true;
            if(contadorMinutosEnMilis != 0){
                double tiempoEnMin = (double) contadorMinutosEnMilis/ 1000000.0;
                double velocidadMediaKMMin = distanciaRecorrida/(60*tiempoEnMin);
                velocidadMediaKmH = 60.0*velocidadMediaKMMin;
                Log.i("DistanciaRecorrida:", distanciaRecorrida+"");
                Log.i("VelocidadKMMin:", velocidadMediaKMMin+"");
                Log.i("VelocidadKmH:", velocidadMediaKmH+"");
                this.calcularCaloriasQuemadas();
            }
        }
        else{
            if(contadorMinutosEnMilis != 0){
                double tiempoEnMin = (double) contadorMinutosEnMilis/ 1000000.0;
                double velocidadMediaKMMin = distanciaRecorrida/(60*tiempoEnMin);
                velocidadMediaKmH = 60.0*velocidadMediaKMMin;
                Log.i("DistanciaRecorrida:", distanciaRecorrida+"");
                Log.i("VelocidadKMMin:", velocidadMediaKMMin+"");
                Log.i("VelocidadKmH:", velocidadMediaKmH+"");
                this.calcularCaloriasQuemadas();
            }
        }

    }

    public void addPasos(int pasosDados_, long tiempoEnMilis){

        if(isCompleto == false){
            pasosDados += pasosDados_;
            contadorMinutosEnMilis += tiempoEnMilis;
            Log.i("contadorMinutosEnMilis",String.valueOf(contadorMinutosEnMilis));

            double distanciaAuxKm = pasosDados_*mPorZancada/1000.0;
            double velocidadMediaKmS = 1000000.0*distanciaAuxKm/tiempoEnMilis;
            double velocidadMediaKmH_ = 3600*velocidadMediaKmS;
            tiempoActivoMilis += tiempoEnMilis;
            tiempoActivoMin = (int) (tiempoActivoMilis/(1000000));
            Log.i("TiempoActivoMin",""+tiempoActivoMin);
            distanciaRecorrida += distanciaAuxKm;

            if(contadorMinutosEnMilis != 0){
                double tiempoEnMin = (double) contadorMinutosEnMilis/1000000.0;
                double velocidadMediaKMMin = distanciaRecorrida/(60*tiempoEnMin);
                velocidadMediaKmH = 60.0*velocidadMediaKMMin;
            }

            this.calcularDatosImportantesDirecto();
            this.calcularCaloriasQuemadas();
        }
    }

    public void calcularCaloriasQuemadas(){
        double VO2Max, FCC;
        double METs;
        double KcalHora;

        FCC = (220-edad)*0.5;

        if(velocidadMediaKmH > 6.0)
            FCC = (220-edad)*(0.5+0.5*(velocidadMediaKmH-6.0)/11.5);


        if(distanciaRecorrida != 0.0){
            double tiempoEnCompletarSegundos = (double) contadorMinutosEnMilis/(distanciaRecorrida*1000000.0);
            Log.i("tiempoEnSegundos",""+tiempoEnCompletarSegundos);

            VO2Max = 132.853 - (0.1692*peso) -(0.3977*edad)+(6.315*sexo)-(3.2649*1.609*tiempoEnCompletarSegundos/60.0)-(0.156*FCC);
            METs = VO2Max/3.5;
            KcalHora = METs*peso;
            double porcentaje = (double) contadorMinutosEnMilis/(3600.0*1000000.0);
            caloriasQuemadas = KcalHora*porcentaje;
            Log.i("caloriasQuemadas",""+caloriasQuemadas);
            }
        else
            caloriasQuemadas = 0.0;
    }

    public int getPasosDados() {
        return pasosDados;
    }

    public void setPasosDados(int pasosDados) {
        this.pasosDados = pasosDados;
    }

    public long getContadorMinutosEnMilis() {
        return contadorMinutosEnMilis;
    }

    public void setContadorMinutosEnMilis(long contadorMinutosEnMilis) {
        this.contadorMinutosEnMilis = contadorMinutosEnMilis;
    }

    public double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(double distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

    public double getVelocidadMediaKmH() {
        return velocidadMediaKmH;
    }

    public void setVelocidadMediaKmH(double velocidadMediaKmH) {
        this.velocidadMediaKmH = velocidadMediaKmH;
    }

    public double getCaloriasQuemadas() {
        return caloriasQuemadas;
    }

    public void setCaloriasQuemadas(double caloriasQuemadas) {
        this.caloriasQuemadas = caloriasQuemadas;
    }

    public long getTiempoActivoMilis() {
        return tiempoActivoMilis;
    }

    public void setTiempoActivoMilis(int tiempoActivoMilis) {
        this.tiempoActivoMilis = tiempoActivoMilis;
    }

    public boolean isCompleto() {
        return isCompleto;
    }

    public void setCompleto(boolean completo) {
        isCompleto = completo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getTiempoActivoMin() {
        return tiempoActivoMin;
    }

    public void setTiempoActivoMin(int tiempoActivoMin) {
        this.tiempoActivoMin = tiempoActivoMin;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }
}
