package com.example.asistenciaqr.network;

// Clase modelo para los datos recibidos
public class Person {
    public String idservidor;
    public String nombre;
    public String urlfoto;
    public String codigo;
    public int trabajos;
    // Getters y Setters
    public String getIdservidor() {
        return idservidor;
    }

    public void setIdservidor(String idservidor) {
        this.idservidor = idservidor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlfoto() {
        return urlfoto;
    }

    public void setUrlfoto(String urlfoto) {
        this.urlfoto = urlfoto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getTrabajos() {
        return trabajos;
    }

    public void setTrabajos(int trabajos) {
        this.trabajos = trabajos;
    }
}
