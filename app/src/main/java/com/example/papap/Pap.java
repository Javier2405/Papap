package com.example.papap;

import java.util.List;

public class Pap {

    private String nombre;
    private Integer calorias;
    private List<String> ingredientes;
    private List<String> pasos;

    public Pap(String nombre, Integer calorias, List<String> ingredientes, List<String> pasos){
        this.nombre = nombre;
        this.calorias = calorias;
        this.ingredientes = ingredientes;
        this.pasos = pasos;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getCalorias() {
        return calorias;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public List<String> getPasos() {
        return pasos;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCalorias(Integer calorias) {
        this.calorias = calorias;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void setPasos(List<String> pasos) {
        this.pasos = pasos;
    }
}
