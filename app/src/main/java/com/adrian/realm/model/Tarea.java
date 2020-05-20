package com.adrian.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Tarea extends RealmObject {
    @PrimaryKey()
    public int id;

    public String descripcion;
    // Campo de la version 0
    // public String fecha;

    // Nuevo campo de la version 1
    public String prioridad;

    public Tarea(String descripcion, String prioridad, int prioridadId) {
        this.descripcion = descripcion;
        this.prioridad = prioridad;
    }

    public Tarea(){

    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}
