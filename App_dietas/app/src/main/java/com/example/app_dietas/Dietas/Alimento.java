package com.example.app_dietas.Dietas;

public class Alimento {

    private int cantidadNumerica;
    private String cantidadCualitativa;
    private String nombre;
    private String grupoAlimenticio;
    private String comidaDelDiaTipo;
    private double energía;
    private double proteinas;
    private double hidratosCarbono;
    private double grasas;
    private String diaDeLaSemana;
    private boolean alternativa;
    private boolean equivalentes;
    private int alternableGrupo;
    private String observacion;


    Alimento(){

    }

    public Alimento(int cantidadNumerica, String cantidadCualitativa, String nombre, String grupoAlimenticio, String comidaDelDiaTipo, double energía, double proteinas, double hidratosCarbono, double grasas, String diaDeLaSemana, boolean alternativa, boolean equivalentes, int alternableGrupo, String observacion) {
        this.cantidadNumerica = cantidadNumerica;
        this.cantidadCualitativa = cantidadCualitativa;
        this.nombre = nombre;
        this.grupoAlimenticio = grupoAlimenticio;
        this.comidaDelDiaTipo = comidaDelDiaTipo;
        this.energía = energía;
        this.proteinas = proteinas;
        this.hidratosCarbono = hidratosCarbono;
        this.grasas = grasas;
        this.diaDeLaSemana = diaDeLaSemana;
        this.alternativa = alternativa;
        this.equivalentes = equivalentes;
        this.alternableGrupo = alternableGrupo;
        this.observacion = observacion;

    }

    public Alimento(int cantidadNumerica, String cantidadCualitativa, String nombre, String grupoAlimenticio, String comidaDelDiaTipo, double energía, double proteinas, double hidratosCarbono, double grasas, String diaDeLaSemana, boolean alternativa, boolean equivalentes, String observacion) {
        this.cantidadNumerica = cantidadNumerica;
        this.cantidadCualitativa = cantidadCualitativa;
        this.nombre = nombre;
        this.grupoAlimenticio = grupoAlimenticio;
        this.comidaDelDiaTipo = comidaDelDiaTipo;
        this.energía = energía;
        this.proteinas = proteinas;
        this.hidratosCarbono = hidratosCarbono;
        this.grasas = grasas;
        this.diaDeLaSemana = diaDeLaSemana;
        this.alternativa = alternativa;
        this.equivalentes = equivalentes;
        this.observacion = observacion;

    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public void setHidratosCarbono(double hidratosCarbono) {
        this.hidratosCarbono = hidratosCarbono;
    }

    public void setGrasas(double grasas) {
        this.grasas = grasas;
    }

    public boolean isEquivalentes() {
        return equivalentes;
    }

    public void setEquivalentes(boolean equivalentes) {
        this.equivalentes = equivalentes;
    }

    public void setEnergía(double energía) {
        this.energía = energía;
    }

    public String getComidaDelDiaTipo() {
        return comidaDelDiaTipo;
    }

    public void setComidaDelDiaTipo(String comidaDelDiaTipo) {
        this.comidaDelDiaTipo = comidaDelDiaTipo;
    }

    public String getDiaDeLaSemana() {
        return diaDeLaSemana;
    }

    public void setDiaDeLaSemana(String diaDeLaSemana) {
        this.diaDeLaSemana = diaDeLaSemana;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getCantidadNumerica() {
        return cantidadNumerica;
    }

    public void setCantidadNumerica(int cantidadNumerica) {
        this.cantidadNumerica = cantidadNumerica;
    }

    public String getCantidadCualitativa() {
        return cantidadCualitativa;
    }

    public void setCantidadCualitativa(String cantidadCualitativa) {
        this.cantidadCualitativa = cantidadCualitativa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrupoAlimenticio() {
        return grupoAlimenticio;
    }

    public void setGrupoAlimenticio(String grupoAlimenticio) {
        this.grupoAlimenticio = grupoAlimenticio;
    }

    public double getEnergía() {
        return energía;
    }

    public void setEnergía(int energía) {
        this.energía = energía;
    }

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(int proteinas) {
        this.proteinas = proteinas;
    }

    public double getHidratosCarbono() {
        return hidratosCarbono;
    }

    public void setHidratosCarbono(int hidratosCarbono) {
        this.hidratosCarbono = hidratosCarbono;
    }

    public double getGrasas() {
        return grasas;
    }

    public void setGrasas(int grasas) {
        this.grasas = grasas;
    }

    public boolean isAlternativa() {
        return alternativa;
    }

    public void setAlternativa(boolean alternativa) {
        this.alternativa = alternativa;
    }

    public int getAlternableGrupo() {
        return alternableGrupo;
    }

    public void setAlternableGrupo(int alternableGrupo) {
        this.alternableGrupo = alternableGrupo;
    }
}
