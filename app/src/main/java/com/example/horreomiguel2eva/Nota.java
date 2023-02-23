package com.example.horreomiguel2eva;

import java.io.Serializable;

public class Nota implements Serializable {

    public String titulo;
    public String contenido;

    public Nota() {
    }

    public Nota(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "titulo='" + titulo + '\'' +
                ", contenido='" + contenido + '\'' +
                '}';
    }
}

