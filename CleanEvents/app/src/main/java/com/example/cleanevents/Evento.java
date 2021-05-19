package com.example.cleanevents;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class Evento implements Serializable {

    int idEvento;
    String nombre;
    int idUsuario;
    String tipoActividad;
    String imagen;
    Fecha fecha;
    String descripcion;
    String poblacion;
    GeoPoint geopoint;
    Integer numParticipantes;


    public Evento() {
        this.idEvento = 0;
        this.nombre = "";
        this.idUsuario = 0;
        this.tipoActividad = "";
        this.imagen = "";
        this.fecha = null;
        this.descripcion = "";
        this.poblacion = "";
        this.geopoint = null;
        this.numParticipantes = 0;
    }

    public Evento(int idEvento, String nombre, int idUsuario, String tipoActividad,
                  String imagen, Fecha fecha, String descripcion, String poblacion, GeoPoint geopoint, int numParticipantes) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
        this.tipoActividad = tipoActividad;
        this.imagen = imagen;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.poblacion = poblacion;
        this.geopoint = geopoint;
        this.numParticipantes = numParticipantes;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public GeoPoint getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(GeoPoint geopoint) {
        this.geopoint = geopoint;
    }

    public int getNumParticipantes() {
        return numParticipantes;
    }

    public void setNumParticipantes(int numParticipantes) {
        this.numParticipantes = numParticipantes;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "idEvento=" + idEvento +
                ", nombre='" + nombre + '\'' +
                ", idUsuario=" + idUsuario +
                ", tipoActividad='" + tipoActividad + '\'' +
                ", imagen='" + imagen + '\'' +
                ", fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                ", poblacion='" + poblacion + '\'' +
                ", geopoint=" + geopoint +
                ", numParticipantes=" + numParticipantes +
                '}';
    }
}
